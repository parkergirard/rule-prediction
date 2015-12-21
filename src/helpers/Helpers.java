package helpers;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import analysis.PhonemeSequence;
import enums.GROUP;
import enums.PHONEME;

public class Helpers {


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
	
	/**
	 * Helper function which when given a set of sets,
	 * returns one maximal set, such that each element
	 * in the set is contained in all of the input sets
	 * @param sets a set of sets containing elements
	 * @return the intersecting set
	 */
	public static <T> Set<T> getIntersectionOfSets(Collection<T> set1, Collection<T> set2) {
		Set<T> intersection = new HashSet<T>();
		
		// loop through the first set and add all of the same elements
		// contained in the second set to the intersection
		for (T el : set1) {
			if (!set2.contains(el)) {
				// second set didn't contain this element, so it's
				// not in the intersection
				break;
			} else {
				// element is in both sets, and therefore the intersection
				intersection.add(el);
			}
		}
		
		return intersection;
	}
	
	public static <T> Set<T> getSet1MinusSet2(Collection<T> set1, Collection<T> set2) {
		Set<T> set1MinusSet2 = new HashSet<T>();
		
		// add set 1
		for (T el : set1) {
			if (el.getClass().equals(PHONEME.class)) {
				PHONEME p = (PHONEME) el;
				if (!p.getGroup().equals(GROUP.VOWEL)) {
					set1MinusSet2.add(el);
				}
			}
		}
		
		// remove all of set 2
		set1MinusSet2.removeAll(set2);
		
		return set1MinusSet2;
	}

	public static <T> Set<T> getSet1MinusSet2(T[] a,
			Set<T> s2) {
		Set<T> aAsSet = new HashSet<T>();
		for (T el : a) {
			aAsSet.add(el);
		}
		return getSet1MinusSet2(aAsSet, s2);
	}
}
