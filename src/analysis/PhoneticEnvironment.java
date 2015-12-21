package analysis;

import helpers.SetHelpers;

import java.util.HashSet;
import java.util.Set;

import enums.*;

public class PhoneticEnvironment {
	Set<POSITION> wordPlacement;
	Set<POSITION> syllablePlacement;
	Set<POSITION> vowelPlacement;

	// sets of descriptions of sounds before/after the rule occurs
	Set<PHONEME> comesAfterPhonemes;
	Set<PHONEME> comesBeforePhonemes;
	
	Set<PHONEME> doesntComeAfterPhonemes;
	Set<PHONEME> doesntComeBeforePhonemes;

	
	public PhoneticEnvironment(boolean global) {
		wordPlacement = new HashSet<POSITION>();
		syllablePlacement = new HashSet<POSITION>();
		vowelPlacement = new HashSet<POSITION>();

		comesAfterPhonemes = new HashSet<PHONEME>();
		comesBeforePhonemes = new HashSet<PHONEME>();

		doesntComeAfterPhonemes = new HashSet<PHONEME>();
		doesntComeBeforePhonemes = new HashSet<PHONEME>();
		
		if (global) {
			
			// comes after/before any feature
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
		}
	}
	
	// OPTIONS TO UPDATE AND GET INFO ABOUT THE RULE

	public void setWordPlacement(POSITION p) {
		wordPlacement = new HashSet<POSITION>();
		wordPlacement.add(p);
	}
	public void setWordPlacement(Set<POSITION> ps) {
		this.wordPlacement = new HashSet<POSITION>(ps);
	}

	public void setSyllablePlacement(POSITION p) {
		syllablePlacement = new HashSet<POSITION>();
		syllablePlacement.add(p);
	}
	public void setSyllablePlacement(Set<POSITION> ps) {
		this.syllablePlacement = new HashSet<POSITION>(ps);
	}
	
	public void setVowelPlacement(POSITION p) {
		vowelPlacement = new HashSet<POSITION>();
		vowelPlacement.add(p);
	}
	public void setVowelPlacement(Set<POSITION> ps) {
		this.vowelPlacement = new HashSet<POSITION>(ps);
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
		comesAfterPhonemes.add(p);
		doesntComeAfterPhonemes.remove(p);

		assertComesAndDoesntComeAddUp(comesAfterPhonemes, doesntComeAfterPhonemes);
	}


	
	public void addComesBefore(PHONEME p) {
		comesBeforePhonemes.add(p);
		doesntComeBeforePhonemes.remove(p);
		assertComesAndDoesntComeAddUp(comesBeforePhonemes, doesntComeBeforePhonemes);
	}


	public void removeComesAfterPhoneme(PHONEME p) {
		comesAfterPhonemes.remove(p);
		doesntComeAfterPhonemes.add(p);
		assertComesAndDoesntComeAddUp(comesAfterPhonemes, doesntComeAfterPhonemes);
	}

	public void removeComesBeforePhoneme(PHONEME p) {
		comesBeforePhonemes.remove(p);
		doesntComeBeforePhonemes.add(p);
		assertComesAndDoesntComeAddUp(comesBeforePhonemes, doesntComeBeforePhonemes);
	}

	private void assertComesAndDoesntComeAddUp(Set<PHONEME> s1, Set<PHONEME> s2) {
		boolean ok = (s1.size() + s2.size() 
				== (PHONEME.values().length - VOWELS_FOR_REFERENCE.values().length));
		if (!ok) {
			String str = "";
			System.out.println(s1 + " " + s1.size());
			System.out.println(s2 + " " + s2.size());
			if (s1.equals(comesAfterPhonemes) && s2.equals(doesntComeAfterPhonemes)) {
				str += "After/DoesntComeAfter";
			} else if (s1.equals(comesBeforePhonemes) && s2.equals(doesntComeBeforePhonemes)) {
				str += "Before/DoesntComeBefore";
			} else {
				throw new IllegalArgumentException("Invalid sets.");
			}
			throw new RuntimeException("Error in making " + str + " match up");
		}
	}
	
