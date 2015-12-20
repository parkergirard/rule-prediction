package analysis;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import enums.*;

public class RulePrediction {

	// maps targets to how the child pronounced them
	// sequences separated by vowels
	private Map<PhonemeSequence[], PhonemeSequence[]>
	targetToPronunciation;

	// maps a phoneme to a set of rules for that phoneme
	// (the rules alter in phonetic environments)
	private Map<PHONEME, Set<Rule>> phonemeToRules;

	/**
	 * Construct given a map of strings,
	 * by converting strings into phoneme sequences
	 * @param map: maps target words to pronunciation
	 */
	public RulePrediction(Map<String, String> map) {

		if (map == null) {
			throw new IllegalArgumentException("Map cannot be null");
		}

		this.targetToPronunciation = 
				new HashMap<PhonemeSequence[], PhonemeSequence[]>();

				// convert each string to phoneme sequence
				for (Entry<String, String> e : map.entrySet()) {
					// function will throw error if invalid phoneme
					PhonemeSequence[] target = 
							convertStringToPhonemeSequence(e.getKey());
					PhonemeSequence[] val = 
							convertStringToPhonemeSequence(e.getValue());
					// valid phonemes, add to map
					targetToPronunciation.put(target, val);
				}

				phonemeToRules = new HashMap<PHONEME, Set<Rule>>();
				formRules();
	}

	/**
	 * Analyzes the training set provided, to detect rules
	 */
	private void formRules() {

		// go through each entry in the lexicon
		for (Entry<PhonemeSequence[], PhonemeSequence[]> 
		e : targetToPronunciation.entrySet()) {

			PhonemeSequence[] targetSyllables = e.getKey();
			PhonemeSequence[] actualSyllables = e.getValue();

			if (targetSyllables.length != actualSyllables.length) {
				throw new RuntimeException("Ommisions/Insertions"
						+ " not implemented yet");
			}

			// previous phoneme will be compared to later
			PHONEME previousPhoneme = null;

			// loop through all syllables
			POSITION wordPosition = CONSONANT_POSITION.BEGINNING;
			for (int i = 0; i < targetSyllables.length; i++) {

				List<PHONEME> targetPhonemeSeq = 
						targetSyllables[i].getSequence();
				List<PHONEME> actualPhonemeSeq = 
						actualSyllables[i].getSequence();

				POSITION syllablePosition = CONSONANT_POSITION.BEGINNING;
				// loop through all phonemes in the syllable
				int j = 0;
				for (PHONEME targetPhoneme : targetPhonemeSeq) {
					// ignore if the target is a vowel
					// update to the position that we are in the syllable
					if (j > 0) {
						wordPosition = CONSONANT_POSITION.MIDDLE;
						syllablePosition = CONSONANT_POSITION.MIDDLE;
					}
					if (j == targetPhonemeSeq.size() - 1) {
						syllablePosition = CONSONANT_POSITION.END;
						if (i == targetSyllables.length - 1) {
							wordPosition = CONSONANT_POSITION.END;
						}
					}

					PHONEME actualPhoneme = actualPhonemeSeq.get(j);

					POSITION vowelPosition = null;
					PHONEME nextPhoneme = null;

					// get next phoneme
					if (syllablePosition.equals(CONSONANT_POSITION.END) && 
							!wordPosition.equals(CONSONANT_POSITION.END)) {
						// at end of syllable, but not end of word
						// look at next syllable
						nextPhoneme = targetSyllables[i+1].getSequence().get(0);
					} else if (!wordPosition.equals(CONSONANT_POSITION.END)) {
						// not at end of syllable, and not end of word
						// look at next syllable
						nextPhoneme = targetPhonemeSeq.get(j+1);
					}

					if (previousPhoneme != null && 
							previousPhoneme.getGroup().equals(GROUP.VOWEL) &&
							nextPhoneme != null && 
							nextPhoneme.getGroup().equals(GROUP.VOWEL)) {
						// this phoneme is in the middle of two vowels
						vowelPosition = VOWEL_POSITION.SURROUNDED_BY;
					} else if (previousPhoneme != null && 
							previousPhoneme.getGroup().equals(GROUP.VOWEL)) {
						// this phoneme comes after (the end of) a vowel
						vowelPosition = VOWEL_POSITION.AFTER;
					} else if (nextPhoneme != null && 
							nextPhoneme.getGroup().equals(GROUP.VOWEL)) {
						// this phoneme comes before (the beginning of) a vowel
						vowelPosition = VOWEL_POSITION.BEFORE;
					}

					if (!targetPhoneme.getGroup().equals(GROUP.VOWEL)) {
						// get rules for this phoneme
						Set<Rule> rulesForPhoneme = 
								phonemeToRules.get(targetPhoneme);

						// If target phoneme and actual phoneme are the same:
						if (targetPhoneme.equals(actualPhoneme)) {

							updateRulesIfTransformToSelf(targetPhoneme, 
									rulesForPhoneme, wordPosition, 
									syllablePosition, vowelPosition,
									previousPhoneme, nextPhoneme);

						} else {
							// target phoneme and actual phoneme are NOT the same
							updateRulesIfTransformToOther(targetPhoneme, 
									actualPhoneme,
									rulesForPhoneme, wordPosition, 
									syllablePosition, vowelPosition,
									previousPhoneme, nextPhoneme);
						}
					}

					// move to next phoneme
					j++;
					previousPhoneme = targetPhoneme;
				}
			}

		}

	}

