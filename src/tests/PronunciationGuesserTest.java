package tests;

import static org.junit.Assert.*;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import analysis.GeneralizedRule;
import analysis.PronunciationGuesser;
import analysis.RuleGeneralizer;
import analysis.SpecificRuleFormer;

public class PronunciationGuesserTest {

	@Test
	public void test() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("P-AA-T", "P-AA-T");
		map.put("T-AA-P", "T-AA-P");
		map.put("K-AE-T", "T-AE-T");
		map.put("B-AE-K", "B-AE-T");
		map.put("D-EY", "D-EY");
		
		SpecificRuleFormer rp = new SpecificRuleFormer(map);
		RuleGeneralizer rg = new RuleGeneralizer(rp.getRules());
		Collection<GeneralizedRule> genRules = rg.getGeneralizedRules();
		PronunciationGuesser guesser = new PronunciationGuesser(genRules, rp.getPhonemeToSpecificRules());
		assertEquals("D-EY-M", guesser.guessPronunciationOfTargetWord("G-EY-M"));
		assertEquals("D-EY-D", guesser.guessPronunciationOfTargetWord("G-EY-G"));
		assertEquals("T-UH-M", guesser.guessPronunciationOfTargetWord("K-UH-M"));
		assertEquals("T-UH-T", guesser.guessPronunciationOfTargetWord("K-UH-K"));
		assertEquals("D-EY-D T-UH-M", guesser.guessPronunciationOfTargetWord("G-EY-G K-UH-M"));
		assertEquals("D-EY-Z T-UH-M", guesser.guessPronunciationOfTargetWord("G-EY-Z K-UH-M"));
	}
	
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
		
		SpecificRuleFormer rp = new SpecificRuleFormer(map);
		
		
		RuleGeneralizer rg = new RuleGeneralizer(rp.getRules());
		
		Collection<GeneralizedRule> genRules = rg.getGeneralizedRules();
		PronunciationGuesser guesser = new PronunciationGuesser(genRules, rp.getPhonemeToSpecificRules());
//		assertEquals("D-EY-M", guesser.guessPronunciationOfTargetWord("G-EY-M"));
	}

}
