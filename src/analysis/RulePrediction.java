package analysis;
import java.util.ArrayList;
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
	
	private Set<Rule> rules;
		
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
		
		rules = new HashSet<Rule>();
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
			POSITION wordPosition = POSITION.BEGINNING;
			for (int i = 0; i < targetSyllables.length; i++) {
				
				List<PHONEME> targetPhonemeSeq = 
						targetSyllables[i].getSequence();
				List<PHONEME> actualPhonemeSeq = 
						actualSyllables[i].getSequence();

				POSITION syllablePosition = POSITION.BEGINNING;
				// loop through all phonemes in the syllable
				int j = 0;
				for (PHONEME targetPhoneme : targetPhonemeSeq) {
					// update to the position that we are in the syllable
					if (j > 0) {
						wordPosition = POSITION.MIDDLE;
						syllablePosition = POSITION.MIDDLE;
					}
					if (j == targetPhonemeSeq.size() - 1) {
						syllablePosition = POSITION.END;
						if (i == targetSyllables.length - 1) {
							wordPosition = POSITION.END;
						}
					}
					
					PHONEME actualPhoneme = actualPhonemeSeq.get(j);

					System.out.println("*******" + targetPhoneme.name() + 
							" compared to " + actualPhoneme.name() + "*******");
//					System.out.println("word " + wordPosition);
//					System.out.println("syl " + syllablePosition);
					
					// compare places/manners/voices
					Rule r = new Rule();
					r.setFromProperties(new Properties(targetPhoneme.getPlace(),
							targetPhoneme.getManner(), 
							targetPhoneme.getVoice()));
					r.setToProperties(new Properties(actualPhoneme.getPlace(),
							actualPhoneme.getManner(), 
							actualPhoneme.getVoice()));

					
					// add phonetic environment detail to the rule
					
					r.addWordPlacement(wordPosition);
					r.addSyllablePlacement(syllablePosition);
					
					// collect information relative to previous
					// and next phoneme
					
					POSITION vowelPosition = null;
					PHONEME nextPhoneme = null;
					
					// get next phoneme
					if (syllablePosition.equals(POSITION.END) && 
							!wordPosition.equals(POSITION.END)) {
						// at end of syllable, but not end of word
						// look at next syllable
						nextPhoneme = targetSyllables[i+1].getSequence().get(0);
					} else if (!wordPosition.equals(POSITION.END)) {
						// not at end of syllable, and not end of word
						// look at next syllable
						nextPhoneme = targetPhonemeSeq.get(j+1);
					}
					
					if (previousPhoneme != null && 
							previousPhoneme.getGroup().equals(GROUP.VOWEL) &&
								nextPhoneme != null && 
								nextPhoneme.getGroup().equals(GROUP.VOWEL)) {
						// this phoneme is in the middle of two vowels
						vowelPosition = POSITION.SURROUNDED_BY;
					} else if (previousPhoneme != null && 
							previousPhoneme.getGroup().equals(GROUP.VOWEL)) {
						// this phoneme comes after (the end of) a vowel
						vowelPosition = POSITION.AFTER;
					} else if (nextPhoneme != null && 
							nextPhoneme.getGroup().equals(GROUP.VOWEL)) {
						// this phoneme comes before (the beginning of) a vowel
						vowelPosition = POSITION.BEFORE;
					}
					
					if (vowelPosition != null) {
						r.addVowelPlacement(vowelPosition);
					}
					
					// set comes after/before properties
					if (previousPhoneme != null) {
						r.addComesAfterProperties(previousPhoneme.getPlace(),
								previousPhoneme.getManner(), 
								previousPhoneme.getVoice());
					}
					if (nextPhoneme != null) {
						r.addComesBeforeProperties(nextPhoneme.getPlace(),
								nextPhoneme.getManner(), 
								nextPhoneme.getVoice());
					}
					
					System.out.println(r);
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
