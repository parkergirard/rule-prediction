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
	Set<PHONEME> comesAfterPhonemes;
	FeatureProperties comesBeforeFeatures;
	Set<PHONEME> comesBeforePhonemes;
	
	Set<PHONEME> doesntComeAfterPhonemes;
	Set<PHONEME> doesntComeBeforePhonemes;
	
	public PhoneticEnvironment(boolean global) {
		wordPlacement = new HashSet<POSITION>();
		syllablePlacement = new HashSet<POSITION>();
		vowelPlacement = new HashSet<POSITION>();
		
		comesAfterFeatures = new FeatureProperties();
		comesBeforeFeatures = new FeatureProperties();

		comesAfterPhonemes = new HashSet<PHONEME>();
		comesBeforePhonemes = new HashSet<PHONEME>();

		doesntComeAfterPhonemes = new HashSet<PHONEME>();
		doesntComeBeforePhonemes = new HashSet<PHONEME>();
		
		if (global) {
			
			makeComesBeforeAndAfterGlobal();
			
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

	public void setWordPlacement(POSITION p) {
		wordPlacement = new HashSet<POSITION>();
		wordPlacement.add(p);
	}
	
	public void setSyllablePlacement(POSITION p) {
		syllablePlacement = new HashSet<POSITION>();
		syllablePlacement.add(p);
	}
	
	public void setVowelPlacement(POSITION p) {
		vowelPlacement = new HashSet<POSITION>();
		vowelPlacement.add(p);
	}
	
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

	public void addComesAfter(PHONEME p) {
		comesAfterFeatures.add(p.getPlace(), p.getManner(), p.getVoice());
		comesAfterPhonemes.add(p);
	}

	public FeatureProperties getComesAfterFeatures() {
		return comesAfterFeatures;
	}

	
	public void addComesBefore(PHONEME p) {
		comesBeforeFeatures.add(p.getPlace(), p.getManner(), p.getVoice());
		comesBeforePhonemes.add(p);
	}

	public FeatureProperties getComesBeforeFeatures() {
		return comesBeforeFeatures;
	}


	public void removeComesAfterPhoneme(PHONEME p) {
		removeComesAfterFeatures(p.getPlace(), p.getManner(), p.getVoice());
		comesAfterPhonemes.remove(p);
		doesntComeAfterPhonemes.add(p);
	}
	
	private void removeComesAfterFeatures(PLACE p,MANNER m, VOICE v) {
		comesAfterFeatures.removePlace(p);
		comesAfterFeatures.removeManner(m);
		comesAfterFeatures.removeVoicing(v);
	}

	public void removeComesBeforePhoneme(PHONEME p) {
		removeComesBeforeFeatures(p.getPlace(), p.getManner(), p.getVoice());
		comesBeforePhonemes.remove(p);
		doesntComeBeforePhonemes.add(p);
	}

	
	private void removeComesBeforeFeatures(PLACE p,MANNER m, VOICE v) {
		comesBeforeFeatures.removePlace(p);
		comesBeforeFeatures.removeManner(m);
		comesBeforeFeatures.removeVoicing(v);
	}

	public void makeComesAfterGlobal() {
		// can come after every phoneme
		// (ignore vowels)
		for (PHONEME p : PHONEME.values()) {
			if (p.getGroup().equals(GROUP.VOWEL)) {
				continue;
			}
			comesAfterPhonemes.add(p);
		}
		doesntComeAfterPhonemes.clear();
		comesAfterFeatures.makePropertiesGlobal();
	}
	public void makeComesBeforeGlobal() {
		// can come before every phoneme
		// (ignore vowels)
		for (PHONEME p : PHONEME.values()) {
			if (p.getGroup().equals(GROUP.VOWEL)) {
				continue;
			}
			comesBeforePhonemes.add(p);
		}
		doesntComeBeforePhonemes.clear();
		comesBeforeFeatures.makePropertiesGlobal();
	}
	
	public void makeComesBeforeAndAfterGlobal() {
		// can come after and before every phoneme
		// (ignore vowels)
		for (PHONEME p : PHONEME.values()) {
			if (p.getGroup().equals(GROUP.VOWEL)) {
				continue;
			}
			comesAfterPhonemes.add(p);
			comesBeforePhonemes.add(p);
		}
		doesntComeAfterPhonemes.clear();
		doesntComeBeforePhonemes.clear();
		comesAfterFeatures.makePropertiesGlobal();
		comesBeforeFeatures.makePropertiesGlobal();
	}
	

	/**
	 * Returns whether or not this environment contains every possibility
	 * of placement
	 * @return if it is global or not
	 */
	public boolean isGlobal() {
		
		return wordPlacement.size() == CONSONANT_POSITION.values().length &&
				syllablePlacement.size() == CONSONANT_POSITION.values().length &&
				vowelPlacement.size() == VOWEL_POSITION.values().length &&
				doesntComeAfterPhonemes.size() == 0 && 
				doesntComeBeforePhonemes.size() == 0 ;
	}
	
	@Override
	/**
	 * PE A = PE B if B's details equal all of A's details
	 */
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        PhoneticEnvironment r = (PhoneticEnvironment) o;
        return  r.wordPlacement.equals(wordPlacement) &&
        		r.syllablePlacement.equals(syllablePlacement) &&
        		r.vowelPlacement.equals(vowelPlacement) &&
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

		sb.append("\nComes After PHONEMES\n");
		sb.append(comesAfterPhonemes);

		sb.append("\nDoesn't Come After PHONEMES\n");
		sb.append(doesntComeAfterPhonemes);
		
		sb.append("\nComes Before PHONEMES\n");
		sb.append(comesBeforePhonemes);

		sb.append("\nDoesn't Come Before PHONEMES\n");
		sb.append(doesntComeBeforePhonemes);

//		sb.append("\nComes After Features\n");
//		sb.append(getComesAfterFeatures().toString());
//		
//		sb.append("\nComes Before Features\n");
//		sb.append(getComesBeforeFeatures().toString());
		
		return sb.toString();
	}
}
