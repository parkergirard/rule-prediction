package analysis;

import java.util.ArrayList;
import java.util.List;

import phonemes.PHONEME;

/**
 * Maintains an ordered sequence of phonemes
 */

public class PhonemeSequence {
	List<PHONEME> phonemes;
	
	/**
	 * Init with empty list
	 */
	public PhonemeSequence() {
		phonemes = new ArrayList<PHONEME>();
	}
	
	/**
	 * Init with given list of phonemes
	 * @param phonemes
	 */
	public PhonemeSequence(List<PHONEME> phonemes) {
		this.phonemes = phonemes;
	}
	
	/**
	 * Add phoneme to end of sequence
	 * @param p: phoneme to add
	 */
	public void add(PHONEME p) {
		phonemes.add(p);
	}
	
	public List<PHONEME> getSequence() {
		return phonemes;
	}
	
	@Override
	public String toString() {
		String s = "";
		
		boolean start = true;
		for (PHONEME p : phonemes) {
			if (!start) {
				s += "-";
			}
			s += p.name();
			start = false;
		}
		
		return s;
	}
	
}
