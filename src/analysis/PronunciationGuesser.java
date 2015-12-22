package analysis;

import helpers.Helpers;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import enums.*;

public class PronunciationGuesser {

	private Collection<GeneralizedRule> rules;
	private Map<FeatureProperties, PHONEME> featuresToPhoneme;
	Map<PHONEME, Set<SpecificRule>> phonemeToSpecificRules;

	/**
	 * Given a set of general rules, construct a guesser
	 * @param collection
	 */
	public PronunciationGuesser(Collection<GeneralizedRule> collection, 
			Map<PHONEME, Set<SpecificRule>> phonemeToSpecificRules) {
		this.rules = collection;
		this.phonemeToSpecificRules = phonemeToSpecificRules;
		// initialize features to phoneme map
		featuresToPhoneme = new HashMap<FeatureProperties, PHONEME>();
		for (PHONEME p : PHONEME.values()) {
			// don't add vowels
			if (p.getGroup().equals(GROUP.VOWEL)) {
				continue;
			}
			// add to map
			featuresToPhoneme.put(
					new FeatureProperties(p.getPlace(), 
							p.getManner(), p.getVoice()), p);
		}
	}

	/**
	 * Guess how the child with the given generalized rules, will pronounce
	 * a word.
	 * @param word: the target word the child will try to say. the word
	 * should be separated by spaces for a new syllable, and dashes for a new phoneme
	 * ie, Parker would be: P-A-R K-E-R
	 * @return the guess, where each element in the 
	 * array is a syllable with phonemes
	 */
	public String guessPronunciationOfTargetWord(String word) {

		PhonemeSequence[] targetSyllables = 
				Helpers.convertStringToPhonemeSequence(word);

		PhonemeSequence[] guessWord = new PhonemeSequence[targetSyllables.length];

		// previous phoneme will be compared to later
		PHONEME previousPhoneme = null;

		// loop through all syllables
		POSITION wordPosition = CONSONANT_POSITION.BEGINNING;
		for (int i = 0; i < targetSyllables.length; i++) {

			List<PHONEME> targetPhonemeSeqAtSyllable = 
					targetSyllables[i].getSequence();

			// the guess phoneme sequence for this syllable
			PhonemeSequence guessSyllable = new PhonemeSequence();

			POSITION syllablePosition = CONSONANT_POSITION.BEGINNING;
			// loop through all phonemes in the syllable
			int j = 0;
			for (PHONEME targetPhoneme : targetPhonemeSeqAtSyllable) {
				// ignore if the target is a vowel
				// update to the position that we are in the syllable
				if (j > 0) {
					wordPosition = CONSONANT_POSITION.MIDDLE;
					syllablePosition = CONSONANT_POSITION.MIDDLE;
				}
				if (j == targetPhonemeSeqAtSyllable.size() - 1) {
					syllablePosition = CONSONANT_POSITION.END;
					if (i == targetSyllables.length - 1) {
						wordPosition = CONSONANT_POSITION.END;
					}
				}

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
					nextPhoneme = targetPhonemeSeqAtSyllable.get(j+1);
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


				// If input phoneme is a vowel, return input phoneme
				if (targetPhoneme.getGroup().equals(GROUP.VOWEL)) {
					guessSyllable.add(targetPhoneme);
				} else {
					// construct this environment
					PhoneticEnvironment e = new PhoneticEnvironment(false);
					e.addWordPlacement(wordPosition);
					e.addSyllablePlacement(syllablePosition);
					e.addVowelPlacement(vowelPosition);
					if (previousPhoneme != null && !previousPhoneme.isVowel()) {
						e.addComesAfterPhoneme(previousPhoneme);
					}
					if (nextPhoneme != null && !nextPhoneme.isVowel()) {
						e.addComesBeforePhoneme(nextPhoneme);
					}


					// if there is a specific rule for this phoneme in this
					// environment, follow the specific rule.

					Set<SpecificRule> specificRules = phonemeToSpecificRules.get(targetPhoneme);
					SpecificRule specificR = getSpecificRuleForPhonemeAndEnvironment(targetPhoneme, e);
					if (specificR != null) {
						// specific rule was found. add the transformation
						guessSyllable.add(specificR.getActualPhoneme());
					} else {
						// no specific rule, so get general rule that can 
						// apply for this phoneme/environment

						GeneralizedRule r = getGeneralizedRuleForPhonemeAndEnvironment(targetPhoneme, e);
						if (r == null) {
							// no rule was found. do not apply transformation
							guessSyllable.add(targetPhoneme);
						} else {
							// a rule for this phoneme/environment exists.
							// apply the rule to the phoneme

							PLACE transformsToPlace = null;
							MANNER transformsToManner = null;
							VOICE transformsToVoice = null;

							Set<FEATURE_TYPE> remainsSame = 
									r.getFeatureTypesThatRemainSame();

							FeatureProperties outputProps = r.getOutputPhonemeFeatures();

							// if place remains same, dont change it.
							// otherwise, transform it
							if (remainsSame.contains(FEATURE_TYPE.PLACE)) {
								transformsToPlace = targetPhoneme.getPlace();
							} else {
								transformsToPlace = outputProps.getSinglePlace();
							}

							// if manner remains same, dont change it.
							// otherwise, transform it
							if (remainsSame.contains(FEATURE_TYPE.MANNER)) {
								transformsToManner = targetPhoneme.getManner();
							} else {
								transformsToManner = outputProps.getSingleManner();
							}

							// if voice remains same, dont change it.
							// otherwise, transform it
							if (remainsSame.contains(FEATURE_TYPE.VOICE)) {
								transformsToVoice = targetPhoneme.getVoice();
							} else {
								transformsToVoice = outputProps.getSingleVoice();
							}

							FeatureProperties newPhonemeFeatures = new 
									FeatureProperties(transformsToPlace, 
											transformsToManner, transformsToVoice);

							// check if this phoneme exists
							PHONEME transformToPhoneme = featuresToPhoneme
									.get(newPhonemeFeatures);

							if (transformToPhoneme == null) {
								// this phoneme doesn't exist. don't transform
								guessSyllable.add(targetPhoneme);
							} else {
								// this phoneme exists. transform it
								guessSyllable.add(transformToPhoneme);
							}

						}
					}
				}

				// move to next phoneme
				j++;
				previousPhoneme = targetPhoneme;
			}
			guessWord[i] = guessSyllable;
		}

		return convertPhonemeSequenceArrayToString(guessWord);
	}

