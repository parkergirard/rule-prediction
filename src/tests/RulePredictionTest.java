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

	@SuppressWarnings("unused")
	private void printRules(Set<Rule> rules) {
		System.out.println("\n\n\n\n ******RULES ****\n\n\n");
		
		for (Rule r : rules) {
			System.out.println("\n\n*****NEW RULE****\n\n");
			System.out.println(r);
		}
	}
	
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
		
		assertEquals(11, rp.getRules().size());
		
		Set<Rule> expectedRules = new HashSet<Rule>();
		
		PhoneticEnvironment e = new PhoneticEnvironment(true);
		
		Rule newRule = 
				new Rule(e, PHONEME.L.getProperties(), PHONEME.L.getProperties());
		expectedRules.add(newRule);
		
		newRule = 
				new Rule(e, PHONEME.AE.getProperties(), PHONEME.AE.getProperties());
		expectedRules.add(newRule);

		newRule = 
				new Rule(e, PHONEME.D.getProperties(), PHONEME.D.getProperties());
		expectedRules.add(newRule);

		newRule = 
				new Rule(e, PHONEME.AXR.getProperties(), PHONEME.AXR.getProperties());
		expectedRules.add(newRule);

		newRule = 
				new Rule(e, PHONEME.R.getProperties(), PHONEME.W.getProperties());
		expectedRules.add(newRule);
		
		newRule = 
				new Rule(e, PHONEME.JH.getProperties(), PHONEME.JH.getProperties());
		expectedRules.add(newRule);

		newRule = 
				new Rule(e, PHONEME.AX.getProperties(), PHONEME.AX.getProperties());
		expectedRules.add(newRule);

		newRule = 
				new Rule(e, PHONEME.F.getProperties(), PHONEME.F.getProperties());
		expectedRules.add(newRule);

		newRule = 
				new Rule(e, PHONEME.S.getProperties(), PHONEME.S.getProperties());
		expectedRules.add(newRule);

		newRule = 
				new Rule(e, PHONEME.T.getProperties(), PHONEME.T.getProperties());
		expectedRules.add(newRule);

		newRule = 
				new Rule(e, PHONEME.AO_R.getProperties(), PHONEME.AO_R.getProperties());
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
		assertTrue(size == 10 || size == 11 || size == 12 );
		
		Set<Rule> expectedRules = new HashSet<Rule>();
		
		PhoneticEnvironment e = new PhoneticEnvironment(true);
		
		// AE → AE always
		Rule newRule = 
				new Rule(e, PHONEME.AE.getProperties(), PHONEME.AE.getProperties());
		expectedRules.add(newRule);
		
		// AA -> AA always
		e = new PhoneticEnvironment(true);
		newRule = 
				new Rule(e, PHONEME.AA.getProperties(), PHONEME.AA.getProperties());
		expectedRules.add(newRule);
		
		// K -> K always
		e = new PhoneticEnvironment(true);
		newRule = 
				new Rule(e, PHONEME.K.getProperties(), PHONEME.K.getProperties());
		expectedRules.add(newRule);
		
		// IY -> IY always
		e = new PhoneticEnvironment(true);
		newRule = 
				new Rule(e, PHONEME.IY.getProperties(), PHONEME.IY.getProperties());
		expectedRules.add(newRule);

		// B -> B always
		e = new PhoneticEnvironment(true);
		newRule = 
				new Rule(e, PHONEME.B.getProperties(), PHONEME.B.getProperties());
		expectedRules.add(newRule);

		// AX -> AX always
		e = new PhoneticEnvironment(true);
		newRule = 
				new Rule(e, PHONEME.AX.getProperties(), PHONEME.AX.getProperties());
		expectedRules.add(newRule);
		
		// P -> P always
		e = new PhoneticEnvironment(true);
		newRule = 
				new Rule(e, PHONEME.P.getProperties(), PHONEME.P.getProperties());
		expectedRules.add(newRule);

		// L -> L always
		e = new PhoneticEnvironment(true);
		newRule = 
				new Rule(e, PHONEME.L.getProperties(), PHONEME.L.getProperties());
		expectedRules.add(newRule);
		
		assertTrue(rp.getRules().containsAll(expectedRules));
		
		// NOW LOOK AT DIFFERENT OPTIONS FOR THE OTHER RULES
		// ENSURE THAT AT LEAST ONE OPTION IS IN THE RULES
		// (some rules will vary depending on the order the data is read)

		boolean firstOptionIsRight = false;
		boolean secondOptionIsRight = false;
		/* FIRST OPTION:
		 * {S → S always, except at the beginning of a word/beginning 
		 * of a syllable/before a vowel/before “AA” AND
		S → TH at the beginning of a word/beginning of a 
		syllable/before a vowel/before “AA”} 
		 */
		// reset expected
		expectedRules = new HashSet<Rule>();
		// S → S always, except at the beginning of a word/beginning of a 
		// syllable/before a vowel/before “AA”
		e = new PhoneticEnvironment(true);
		e.removeWordPlacement(CONSONANT_POSITION.BEGINNING);
		e.removeSyllablePlacement(CONSONANT_POSITION.BEGINNING);
		e.removeVowelPlacement(VOWEL_POSITION.BEFORE);
		e.removeComesBefore(PHONEME.AA);
		newRule = 
				new Rule(e, PHONEME.S.getProperties(), PHONEME.S.getProperties());
		expectedRules.add(newRule);

		// S → TH at the beginning of a word/beginning of a 
		// syllable/before a vowel/before “AA”
		e = new PhoneticEnvironment(false);
		e.addWordPlacement(CONSONANT_POSITION.BEGINNING);
		e.addSyllablePlacement(CONSONANT_POSITION.BEGINNING);
		e.addVowelPlacement(VOWEL_POSITION.BEFORE);
		e.addComesBefore(PHONEME.AA);
		newRule = 
				new Rule(e, PHONEME.S.getProperties(), PHONEME.TH.getProperties());
		expectedRules.add(newRule);
		
		firstOptionIsRight = rp.getRules().containsAll(expectedRules);
		
		if (!firstOptionIsRight) {
			// try the second option
			
			/* SECOND OPTION:
			 * {S → TH always, except at the middle of a word/middle of a 
			 * syllable/after a vowel/after “AE”/before “T”}
			 */
			
			expectedRules = new HashSet<Rule>();

			e = new PhoneticEnvironment(true);
			e.removeWordPlacement(CONSONANT_POSITION.MIDDLE);
			e.removeSyllablePlacement(CONSONANT_POSITION.MIDDLE);
			e.removeVowelPlacement(VOWEL_POSITION.AFTER);
			e.removeComesAfter(PHONEME.AE);
			e.removeComesBefore(PHONEME.T);
			newRule = 
					new Rule(e, PHONEME.S.getProperties(), PHONEME.TH.getProperties());
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
		  syllable/before a vowel/before “IY” AND
			Z → Z always, except at the beginning of the 
			word/beginning of a syllable/before a vowel/before “IY”}
		 */
		// reset expected
		expectedRules = new HashSet<Rule>();
		
		// Z → DH at the beginning of the word/beginning 
		// of a syllable/before a vowel/before “IY”
		e = new PhoneticEnvironment(false);
		e.addWordPlacement(CONSONANT_POSITION.BEGINNING);
		e.addSyllablePlacement(CONSONANT_POSITION.BEGINNING);
		e.addVowelPlacement(VOWEL_POSITION.BEFORE);
		e.addComesBefore(PHONEME.IY);
		newRule = 
				new Rule(e, PHONEME.Z.getProperties(), PHONEME.DH.getProperties());
		expectedRules.add(newRule);
		
		// Z → Z always, except at the beginning of the
		// word/beginning of a syllable/before a vowel/before “IY”
		e = new PhoneticEnvironment(true);
		e.removeWordPlacement(CONSONANT_POSITION.BEGINNING);
		e.removeSyllablePlacement(CONSONANT_POSITION.BEGINNING);
		e.removeVowelPlacement(VOWEL_POSITION.BEFORE);
		e.removeComesBefore(PHONEME.IY);
		newRule = 
				new Rule(e, PHONEME.Z.getProperties(), PHONEME.Z.getProperties());
		expectedRules.add(newRule);
		
		firstOptionIsRight = rp.getRules().containsAll(expectedRules);

		if (!firstOptionIsRight) {
			// try the second option

			/* SECOND OPTION:
			 * {Z → DH always, except at the end of word/end 
			 * of a syllable/after a vowel/after “IY”}
			 */

			expectedRules = new HashSet<Rule>();

			e = new PhoneticEnvironment(true);
			e.removeWordPlacement(CONSONANT_POSITION.END);
			e.removeSyllablePlacement(CONSONANT_POSITION.END);
			e.removeVowelPlacement(VOWEL_POSITION.AFTER);
			e.removeComesAfter(PHONEME.IY);
			newRule = 
					new Rule(e, PHONEME.Z.getProperties(), PHONEME.DH.getProperties());
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
		map.put("D-UH-G", "D-UH-K");
		map.put("D-AO G-IY", "D-AO G-IY");
		map.put("R-UH-B", "W-UH-P");
		map.put("R-UH B-IH-NG", "W-UH B-IH-NG");
		map.put("S-L-IY-P", "S-W-IY-P");
		
		RulePrediction rp = new RulePrediction(map);
		
		int size = rp.getRules().size();
		
		assertTrue(size == 10 || size == 11 || size == 12);

		Set<Rule> expectedRules = new HashSet<Rule>();

		PhoneticEnvironment e = new PhoneticEnvironment(true);

		// D -> D always
		Rule newRule = 
				new Rule(e, PHONEME.D.getProperties(), PHONEME.D.getProperties());
		expectedRules.add(newRule);

		// UH -> UH always
		e = new PhoneticEnvironment(true);
		newRule = 
				new Rule(e, PHONEME.UH.getProperties(), PHONEME.UH.getProperties());
		expectedRules.add(newRule);

		// K -> K always
		e = new PhoneticEnvironment(true);
		newRule = 
				new Rule(e, PHONEME.K.getProperties(), PHONEME.K.getProperties());
		expectedRules.add(newRule);

		// P -> P always
		e = new PhoneticEnvironment(true);
		newRule = 
				new Rule(e, PHONEME.P.getProperties(), PHONEME.P.getProperties());
		expectedRules.add(newRule);

		// IH -> IH always
		e = new PhoneticEnvironment(true);
		newRule = 
				new Rule(e, PHONEME.IH.getProperties(), PHONEME.IH.getProperties());
		expectedRules.add(newRule);
		
		// AO -> AO always
		e = new PhoneticEnvironment(true);
		newRule = 
				new Rule(e, PHONEME.AO.getProperties(), PHONEME.AO.getProperties());
		expectedRules.add(newRule);

		// IY -> IY always
		e = new PhoneticEnvironment(true);
		newRule = 
				new Rule(e, PHONEME.IY.getProperties(), PHONEME.IY.getProperties());
		expectedRules.add(newRule);

		// R -> W always
		e = new PhoneticEnvironment(true);
		newRule = 
				new Rule(e, PHONEME.R.getProperties(), PHONEME.W.getProperties());
		expectedRules.add(newRule);

		// S -> S always
		e = new PhoneticEnvironment(true);
		newRule = 
				new Rule(e, PHONEME.S.getProperties(), PHONEME.S.getProperties());
		expectedRules.add(newRule);

		// L -> W always
		e = new PhoneticEnvironment(true);
		newRule = 
				new Rule(e, PHONEME.L.getProperties(), PHONEME.W.getProperties());
		expectedRules.add(newRule);
		
		// NG -> NG always
		e = new PhoneticEnvironment(true);
		newRule = 
				new Rule(e, PHONEME.NG.getProperties(), PHONEME.NG.getProperties());
		expectedRules.add(newRule);
		
		
		assertTrue(rp.getRules().containsAll(expectedRules));
		
		// NOW LOOK AT DIFFERENT OPTIONS FOR THE OTHER RULES
		// ENSURE THAT AT LEAST ONE OPTION IS IN THE RULES
		// (some rules will vary depending on the order the data is read)

		boolean firstOptionIsRight = false;
		boolean secondOptionIsRight = false;
		/* FIRST OPTION:
		 * G → G always, except at the end of a word/end of 
		 * a syllable/after vowel/after “AO” or "IH"
		 * AND
			G → K at the end of a word/end of a syllable/after vowel/after “AO” or "IH"}
		 */
		// reset expected
		expectedRules = new HashSet<Rule>();
		// G → G always, except at the end of a word/end of 
		// a syllable/after vowel/after “AO” or "IH"
		e = new PhoneticEnvironment(true);
		e.removeWordPlacement(CONSONANT_POSITION.END);
		e.removeSyllablePlacement(CONSONANT_POSITION.END);
		e.removeVowelPlacement(VOWEL_POSITION.AFTER);
		e.removeComesAfter(PHONEME.AO);
		e.removeComesAfter(PHONEME.IH);
		newRule = 
				new Rule(e, PHONEME.G.getProperties(), PHONEME.G.getProperties());
		expectedRules.add(newRule);

		// G → K at the end of a word/end of a syllable/after vowel/after “AO” or "IH"
		e = new PhoneticEnvironment(false);
		e.setWordPlacement(CONSONANT_POSITION.END);
		e.setSyllablePlacement(CONSONANT_POSITION.END);
		e.setVowelPlacement(VOWEL_POSITION.AFTER);
		e.addComesAfter(PHONEME.AO);
		e.addComesAfter(PHONEME.IH);
		newRule = 
				new Rule(e, PHONEME.G.getProperties(), PHONEME.K.getProperties());
		expectedRules.add(newRule);
		
		firstOptionIsRight = rp.getRules().containsAll(expectedRules);
		
		if (!firstOptionIsRight) {
			// try the second option
			
			/* SECOND OPTION:
			 * G → K always, except at the middle of a word/beginning 
			 * of a syllable/surrounded by vowels/before “IY”
			 */
			
			expectedRules = new HashSet<Rule>();

			e = new PhoneticEnvironment(true);
			e.removeWordPlacement(CONSONANT_POSITION.MIDDLE);
			e.removeSyllablePlacement(CONSONANT_POSITION.BEGINNING);
			e.removeVowelPlacement(VOWEL_POSITION.SURROUNDED_BY);
			e.removeComesBefore(PHONEME.IY);
			newRule = 
					new Rule(e, PHONEME.G.getProperties(), PHONEME.K.getProperties());
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
		  of a syllable/after vowel/after "UH"
		AND	
		B → P at the end of a word/at the end of a syllable/after vowel/after "UH"
		 */
		// reset expected
		expectedRules = new HashSet<Rule>();
		
		//  B → B always, except at the end of a word/at the end
		//  of a syllable/after vowel/after "UH"
		e = new PhoneticEnvironment(true);
		e.removeWordPlacement(CONSONANT_POSITION.END);
		e.removeSyllablePlacement(CONSONANT_POSITION.END);
		e.removeVowelPlacement(VOWEL_POSITION.AFTER);
		e.removeComesAfter(PHONEME.UH);
		newRule = 
				new Rule(e, PHONEME.B.getProperties(), PHONEME.B.getProperties());
		expectedRules.add(newRule);
		
		// B → P at the end of a word/at the end of a syllable/after vowel/after "UH"
		e = new PhoneticEnvironment(false);
		e.addWordPlacement(CONSONANT_POSITION.END);
		e.addSyllablePlacement(CONSONANT_POSITION.END);
		e.addVowelPlacement(VOWEL_POSITION.AFTER);
		e.addComesAfter(PHONEME.UH);
		newRule = 
				new Rule(e, PHONEME.B.getProperties(), PHONEME.P.getProperties());
		expectedRules.add(newRule);
		
		firstOptionIsRight = rp.getRules().containsAll(expectedRules);

		if (!firstOptionIsRight) {
			// try the second option

			/* SECOND OPTION:
			 * B → P  always, except at the middle of a word/beginning 
			 * of a syllable/surrounded by vowels/before “IH”
			 */

			expectedRules = new HashSet<Rule>();

			e = new PhoneticEnvironment(true);
			e.removeWordPlacement(CONSONANT_POSITION.MIDDLE);
			e.removeSyllablePlacement(CONSONANT_POSITION.BEGINNING);
			e.removeVowelPlacement(VOWEL_POSITION.SURROUNDED_BY);
			e.removeComesBefore(PHONEME.IH);
			newRule = 
					new Rule(e, PHONEME.B.getProperties(), PHONEME.P.getProperties());
			expectedRules.add(newRule);
			
			secondOptionIsRight = rp.getRules().containsAll(expectedRules);
			
		}

		
		if (firstOptionIsRight && secondOptionIsRight) {
			fail("Both options cannot be right :(");
		} else if (!firstOptionIsRight && !secondOptionIsRight) {
			fail("Neither option is in the rules");
		}
		
	}
	
}