	private void updateRulesIfTransformToOther(PHONEME targetPhoneme, 
			PHONEME actualPhoneme, Set<Rule> rulesForPhoneme,
			POSITION wordPosition, POSITION syllablePosition, POSITION vowelPosition,
			PHONEME previousPhoneme, PHONEME nextPhoneme) {

		// we check this later
		boolean existsRuleFromTargetToActual = false;


		if (rulesForPhoneme != null) {
			// go through every rule for this phoneme
			for (Rule r : rulesForPhoneme) {

				// phonetic environment for the rule
				PhoneticEnvironment ruleEnv = r.getEnvironment();


				FeatureProperties ruleTransformsTo = r.getTransformsToFeatures();

				// If this rule that says target changes to itself
				if (r.getOriginalFeatures().
						equals(r.getTransformsToFeatures())) {

					// this rule says to transform to itself
					// (but the child transformed it to something else)
					// delete the the properties of this environment
					// from the rule
					ruleEnv = modifyProperties(ruleEnv, wordPosition, syllablePosition, 
							vowelPosition, previousPhoneme, nextPhoneme, -1);
					r.setEnvironment(ruleEnv);


				} else if (!ruleTransformsTo.getPlaces().
						contains(actualPhoneme.getPlace()) ||
						!ruleTransformsTo.getManners().
						contains(actualPhoneme.getManner()) ||
						!ruleTransformsTo.getVoices().
						contains(actualPhoneme.getVoice())
						) {

					// this rule says to transform to a phoneme other than
					// itself, and other than the actual phoneme
					// delete the the properties of this environment
					// from the rule
					ruleEnv = modifyProperties(ruleEnv, wordPosition, syllablePosition, 
							vowelPosition, previousPhoneme, nextPhoneme, -1);
					r.setEnvironment(ruleEnv);

				} else if (ruleTransformsTo.getPlaces().
						contains(actualPhoneme.getPlace()) &&
						ruleTransformsTo.getManners().
						contains(actualPhoneme.getManner()) &&
						ruleTransformsTo.getVoices().
						contains(actualPhoneme.getVoice())
						) {

					// this rule says to transform the target phoneme
					// to the actual phoneme (which the child did)

					// add phonetic environment to the rule
					ruleEnv = modifyProperties(ruleEnv, wordPosition, syllablePosition, 
							vowelPosition, previousPhoneme, nextPhoneme, 1);
					r.setEnvironment(ruleEnv);

					existsRuleFromTargetToActual = true;
				}

			}
		}

		// If there isn’t a rule that already says the target phoneme
		// should transform to the actual phoneme

		// construct global rule with this transformation 

		if (!existsRuleFromTargetToActual) {
			// we have to add a rule
			Rule newRule = null;

			if (rulesForPhoneme == null) {
				// there are no rules for this phoneme
				// make the rule global
				newRule = new Rule(targetPhoneme, actualPhoneme, true);
			} else {
				// there are rules for this phoneme
				// make a rule just for this phonetic environment
				// (but globally after/before)
				newRule = new Rule(targetPhoneme, actualPhoneme, false);

				// construct the phonetic environment
				PhoneticEnvironment env = new PhoneticEnvironment(false);
				// add to the environment
				env = modifyProperties(env, wordPosition, syllablePosition,
						vowelPosition, previousPhoneme, nextPhoneme, 1);
				env.makeComesBeforeAndAfterGlobal();
				newRule.setEnvironment(env);

			}

			Set<Rule> set = phonemeToRules.get(targetPhoneme);
			if (set == null) {
				set = new HashSet<Rule>();
			}
			set.add(newRule);
			phonemeToRules.put(targetPhoneme, set);

		}

	}