	public void setComesAfterPhonemes(Set<PHONEME> set) {
		this.comesAfterPhonemes = set;
		doesntComeAfterPhonemes =
				SetHelpers.getSet1MinusSet2(PHONEME.values(), comesAfterPhonemes);
		assertComesAndDoesntComeAddUp(comesAfterPhonemes, doesntComeAfterPhonemes);
		
	}
	public void setComesBeforePhonemes(Set<PHONEME> set) {
		this.comesBeforePhonemes = set;
		doesntComeBeforePhonemes =
				SetHelpers.getSet1MinusSet2(PHONEME.values(), comesBeforePhonemes);
		assertComesAndDoesntComeAddUp(comesBeforePhonemes, doesntComeBeforePhonemes);
	}

	public Set<PHONEME> getComesAfterPhonemes() {
		return comesAfterPhonemes;
	}
	public Set<PHONEME> getComesBeforePhonemes() {
		return comesBeforePhonemes;
	}

	public Set<PHONEME> getDoesntComeAfterPhonemes() {
		return doesntComeAfterPhonemes;
	}
	public Set<PHONEME> getDoesntComeBeforePhonemes() {
		return doesntComeBeforePhonemes;
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
	}

	public boolean isWordPlacementGlobal() {
		return wordPlacement.size() == CONSONANT_POSITION.values().length;
	}
	
	public boolean isSyllablePlacementGlobal() {
		return syllablePlacement.size() == CONSONANT_POSITION.values().length;
	}

	public boolean isVowelPlacementGlobal() {
		return vowelPlacement.size() == VOWEL_POSITION.values().length;
	}
	
	public boolean isComesAfterPhonemesGlobal() {
		boolean yes = comesAfterPhonemes.size() == PHONEME.values().length -
				VOWELS_FOR_REFERENCE.values().length;
		if (yes) {
			assert(doesntComeAfterPhonemes.size() == 0);
		}
		return yes;
	}
	
	public boolean isComesBeforePhonemesGlobal() {
		boolean yes = comesBeforePhonemes.size() == PHONEME.values().length -
				VOWELS_FOR_REFERENCE.values().length;
		if (yes) {
			assert(doesntComeBeforePhonemes.size() == 0);
		}
		return yes;
	}

	/**
	 * Returns whether or not this environment contains every possibility
	 * of placement
	 * @return if it is global or not
	 */
	public boolean isGlobal() {
		
		return isWordPlacementGlobal() && isSyllablePlacementGlobal() &&
				isVowelPlacementGlobal() && isComesAfterPhonemesGlobal() &&
				isComesBeforePhonemesGlobal();
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
                r.comesAfterPhonemes.equals(comesAfterPhonemes) &&
                r.comesBeforePhonemes.equals(comesBeforePhonemes) &&
                r.doesntComeAfterPhonemes.equals(doesntComeAfterPhonemes) &&
                r.doesntComeBeforePhonemes.equals(doesntComeBeforePhonemes);
    }
	
	@Override
    public int hashCode() {
        return wordPlacement.hashCode() + syllablePlacement.hashCode() + 
        		vowelPlacement.hashCode() + comesAfterPhonemes.hashCode() +
        		comesBeforePhonemes.hashCode() +
        		doesntComeAfterPhonemes.hashCode() + 
        		doesntComeBeforePhonemes.hashCode();
    }
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();

		if (isGlobal()) {
			sb.append("(Global Environment)");
		}
		
		sb.append("\nWord Placements: ");
		if (isWordPlacementGlobal()) {
			sb.append(" (Global) ");
		}
		sb.append(getWordPlacement());
		
		sb.append("\nSyllable Placements: ");
		if (isSyllablePlacementGlobal()) {
			sb.append(" (Global) ");
		}
		sb.append(getSyllablePlacement());
		
		sb.append("\nVowel Placements: ");
		if (isVowelPlacementGlobal()) {
			sb.append(" (Global) ");
		}
		sb.append(getVowelPlacement());

		sb.append("\nComes After PHONEMES\n");
		if (isComesAfterPhonemesGlobal()) {
			sb.append(" (Global) ");
		}
		sb.append(comesAfterPhonemes);

		sb.append("\nDoesn't Come After PHONEMES\n");
		sb.append(doesntComeAfterPhonemes);
		
		sb.append("\nComes Before PHONEMES\n");
		if (isComesBeforePhonemesGlobal()) {
			sb.append(" (Global) ");
		}
		sb.append(comesBeforePhonemes);

		sb.append("\nDoesn't Come Before PHONEMES\n");
		sb.append(doesntComeBeforePhonemes);
		
		return sb.toString();
	}
}
