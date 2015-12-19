package tests;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import analysis.*;
import static org.junit.Assert.*;

import org.junit.Test;

public class RulePredictionTest {

	@Test(expected=IllegalArgumentException.class)
	public void testInvalidPhoneme() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("D-EY", "D-EYx");
		new RulePrediction(map);
	}
	
	@Test
	public void guessProb2() {
		Map<String, String> map = new HashMap<String, String>();
//		map.put("K-AE-T", "T-AE-T");
//		map.put("P-AA-T", "P-AA-T");
//		map.put("T-AA-P", "T-AA-P");
//		map.put("B-AE-K", "B-AE-T");
//		map.put("D-EY", "D-EY");
		map.put("P-AA_R K-ER P-Y-ER", "P-AA_R T-ER P-Y-ER");
		
		RulePrediction rp = new RulePrediction(map);
		Set<String> guesses = rp.guessPronunciation("G-EY-M");

		Set<String> expectedGuesses = new HashSet<String>();
		expectedGuesses.add("D-EY-M");
		
		assertEquals(expectedGuesses, guesses);
		
	}

}