	private void updateRulesIfTransformToSelf(PHONEME targetPhoneme, Set<Rule> rulesForPhoneme,
			POSITION wordPosition, POSITION syllablePosition, POSITION vowelPosition,
			PHONEME previousPhoneme, PHONEME nextPhoneme) {


		// If there is not a rule that says targetPhoneme
		// changes to itself, create one with a global
		// phonetic environment

		if (rulesForPhoneme == null) {
			// construct global rule with this transformation 
			// make the rule (and make it global)
			Rule newRule = new Rule(targetPhoneme, targetPhoneme, true);
			// add rule to new set
			Set<Rule> set = new HashSet<Rule>();
			set.add(newRule);
			// put new rule in map
			phonemeToRules.put(targetPhoneme, set);
		} else {
			// rules exist
			// go through every rule for this phoneme
			for (Rule r : rulesForPhoneme) {
				// phonetic environment for the rule
				PhoneticEnvironment ruleEnv = r.getEnvironment();

				// If this rule that says target
				// changes to a phoneme other than itself
				if (!r.getOriginalFeatures().
						equals(r.getTransformsToFeatures())) {

					// this rule says to transform to a dif phoneme
					// delete the the properties of this environment
					// from the rule
					ruleEnv = modifyProperties(ruleEnv, wordPosition,
							syllablePosition, vowelPosition, 
							previousPhoneme, nextPhoneme, -1);
					r.setEnvironment(ruleEnv);

				} else {
					// this rule says to transform the target
					// phoneme into itself

					// *the target did transform into itself
					// but we must update the rule to include
					// this environment if it doesn't*

					// if the rule doesn't include
					// the current phonetic environment,
					// add it
					ruleEnv = modifyProperties(ruleEnv, wordPosition,
							syllablePosition, vowelPosition, previousPhoneme,
							nextPhoneme, 1);
					r.setEnvironment(ruleEnv);
				}

			}
		}
	}


	/**
	 * 
	 * @param ruleEnv
	 * @param wordPosition
	 * @param syllablePosition
	 * @param vowelPosition
	 * @param previousPhoneme
	 * @param nextPhoneme
	 * @param addRemove: -1 = remove, 1 = add
	 * @return
	 */
	private PhoneticEnvironment modifyProperties(PhoneticEnvironment ruleEnv,
			POSITION wordPosition, POSITION syllablePosition,
			POSITION vowelPosition, PHONEME previousPhoneme, PHONEME nextPhoneme,
			int addRemove) {

		boolean add = (addRemove == 1);
		boolean remove = (addRemove == -1);
		if (!add && !remove) {
			throw new IllegalArgumentException("-1 to remove, 1 to add");
		}


		if (remove) {
			// if rule env contains this word placement
			ruleEnv.removeWordPlacement(wordPosition);
		} else {
			ruleEnv.addWordPlacement(wordPosition);
		}

		if (remove) {
			// if rule env contains this syllable placement
			ruleEnv.removeSyllablePlacement(syllablePosition);
		} else {
			ruleEnv.addSyllablePlacement(syllablePosition);
		}

		if (remove) {
			// if rule env contains this vowel placement
			ruleEnv.removeVowelPlacement(vowelPosition);
		} else {
			ruleEnv.addVowelPlacement(vowelPosition);
		}

		// if rule env contains instructions to transform
		// after certain features, remove them
		// UNLESS the previous phoneme is a vowel
		if (previousPhoneme != null && !previousPhoneme.getGroup().equals(GROUP.VOWEL)) {
			if (remove) {
				ruleEnv.removeComesAfterPhoneme(previousPhoneme);
			} else {
				ruleEnv.addComesAfter(previousPhoneme);
			}
		}

		// if rule env contains instructions to transform
		// before certain features, remove them
		// UNLESS the next phoneme is a vowel
		if (nextPhoneme != null && !nextPhoneme.getGroup().equals(GROUP.VOWEL)) {
			if (remove) {
				ruleEnv.removeComesBeforePhoneme(nextPhoneme);
			} else {
				ruleEnv.addComesBefore(nextPhoneme);
			}
		}

		return ruleEnv;

	}
	
