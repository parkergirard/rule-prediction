package analysis;

import helpers.Helpers;

import java.util.List;
import java.util.Set;
import java.util.Map.Entry;

import enums.CONSONANT_POSITION;
import enums.GROUP;
import enums.PHONEME;
import enums.POSITION;
import enums.VOWEL_POSITION;

public class PronunciationGuesser {

	private Set<GeneralizedRule> rules;

	/**
	 * Given a set of general rules, construct a guesser
	 * @param rules
	 */
	public PronunciationGuesser(Set<GeneralizedRule> rules) {
		this.rules = rules;
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
	public PhonemeSequence[] guessPronunciationOfTargetWord(String word) {
		
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
				if (!targetPhoneme.getGroup().equals(GROUP.VOWEL)) {
					guessSyllable.add(targetPhoneme);
				} else {
					// construct this environment
					PhoneticEnvironment e = new PhoneticEnvironment(false);
					e.addWordPlacement(wordPosition);
					e.addSyllablePlacement(syllablePosition);
					e.addVowelPlacement(vowelPosition);
					e.addComesAfterPhoneme(previousPhoneme);
					e.addComesBeforePhoneme(previousPhoneme);
					// get rule that can apply for this phoneme/environment
					GeneralizedRule r = getRuleForPhonemeAndEnvironment(targetPhoneme, e);
					if (r == null) {
						// no rule was found. do not apply transformation
						guessSyllable.add(targetPhoneme);
					} else {
						// apply the rule to the phoneme, and transform it
					}
				}

				// move to next phoneme
				j++;
				previousPhoneme = targetPhoneme;
			}
		}

		return guessWord;
	}
	
	/**
	 * Helper to get a rule applicable to the given phoneme and environment
	 * @param p: the phoneme
	 * @param e: the environment
	 * @return: the rule that applies, or null if none do
	 */
	private GeneralizedRule 
		getRuleForPhonemeAndEnvironment(PHONEME p, PhoneticEnvironment e) {
		
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

}
