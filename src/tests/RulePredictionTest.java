package tests;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import analysis.*;
import static org.junit.Assert.*;

import org.junit.Test;

import enums.*;

public class RulePredictionTest {

	
	@Test(expected=IllegalArgumentException.class)
	public void testInvalidPhoneme() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("D-EY", "D-EYx");
		new RulePrediction(map);
	}
	
	
	@Test
	public void basicTestAlwaysChangeRtoW() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("L-AE D-AXR", "L-AE D-AXR");
		map.put("R-IH-NG", "W-IH-NG");
		map.put("JH-AX R-AE-F", "JH-AX W-AE-F");
		map.put("S-T-AO_R", "S-T-AO_R");
		
		RulePrediction rp = new RulePrediction(map);
		
		assertEquals(8, rp.getRules().size());
		
		Set<Rule> expectedRules = new HashSet<Rule>();
		
		PhoneticEnvironment e = new PhoneticEnvironment(true);
		
		Rule newRule = 
				new Rule(e, PHONEME.L, PHONEME.L);
		expectedRules.add(newRule);
		

		newRule = 
				new Rule(e, PHONEME.D, PHONEME.D);
		expectedRules.add(newRule);


		newRule = 
				new Rule(e, PHONEME.R, PHONEME.W);
		expectedRules.add(newRule);
		
		newRule = 
				new Rule(e, PHONEME.JH, PHONEME.JH);
		expectedRules.add(newRule);

		newRule = 
				new Rule(e, PHONEME.F, PHONEME.F);
		expectedRules.add(newRule);

		newRule = 
				new Rule(e, PHONEME.S, PHONEME.S);
		expectedRules.add(newRule);

		newRule = 
				new Rule(e, PHONEME.T, PHONEME.T);
		expectedRules.add(newRule);
		
		assertTrue(rp.getRules().containsAll(expectedRules));
	}


	@Test
	public void test2ChangesTimeOfTransformation() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("K-AE-S-T",	"K-AE-S-T");
		map.put("S-AA-K", "TH-AA-K");
		map.put("Z-IY B-R-AX", "DH-IY B-R-AX");
		map.put("P-L-IY-Z", "P-L-IY-Z");
		
		RulePrediction rp = new RulePrediction(map);
		
		int size = rp.getRules().size();
		assertTrue(size == 8 || size == 9 || size == 10 );
		
		
		Set<Rule> expectedRules = new HashSet<Rule>();
		
		PhoneticEnvironment e = new PhoneticEnvironment(true);
			
		// K -> K always
		e = new PhoneticEnvironment(true);
		Rule newRule = 
				new Rule(e, PHONEME.K, PHONEME.K);
		expectedRules.add(newRule);

		// B -> B always
		e = new PhoneticEnvironment(true);
		newRule = 
				new Rule(e, PHONEME.B, PHONEME.B);
		expectedRules.add(newRule);
		
		// P -> P always
		e = new PhoneticEnvironment(true);
		newRule = 
				new Rule(e, PHONEME.P, PHONEME.P);
		expectedRules.add(newRule);

		// L -> L always
		e = new PhoneticEnvironment(true);
		newRule = 
				new Rule(e, PHONEME.L, PHONEME.L);
		expectedRules.add(newRule);
		
		assertTrue(rp.getRules().containsAll(expectedRules));
		
		// NOW LOOK AT DIFFERENT OPTIONS FOR THE OTHER RULES
		// ENSURE THAT AT LEAST ONE OPTION IS IN THE RULES
		// (some rules will vary depending on the order the data is read)

		boolean firstOptionIsRight = false;
		boolean secondOptionIsRight = false;
		/* FIRST OPTION:
		 * {S → S always, except at the beginning of a word/beginning 
		 * of a syllable/before a vowel AND
		S → TH at the beginning of a word/beginning of a 
		syllable/before a vowel/after anything/before anything} 
		 */
		// reset expected
		expectedRules = new HashSet<Rule>();
		// S → S always, except at the beginning of a word/beginning of a 
		// syllable/before a vowel
		e = new PhoneticEnvironment(true);
		e.removeWordPlacement(CONSONANT_POSITION.BEGINNING);
		e.removeSyllablePlacement(CONSONANT_POSITION.BEGINNING);
		e.removeVowelPlacement(VOWEL_POSITION.BEFORE);
		newRule = 
				new Rule(e, PHONEME.S, PHONEME.S);
		expectedRules.add(newRule);

		// S → TH at the beginning of a word/beginning of a 
		// syllable/before a vowel/after everything/before everything
		e = new PhoneticEnvironment(false);
		e.addWordPlacement(CONSONANT_POSITION.BEGINNING);
		e.addSyllablePlacement(CONSONANT_POSITION.BEGINNING);
		e.addVowelPlacement(VOWEL_POSITION.BEFORE);
		e.makeComesAfterGlobal();
		e.makeComesBeforeGlobal();
		newRule = 
				new Rule(e, PHONEME.S, PHONEME.TH);
		expectedRules.add(newRule);
		
		firstOptionIsRight = rp.getRules().containsAll(expectedRules);
		
		if (!firstOptionIsRight) {
			// try the second option
			
			/* SECOND OPTION:
			 * {S → TH always, except at the middle of a word/middle of a 
			 * syllable/after a vowel/before “T”}
			 */
			
			expectedRules = new HashSet<Rule>();

			e = new PhoneticEnvironment(true);
			e.removeWordPlacement(CONSONANT_POSITION.MIDDLE);
			e.removeSyllablePlacement(CONSONANT_POSITION.MIDDLE);
			e.removeVowelPlacement(VOWEL_POSITION.AFTER);
			e.removeComesBeforePhoneme(PHONEME.T);
			newRule = 
					new Rule(e, PHONEME.S, PHONEME.TH);
			expectedRules.add(newRule);
			
			secondOptionIsRight = rp.getRules().containsAll(expectedRules);
		}
		
		if (firstOptionIsRight && secondOptionIsRight) {
			fail("Both options cannot be right :(");
		} else if (!firstOptionIsRight && !secondOptionIsRight) {
			fail("Neither option is in the rules");
		}
		
		firstOptionIsRight = false;
		secondOptionIsRight = false;
		/* FIRST OPTION:
		  {Z → DH at the beginning of the word/beginning of a 
		  syllable/before a vowel/after everything/before everything AND
			Z → Z always, except at the beginning of the 
			word/beginning of a syllable/before a vowel}
		 */
		// reset expected
		expectedRules = new HashSet<Rule>();
		
		// Z → DH at the beginning of the word/beginning 
		// of a syllable/before a vowel/after everything/before everything
		e = new PhoneticEnvironment(false);
		e.addWordPlacement(CONSONANT_POSITION.BEGINNING);
		e.addSyllablePlacement(CONSONANT_POSITION.BEGINNING);
		e.addVowelPlacement(VOWEL_POSITION.BEFORE);
		e.makeComesAfterGlobal();
		e.makeComesBeforeGlobal();
		newRule = 
				new Rule(e, PHONEME.Z, PHONEME.DH);
		expectedRules.add(newRule);
		
		// Z → Z always, except at the beginning of the
		// word/beginning of a syllable/before a vowel
		e = new PhoneticEnvironment(true);
		e.removeWordPlacement(CONSONANT_POSITION.BEGINNING);
		e.removeSyllablePlacement(CONSONANT_POSITION.BEGINNING);
		e.removeVowelPlacement(VOWEL_POSITION.BEFORE);
		newRule = 
				new Rule(e, PHONEME.Z, PHONEME.Z);
		expectedRules.add(newRule);
		
		firstOptionIsRight = rp.getRules().containsAll(expectedRules);

		if (!firstOptionIsRight) {
			// try the second option

			/* SECOND OPTION:
			 * {Z → DH always, except at the end of word/end 
			 * of a syllable/after a vowel}
			 */

			expectedRules = new HashSet<Rule>();

			e = new PhoneticEnvironment(true);
			e.removeWordPlacement(CONSONANT_POSITION.END);
			e.removeSyllablePlacement(CONSONANT_POSITION.END);
			e.removeVowelPlacement(VOWEL_POSITION.AFTER);
			newRule = 
					new Rule(e, PHONEME.Z, PHONEME.DH);
			expectedRules.add(newRule);
			
			secondOptionIsRight = rp.getRules().containsAll(expectedRules);
			
		}

		if (firstOptionIsRight && secondOptionIsRight) {
			fail("Both options cannot be right :(");
		} else if (!firstOptionIsRight && !secondOptionIsRight) {
			fail("Neither option is in the rules");
		}
		
	}
	
	// (adapted from http://www.asha.org/uploadedFiles/
	// publications/archive/Monographs22.pdf, p. 16)
	@Test
	public void testRealData() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("D-UH-K", "D-UH-K");
		map.put("P-IH-G", "P-IH-K");
		map.put("P-IH G-IY", "P-IH G-IY");
		map.put("D-AO-G", "D-AO-K");
		map.put("D-AO G-IY", "D-AO G-IY");
		map.put("R-UH-B", "W-UH-P");
		map.put("R-UH B-IH-NG", "W-UH B-IH-NG");
		map.put("S-L-IY-P", "S-W-IY-P");
		
		RulePrediction rp = new RulePrediction(map);
		
		int size = rp.getRules().size();
		
		assertTrue(size == 9 || size == 10 || size == 11);

		Set<Rule> expectedRules = new HashSet<Rule>();

		PhoneticEnvironment e = new PhoneticEnvironment(true);

		// D -> D always
		Rule newRule = 
				new Rule(e, PHONEME.D, PHONEME.D);
		expectedRules.add(newRule);

		// K -> K always
		e = new PhoneticEnvironment(true);
		newRule = 
				new Rule(e, PHONEME.K, PHONEME.K);
		expectedRules.add(newRule);

		// P -> P always
		e = new PhoneticEnvironment(true);
		newRule = 
				new Rule(e, PHONEME.P, PHONEME.P);
		expectedRules.add(newRule);

		// R -> W always
		e = new PhoneticEnvironment(true);
		newRule = 
				new Rule(e, PHONEME.R, PHONEME.W);
		expectedRules.add(newRule);

		// S -> S always
		e = new PhoneticEnvironment(true);
		newRule = 
				new Rule(e, PHONEME.S, PHONEME.S);
		expectedRules.add(newRule);

		// L -> W always
		e = new PhoneticEnvironment(true);
		newRule = 
				new Rule(e, PHONEME.L, PHONEME.W);
		expectedRules.add(newRule);
		
		// NG -> NG always
		e = new PhoneticEnvironment(true);
		newRule = 
				new Rule(e, PHONEME.NG, PHONEME.NG);
		expectedRules.add(newRule);
		
		
		assertTrue(rp.getRules().containsAll(expectedRules));
		
		// NOW LOOK AT DIFFERENT OPTIONS FOR THE OTHER RULES
		// ENSURE THAT AT LEAST ONE OPTION IS IN THE RULES
		// (some rules will vary depending on the order the data is read)

		boolean firstOptionIsRight = false;
		boolean secondOptionIsRight = false;
		/* FIRST OPTION:
		 * G → G always, except at the end of a word/end of 
		 * a syllable/after vowel AND
			G → K at the end of a word/end of a syllable/after vowel/after anything/before anything
		 * }
		 */
		// reset expected
		expectedRules = new HashSet<Rule>();
		// G → G always, except at the end of a word/end of 
		// a syllable/after vowel
		e = new PhoneticEnvironment(true);
		e.removeWordPlacement(CONSONANT_POSITION.END);
		e.removeSyllablePlacement(CONSONANT_POSITION.END);
		e.removeVowelPlacement(VOWEL_POSITION.AFTER);
		newRule = 
				new Rule(e, PHONEME.G, PHONEME.G);
		expectedRules.add(newRule);

		// G → K at the end of a word/end of a syllable/after vowel/after anything
		// /before anything
		e = new PhoneticEnvironment(false);
		e.setWordPlacement(CONSONANT_POSITION.END);
		e.setSyllablePlacement(CONSONANT_POSITION.END);
		e.setVowelPlacement(VOWEL_POSITION.AFTER);
		e.makeComesAfterGlobal();
		e.makeComesBeforeGlobal();
		newRule = 
				new Rule(e, PHONEME.G, PHONEME.K);
		expectedRules.add(newRule);
		
		firstOptionIsRight = rp.getRules().containsAll(expectedRules);
		
		if (!firstOptionIsRight) {
			// try the second option
			
			/* SECOND OPTION:
			 * G → K always, except at the middle of a word/beginning 
			 * of a syllable/surrounded by vowels
			 */
			
			expectedRules = new HashSet<Rule>();

			e = new PhoneticEnvironment(true);
			e.removeWordPlacement(CONSONANT_POSITION.MIDDLE);
			e.removeSyllablePlacement(CONSONANT_POSITION.BEGINNING);
			e.removeVowelPlacement(VOWEL_POSITION.SURROUNDED_BY);
			newRule = 
					new Rule(e, PHONEME.G, PHONEME.K);
			expectedRules.add(newRule);
			
			secondOptionIsRight = rp.getRules().containsAll(expectedRules);
		}

		if (firstOptionIsRight && secondOptionIsRight) {
			fail("Both options cannot be right :(");
		} else if (!firstOptionIsRight && !secondOptionIsRight) {
			fail("Neither option is in the rules");
		}
		
		
		firstOptionIsRight = false;
		secondOptionIsRight = false;
		/* FIRST OPTION:
		 B → B always, except at the end of a word/at the end
		  of a syllable/after vowel
		AND	
		B → P at the end of a word/at the end of a syllable/after vowel
		/after anything/before anything
		 */
		// reset expected
		expectedRules = new HashSet<Rule>();
		
		//  B → B always, except at the end of a word/at the end
		//  of a syllable/after vowel
		e = new PhoneticEnvironment(true);
		e.removeWordPlacement(CONSONANT_POSITION.END);
		e.removeSyllablePlacement(CONSONANT_POSITION.END);
		e.removeVowelPlacement(VOWEL_POSITION.AFTER);
		newRule = 
				new Rule(e, PHONEME.B, PHONEME.B);
		expectedRules.add(newRule);
		
		// B → P at the end of a word/at the end of a syllable/after vowel
		// /after anything/before anything
		e = new PhoneticEnvironment(false);
		e.addWordPlacement(CONSONANT_POSITION.END);
		e.addSyllablePlacement(CONSONANT_POSITION.END);
		e.addVowelPlacement(VOWEL_POSITION.AFTER);
		e.makeComesAfterGlobal();
		e.makeComesBeforeGlobal();
		newRule = 
				new Rule(e, PHONEME.B, PHONEME.P);
		expectedRules.add(newRule);
		
		firstOptionIsRight = rp.getRules().containsAll(expectedRules);

		if (!firstOptionIsRight) {
			// try the second option

			/* SECOND OPTION:
			 * B → P  always, except at the middle of a word/beginning 
			 * of a syllable/surrounded by vowels
			 */

			expectedRules = new HashSet<Rule>();

			e = new PhoneticEnvironment(true);
			e.removeWordPlacement(CONSONANT_POSITION.MIDDLE);
			e.removeSyllablePlacement(CONSONANT_POSITION.BEGINNING);
			e.removeVowelPlacement(VOWEL_POSITION.SURROUNDED_BY);
			newRule = 
					new Rule(e, PHONEME.B, PHONEME.P);
			expectedRules.add(newRule);
			
			secondOptionIsRight = rp.getRules().containsAll(expectedRules);
			
		}

		
		if (firstOptionIsRight && secondOptionIsRight) {
			fail("Both options cannot be right :(");
		} else if (!firstOptionIsRight && !secondOptionIsRight) {
			fail("Neither option is in the rules");
		}
		
	}
	
	@Test
	public void testDoesntComeAfter() {
			Map<String, String> map = new HashMap<String, String>();
			map.put("P-T", "T-T");
			map.put("K-P", "K-P");
			
			RulePrediction rp = new RulePrediction(map);
			
			int size = rp.getRules().size();
			assertEquals(3, size);
			
			// there's only one rule we care about (the none global)
			for (Rule r : rp.getRules()) {

				if (!(r.isGlobal() && r.getTargetPhoneme().equals(r.getActualPhoneme()))) {
					assertEquals(2, r.getEnvironment().getWordPlacement().size());
					assertFalse(r.getEnvironment().getWordPlacement().contains(CONSONANT_POSITION.END));
	
					assertEquals(2, r.getEnvironment().getSyllablePlacement().size());
					assertFalse(r.getEnvironment().getSyllablePlacement().contains(CONSONANT_POSITION.END));
	
					assertEquals(3, r.getEnvironment().getVowelPlacement().size());
	
					assertEquals(24, r.getEnvironment().getComesAfterPhonemes().size());
					assertFalse(r.getEnvironment().getComesAfterPhonemes().contains(PHONEME.K));
					
					assertEquals(1, r.getEnvironment().getDoesntComeAfterPhonemes().size());
					assertTrue(r.getEnvironment().getDoesntComeAfterPhonemes().contains(PHONEME.K));
					
					assertEquals(25, r.getEnvironment().getComesBeforePhonemes().size());
					
					assertEquals(0, r.getEnvironment().getDoesntComeBeforePhonemes().size());
				}
			}
	}
	
	@Test
	public void secondExamQuestion() {
			Map<String, String> map = new HashMap<String, String>();
			map.put("P-AA-T", "P-AA-T");
			map.put("T-AA-P", "T-AA-P");
			map.put("K-AE-T", "T-AE-T");
			map.put("B-AE-K", "B-AE-T");
			map.put("D-EY", "D-EY");
			
			RulePrediction rp = new RulePrediction(map);
			
			int size = rp.getRules().size();
			assertEquals(5, size);
			
			
			Set<Rule> expectedRules = new HashSet<Rule>();
			
			expectedRules.add(Rule.getGlobalRuleForPhonemes(PHONEME.P, PHONEME.P));
			expectedRules.add(Rule.getGlobalRuleForPhonemes(PHONEME.T, PHONEME.T));
			expectedRules.add(Rule.getGlobalRuleForPhonemes(PHONEME.K, PHONEME.T));
			expectedRules.add(Rule.getGlobalRuleForPhonemes(PHONEME.B, PHONEME.B));
			expectedRules.add(Rule.getGlobalRuleForPhonemes(PHONEME.D, PHONEME.D));
			
			assertTrue(rp.getRules().containsAll(expectedRules));
	}
	
}
