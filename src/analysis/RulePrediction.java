package analysis;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import phonemes.*;

public class RulePrediction {
	
	// maps targets to how the child pronounced them
	private Map<PhonemeSequence, PhonemeSequence> targetToPronunciation;
	
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
				new HashMap<PhonemeSequence, PhonemeSequence>();
		
		// convert each string to phoneme sequence
		for (Entry<String, String> e : map.entrySet()) {
			// function will throw error if invalid phoneme
			PhonemeSequence target = convertStringToPhonemeSequence(e.getKey());
			PhonemeSequence val = convertStringToPhonemeSequence(e.getValue());
			// valid phonemes, add to map
			targetToPronunciation.put(target, val);
		}
		
		analyzeTrainingSet();
	}
	
	/**
	 * Analyzes the training set provided, to detect rules
	 */
	private void analyzeTrainingSet() {
		
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
		PhonemeSequence target = convertStringToPhonemeSequence(targetStr);
		possibilities.add(target.toString());
		
		
		return possibilities;
	}
	
	/**
	 * Convert string to phoneme sequence
	 * @param s: a string of phonemes, separated by "-" 's
	 * @return the phoneme sequence
	 */
	public static PhonemeSequence convertStringToPhonemeSequence(String s) {
		if (s == null) {
			throw new IllegalArgumentException("String cannot be null.");
		}
		
		// split by dashes
		String[] stringArray = s.split("-");
		
		PhonemeSequence phonemes = new PhonemeSequence();
		
		// loop through phonemes in string
		for (int i = 0; i < stringArray.length; i++) {
			// turn this phoneme string into a PHONEME
			PHONEME p = null;
			// make sure phoneme exists
			try {
				p = PHONEME.valueOf(stringArray[i]);
			} catch (Exception e) {
				throw new IllegalArgumentException(stringArray[i] + 
						" is not a valid phoneme (in " + s + ")");
			}
			// phoneme exists. add to sequence
			phonemes.add(p);
		}
		
		return phonemes;
	}
}
