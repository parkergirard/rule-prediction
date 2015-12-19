package analysis;

import java.util.HashSet;
import java.util.Set;

import enums.*;

public class PhoneticEnvironment {
	Set<POSITION> wordPlacement;
	Set<POSITION> syllablePlacement;
	Set<POSITION> vowelPlacement;

	// sets of descriptions of sounds before/after the rule occurs
	FeatureProperties comesAfterFeatures;
	FeatureProperties comesBeforeFeatures;
	/**
	 * 
	 * @param wordPlacement: Is it at the beginning or end of a word?
	 * @param syllablePlacement: Is it at the beginning or end of a syllable?
	 * @param vowelPlacement: Is it before/after/surrounded by a vowel??
	 * @param afterProperties: Descriptions of sounds after the rule occurs
	 * @param beforeProperties: Descriptions of sounds before the rule occurs
	 */
	PhoneticEnvironment(Set<POSITION> wordPlacement, 
			Set<POSITION> syllablePlacement,
			Set<POSITION> vowelPlacement, 
			FeatureProperties afterProperties,
			FeatureProperties beforeProperties) {
		
		this.wordPlacement = wordPlacement;
		this.syllablePlacement = syllablePlacement;
		this.vowelPlacement = vowelPlacement;

		this.comesAfterFeatures = afterProperties;
		this.comesBeforeFeatures = beforeProperties;
		
	}
	
	PhoneticEnvironment(boolean global) {
		wordPlacement = new HashSet<POSITION>();
		syllablePlacement = new HashSet<POSITION>();
		vowelPlacement = new HashSet<POSITION>();
		
		comesAfterFeatures = new FeatureProperties();
		comesBeforeFeatures = new FeatureProperties();
		if (global) {
			// comes at every placement
			POSITION[] vals = CONSONANT_POSITION.values();
			for (int i = 0; i < vals.length; i++) {
				wordPlacement.add(vals[i]);
				syllablePlacement.add(vals[i]);
			}
			vals = VOWEL_POSITION.values();
			for (int i = 0; i < vals.length; i++) {
				vowelPlacement.add(vals[i]);
			}
			// comes after and before any feature
			comesAfterFeatures.makePropertiesGlobal();
			comesBeforeFeatures.makePropertiesGlobal();
		}
	}
	
	// OPTIONS TO UPDATE AND GET INFO ABOUT THE RULE

	public void addWordPlacement(POSITION p) {
		wordPlacement.add(p);
	}

	public void removeWordPlacement(POSITION p) {
		wordPlacement.remove(p);
	}

	public Set<POSITION> getWordPlacement() {
		return wordPlacement;
	}

	public void addSyllablePlacement(POSITION p) {
		syllablePlacement.add(p);
	}

	public Set<POSITION> getSyllablePlacement() {
		return syllablePlacement;
	}

	public void removeSyllablePlacement(POSITION p) {
		syllablePlacement.remove(p);
	}

	public void addVowelPlacement(POSITION p) {
		vowelPlacement.add(p);
	}

	public Set<POSITION> getVowelPlacement() {
		return vowelPlacement;
	}

	public void removeVowelPlacement(POSITION p) {
		vowelPlacement.remove(p);
	}

	/**
	 * If a param is null, it is ignored
	 * @param p
	 * @param m
	 * @param v
	 */
	public void addComesAfterFeatures(PLACE p, MANNER m, VOICE v) {
		comesAfterFeatures.add(p, m, v);
	}

	public FeatureProperties getComesAfterFeatures() {
		return comesAfterFeatures;
	}

	/**
	 * If a param is null, it is ignored
	 * @param p
	 * @param m
	 * @param v
	 */
	public void addComesBeforeFeatures(PLACE p, MANNER m, VOICE v) {
		comesBeforeFeatures.add(p, m, v);
	}

	public FeatureProperties getComesBeforeFeatures() {
		return comesBeforeFeatures;
	}


	public void setComesAfterFeatures(PLACE place, MANNER manner, VOICE voice) {
		comesAfterFeatures = new FeatureProperties(place, manner, voice);
	}
	public void setComesBeforeFeatures(PLACE place, MANNER manner, VOICE voice) {
		comesBeforeFeatures = new FeatureProperties(place, manner, voice);
	}
	
	@Override
	/**
	 * PE A = PE B if B's details contains all of A's details
	 */
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        PhoneticEnvironment r = (PhoneticEnvironment) o;
        return  r.wordPlacement.containsAll(wordPlacement) &&
        		r.syllablePlacement.containsAll(syllablePlacement) &&
        		r.vowelPlacement.containsAll(vowelPlacement) &&
                r.comesAfterFeatures.equals(comesAfterFeatures) &&
                r.comesBeforeFeatures.equals(comesBeforeFeatures);
    }
	
	@Override
    public int hashCode() {
        return wordPlacement.hashCode() + syllablePlacement.hashCode() + 
        		vowelPlacement.hashCode() + comesAfterFeatures.hashCode() +
        		comesBeforeFeatures.hashCode();
    }
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();

		sb.append("Word Placements: ");
		sb.append(getWordPlacement());
		sb.append("\nSyllable Placements: ");
		sb.append(getSyllablePlacement());
		sb.append("\nVowel Placements: ");
		sb.append(getVowelPlacement());

		sb.append("\nComes After Features\n");
		sb.append(getComesAfterFeatures().toString());
		
		sb.append("\nComes Before Features\n");
		sb.append(getComesBeforeFeatures().toString());
		
		return sb.toString();
	}

	public void removeComesAfter(PLACE p,MANNER m, VOICE v) {
		if (p != null) {
			comesAfterFeatures.removePlace(p);
		}
		if (m != null) {
			comesAfterFeatures.removeManner(m);
		}
		if (v != null) {
			comesAfterFeatures.removeVoicing(v);
		}
	}

	public void removeComesBefore(PLACE p,MANNER m, VOICE v) {
		if (p != null) {
			comesBeforeFeatures.removePlace(p);
		}
		if (m != null) {
			comesBeforeFeatures.removeManner(m);
		}
		if (v != null) {
			comesBeforeFeatures.removeVoicing(v);
		}
	}
}
