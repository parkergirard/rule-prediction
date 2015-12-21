package analysis;
import java.util.Set;
import java.util.HashSet;

import enums.*;

public class GeneralizedRule {
	
	private PhoneticEnvironment e;
	
	private FeatureProperties inputPhonemeFeatures;
	private FeatureProperties outputPhonemeFeatures;
	// set of features that remain the same as the input
	// phoneme features
	private Set<FEATURE> remainsSame;
	
	/**
	 * Construct a generalized rule from the intersections of
	 * features from a set of input phonemes and a
	 * set of environments
	 * @param inputs the input phonemes
	 * @param envs the phonetic environments
	 */
	public GeneralizedRule(Set<PHONEME> inputs, 
			Set<PhoneticEnvironment> envs) {
		
		Set<PLACE> inputPlaces = new HashSet<PLACE>();
		Set<MANNER> inputMannes = new HashSet<MANNER>();
		Set<VOICE> inputVoices = new HashSet<VOICE>();
		// get intersection of the input features
		
	}
	
	/**
	 * Helper function which when given a set of sets,
	 * returns one maximal set, such that each element
	 * in the set is contained in all of the input sets
	 * @param sets a set of sets containing elements
	 * @return the intersecting set
	 */
	public static <T> Set<T> getIntersection(Set<Set<T>> sets) {
		Set<T> intersection = new HashSet<T>();
		
		// get an aribtrary set
		Set<T> firstSet = null;
		for (Set<T> set : sets) {
			firstSet = set;
			break;
		}

		// loop through the set and add all of the same elements
		// contained in all other sets to the intersection
		for (T el : firstSet) {
			boolean foundInAllOtherSets = true;
			// make sure that this element is contained in all other sets
			for (Set<T> set : sets) {
				// move on if its the same set as the arbitrary one
				if (firstSet.equals(set)) {
					continue;
				}
				if (!set.contains(el)) {
					// a set didn't contain this element, so it's
					// not in the intersection
					foundInAllOtherSets = false;
					break;
				}
			}
			// if the element was contained in every set,
			// make it an element of the intersection
			if (foundInAllOtherSets) {
				intersection.add(el);
			}
		}
		
		return intersection;
	}
	
}
