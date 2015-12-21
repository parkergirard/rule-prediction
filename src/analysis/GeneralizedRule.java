package analysis;
import java.util.Objects;
import java.util.Set;
import java.util.HashSet;

import enums.*;

public class GeneralizedRule {
	
	private FeatureProperties inputPhonemeFeatures;
	private FeatureProperties outputPhonemeFeatures;
	// set of features types that remain the same as the input
	// phoneme features (place/manner/voice)
	private Set<FEATURE_TYPE> remainsSame;
	
	private PhoneticEnvironment env;
	
	/**
	 * Construct an "empty" generalized rule
	 * @param inputPhoneme
	 */
	public GeneralizedRule(FeatureProperties inputPhonemeFeatures, 
			FeatureProperties outputPhonemeFeatures, 
			Set<FEATURE_TYPE> remainsSame, PhoneticEnvironment env) {
		
		this.inputPhonemeFeatures = inputPhonemeFeatures;
		this.outputPhonemeFeatures = outputPhonemeFeatures;
		this.remainsSame = remainsSame;
		this.env = env;
	}

	public FeatureProperties getInputPhonemeFeatures() {
		return inputPhonemeFeatures;
	}
	
	public FeatureProperties getOutputPhonemeFeatures() {
		return outputPhonemeFeatures;
	}
	
	public Set<FEATURE_TYPE> getFeatureTypesThatRemainSame() {
		return remainsSame;
	}
	
	public void addFeatureTypesThatRemainsSame(FEATURE_TYPE f) {
		remainsSame.add(f);
	}

	public PhoneticEnvironment getPhoneticEnvironment() {
		return env;
	}

	public void setPhoneticEnvironment(PhoneticEnvironment env) {
		this.env = env;
	}

	@Override
	/**
	 * Rule A = Rule B if everything is the same
	 */
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        GeneralizedRule r = (GeneralizedRule) o;
        return inputPhonemeFeatures.equals(r.inputPhonemeFeatures) &&
        		outputPhonemeFeatures.equals(r.outputPhonemeFeatures) &&
        		remainsSame.equals(r.remainsSame) &&
        		env.equals(r.env);
    }

    @Override
    public int hashCode() {
        return inputPhonemeFeatures.hashCode() +
        		outputPhonemeFeatures.hashCode() + Objects.hash(remainsSame)
        		+ env.hashCode();
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
