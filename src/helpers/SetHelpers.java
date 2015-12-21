package helpers;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import enums.GROUP;
import enums.PHONEME;

public class SetHelpers {

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
