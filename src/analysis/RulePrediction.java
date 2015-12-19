package analysis;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
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
	
	// maps phonetic environment to a set of rule
	public Map<PhoneticEnvironment, Set<Rule>> envToRules;
		
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
		
		envToRules = new HashMap<PhoneticEnvironment, Set<Rule>>();
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

					System.out.println("*******" + targetPhoneme.name() + 
							" compared to " + actualPhoneme.name() + "*******");
					
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

					// goal: look at any rule that exists with the same
					// from features, and contains this phonetic environment
					
					// construct global rule with this transformation if
					// the child transformed the phoneme
					Rule globalRule = new Rule(true);
					globalRule.setOriginalFeatures(new FeatureProperties(
							targetPhoneme.getPlace(),
							targetPhoneme.getManner(), 
							targetPhoneme.getVoice()));
					globalRule.setTransformsToFeatures(new FeatureProperties(
							actualPhoneme.getPlace(),
							actualPhoneme.getManner(), 
							actualPhoneme.getVoice()));
					
					// CONSTRUCT PHONETIC ENVIRONMENT

					PhoneticEnvironment env = new PhoneticEnvironment(false);
					// comes after
					MANNER comesAfterManner = null;
					PLACE comesAfterPlace = null;
					VOICE comesAfterVoice = null;
					if (previousPhoneme != null) {
						comesAfterPlace = previousPhoneme.getPlace();
						comesAfterManner = previousPhoneme.getManner();
						comesAfterVoice = previousPhoneme.getVoice();
						env.addComesAfterFeatures(comesAfterPlace,
							comesAfterManner, 
							comesAfterVoice);
					}
					// comes before
					MANNER comesBeforeManner = null;
					PLACE comesBeforePlace = null;
					VOICE comesBeforeVoice = null;
					if (nextPhoneme != null) {
						comesBeforePlace = nextPhoneme.getPlace();
						comesBeforeManner = nextPhoneme.getManner();
						comesBeforeVoice = nextPhoneme.getVoice();
						env.addComesBeforeFeatures(comesBeforePlace,
							comesBeforeManner, 
							comesBeforeVoice);
					}
					
					// position relative to word/syllable/vowel
					env.addWordPlacement(wordPosition);
					env.addSyllablePlacement(syllablePosition);
					if (vowelPosition != null) {
						env.addVowelPlacement(vowelPosition);
					}
					
					
//					System.out.println("TEST ENV");
//					System.out.println(env);
					
					if (envToRules.containsKey(env)) {
						System.out.println(
								"*****RULE EXISTS WITHIN THIS ENV:****");
							int count = 1;
							Set<Rule> currentRules = envToRules.get(env);
							Iterator<Rule> iter = currentRules.iterator();
							while (iter.hasNext()) {
								Rule r = iter.next();
								System.out.println("***A RULE IN THIS IS: *** "
										+ count++);
								System.out.println(r);
								
								// check if these rules contradict each other
								
								// contradiction: if the original features of
								// the rules are the same, but the transformation 
								// is different
								
								if (r.getOriginalFeatures().equals(
										globalRule.getOriginalFeatures()) &&
										!r.getTransformsToFeatures().equals(
												globalRule.
												getTransformsToFeatures())) {
									System.out.println("****CONTRADICTION**** ");
									// original features of rules are same
									// but transforms to features are not.
									
									// therefore, this rule is not global.
									// remove the current phonetic environment
									// from the rule

									currentRules.remove(r);

									if (!targetPhoneme.equals(actualPhoneme)) {
										globalRule.remove(wordPosition, 
											syllablePosition, vowelPosition, 
											comesBeforePlace, comesAfterPlace,
											comesBeforeManner, comesAfterManner,
											comesBeforeVoice, comesAfterVoice);
									}
								}

							// add a rule if the child made one / 
							// we deleted one that we have to update
							Set<Rule> updatedRules = 
									new HashSet<Rule>(currentRules);
							if (!targetPhoneme.equals(actualPhoneme)) {
								updatedRules.add(globalRule);
							}
							envToRules.put(env, updatedRules);
						}
						
					} else if (!targetPhoneme.equals(actualPhoneme)){
						// child made a rule in a new env that we must add
						Set<Rule> set = new HashSet<Rule>();
						set.add(globalRule);
						envToRules.put(env, set);
					}
					
					System.out.println("\n\n\n\n ******CURRENT RULES ****\n");
					for (Set<Rule> rs : envToRules.values()) {
						for (Rule r : rs) {
							System.out.println("\n***RULE****\n");
							System.out.println(r);
						}
					}
					
					j++;
					previousPhoneme = targetPhoneme;
				}
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