	/**
	 * Generalizes rules from the specific rules formed from the
	 * training set
	 */
	public void generalizeRules() {
		// only look at NON SELF TRANSFORMING GLOBAL RULES
		for (Set<Rule> rs : phonemeToRules.values()) {
			for (Rule r : rs) {
				if (r.isGlobal() && r.getTargetPhoneme().equals(r.getActualPhoneme())) {
					// skip this rule because it is global and transforms to 
					// itself
					continue;
				}
				
				// this rule either transforms to itself SOMETIMES, or
				// transforms to a phoneme other than itself
				
			}
		}
	}

	/**
	 * Guess the pronunciation given a target
	 * @param target
	 * @return set of possibilities for how the child might pronounce the target
	 */
	public Set<String> guessPronunciation(String targetStr) {
		if (targetStr == null) {
			throw new IllegalArgumentException("Target cannot be null");
		}

		Set<String> possibilities = new HashSet<String>();

		// convert target to phoneme sequence
		PhonemeSequence[] target = 
				convertStringToPhonemeSequence(targetStr);
		possibilities.add(target.toString());


		return possibilities;
	}

	public Set<Rule> getRules() {
		Set<Rule> rules = new HashSet<Rule>();
		for (Set<Rule> rs : phonemeToRules.values()) {
			rules.addAll(rs);
		}
		return rules;
	}

	/**
	 * Convert string to phoneme sequence
	 * @param str: a string of phonemes, separated by "-" 's for phonemes
	 * and " " for syllables
	 * @return the phoneme sequence separated by syllables
	 * ie: "P-AR K-ER" -> {P-AR, K-ER} (as phonemes)
	 */
	public static PhonemeSequence[] convertStringToPhonemeSequence(String str) {
		if (str == null) {
			throw new IllegalArgumentException("String cannot be null.");
		}

		// split by syllable
		String[] syllables = str.split(" ");

		PhonemeSequence[] phonemes = new PhonemeSequence[syllables.length];

		for (int i = 0; i < syllables.length; i++) {
			String syllable = syllables[i];
			// split by - to get each phoneme
			String[] stringArray = syllable.split("-");
			PhonemeSequence phonemesSeq = new PhonemeSequence();
			// loop through phonemes in string
			for (int j = 0; j < stringArray.length; j++) {
				// turn this phoneme string into a PHONEME
				PHONEME p = null;
				// make sure phoneme exists
				try {
					p = PHONEME.valueOf(stringArray[j]);
				} catch (Exception e) {
					throw new IllegalArgumentException(stringArray[j] + 
							" is not a valid phoneme (in " + syllable + ")");
				}
				// phoneme exists. add to sequence
				phonemesSeq.add(p);
			}

			phonemes[i] = phonemesSeq;

		}

		return phonemes;
	}

	public String getMapString() {
		StringBuilder sb = new StringBuilder();
		for (Entry<PhonemeSequence[], PhonemeSequence[]> 
		e : targetToPronunciation.entrySet()) {
			boolean first = true;
			for (PhonemeSequence ps : e.getKey()) {
				if (!first) {
					sb.append(", ");
				}
				first = false;
				sb.append(ps.toString());
			}
			sb.append(": ");
			first = true;
			for (PhonemeSequence ps : e.getValue()) {
				if (!first) {
					sb.append(", ");
				}
				first = false;
				sb.append(ps.toString());
			}
			sb.append("\n");
		}
		return sb.toString();
	}

}
