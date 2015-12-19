package tests;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import analysis.*;
import static org.junit.Assert.*;

import org.junit.Test;

public class RulePredictionTest {

//	@Test(expected=IllegalArgumentException.class)
//	public void testInvalidPhoneme() {
//		Map<String, String> map = new HashMap<String, String>();
//		map.put("D-EY", "D-EYx");
//		new RulePrediction(map);
//	}
	
	@Test
	public void guessProb2() {
		Map<String, String> map = new HashMap<String, String>();
//		map.put("K-AE-T", "T-AE-T");
		// velar voiceless top -> alveolar voiceless stop IF at end of word/syl
		map.put("B-AA-K", "B-AA-T");
		map.put("T-AA-P", "T-AA-P");
		map.put("K-AA-T", "K-AA-T");
//		map.put("D-EY", "D-EY");
//		map.put("P-AA_R K-ER P-Y-ER", "P-AA_R T-ER P-Y-ER");
		
		RulePrediction rp = new RulePrediction(map);
		System.out.println("\n\n\n\n ******RULES ****\n\n\n");
		for (Set<Rule> rs : rp.envToRules.values()) {
			for (Rule r : rs) {
				System.out.println("\n\n***NEW RULE****\n\n");
				System.out.println(r);
			}
		}
		
		Set<String> guesses = rp.guessPronunciation("G-EY-M");

		Set<String> expectedGuesses = new HashSet<String>();
		expectedGuesses.add("D-EY-M");
		
		assertEquals(expectedGuesses, guesses);
		
	}

}