	private String convertPhonemeSequenceArrayToString(PhonemeSequence[] seqArr) {
		StringBuilder sb = new StringBuilder();
		boolean first = true;
		for (PhonemeSequence seq : seqArr) {
			if (!first) {
				sb.append(" ");
			}
			sb.append(seq.toString());
			first = false;
		}
		return sb.toString();
	}

	/**
	 * Helper to get a rule applicable to the given phoneme and environment
	 * @param p: the phoneme
	 * @param e: the environment
	 * @return: the rule that applies, or null if none do
	 */
	private GeneralizedRule 
	getGeneralizedRuleForPhonemeAndEnvironment(PHONEME p, PhoneticEnvironment e) {

		// loop through all rules
		for (GeneralizedRule r : rules) {

			// if this rule applies to the given phoneme and given environment
			if (r.appliesToPhoneme(p) && r.appliesToEnvironment(e, true)) {
				return r;
			}

		}

		// no rule applied, return null
		return null;
	}

	/**
	 * Helper to get a rule applicable to the given phoneme and environment
	 * @param p: the phoneme
	 * @param e: the environment
	 * @return: the rule that applies, or null if none do
	 */
	private SpecificRule 
	getSpecificRuleForPhonemeAndEnvironment(PHONEME p, PhoneticEnvironment e) {

		Set<SpecificRule> specificRules = phonemeToSpecificRules.get(p);
		if (specificRules == null) {
			return null;
		}
		
		// loop through all rules
		for (SpecificRule r : specificRules) {

			// if this rule applies to the given phoneme and given environment
			if (r.appliesToEnvironment(e, true)) {
				return r;
			}

		}

		// no rule applied, return null
		return null;
	}

}
