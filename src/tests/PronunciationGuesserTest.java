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
		assertEquals("D-UH-K", guesser.guessPronunciationOfTargetWord("D-UH-G"));
		String guess = guesser.guessPronunciationOfTargetWord("L-AH K-IY");
		assertEquals("W-AH K-IY", guess);
		guess = guesser.guessPronunciationOfTargetWord("T-AE-G");
		assertEquals("T-AE-K", guess);
		guess = guesser.guessPronunciationOfTargetWord("R-AE G-IY");
		assertEquals("W-AE G-IY", guess);
		guess = guesser.guessPronunciationOfTargetWord("L-IH-D");
		assertEquals("W-IH-D", guess);
	}
	
	@Test
	public void testZebraData() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("K-AE-S-T", "K-AE-S-T");
		map.put("S-AA-K", "TH-AA-K");
		map.put("Z-IY B-R-AX", "DH-IY B-R-AX");
		map.put("P-L-IY-Z", "P-L-IY-Z");
		
		SpecificRuleFormer rp = new SpecificRuleFormer(map);
		RuleGeneralizer rg = new RuleGeneralizer(rp.getRules());
		Collection<GeneralizedRule> genRules = rg.getGeneralizedRules();
		PronunciationGuesser guesser = new PronunciationGuesser(genRules, rp.getPhonemeToSpecificRules());
		assertEquals("DH-IH-P", guesser.guessPronunciationOfTargetWord("Z-IH-P"));

		String guess = guesser.guessPronunciationOfTargetWord("HH-IH-S");
		assertEquals("HH-IH-S", guess);
		
		guess = guesser.guessPronunciationOfTargetWord("D-AA-G-Z");
		assertEquals("D-AA-G-Z", guess);
		
		guess = guesser.guessPronunciationOfTargetWord("W-IH-SH");
		assertEquals("W-IH-SH", guess);
		
		guess = guesser.guessPronunciationOfTargetWord("M-IY-S");
		assertEquals("M-IY-S", guess);
		
		guess = guesser.guessPronunciationOfTargetWord("S-AE-T");
		assertEquals("TH-AE-T", guess);
	}
	
	@Test
	public void test3() {
		Map<String, String> map = new HashMap<String, String>();

		map.put("L-AE D-AXR", "L-AE D-AXR");
		map.put("R-IH-NG", "W-IH-NG");
		map.put("JH-AX R-AE-F", "JH-AX W-AE-F");
		map.put("S-T-AO_R", "S-T-AO_R");
		
		SpecificRuleFormer rp = new SpecificRuleFormer(map);
		RuleGeneralizer rg = new RuleGeneralizer(rp.getRules());
		Collection<GeneralizedRule> genRules = rg.getGeneralizedRules();
		PronunciationGuesser guesser = new PronunciationGuesser(genRules, rp.getPhonemeToSpecificRules());
		String guess = guesser.guessPronunciationOfTargetWord("M-AE N-ER");
		assertEquals("M-AE N-ER", guess);
		guess = guesser.guessPronunciationOfTargetWord("R-AX B-ER");
		assertEquals("W-AX B-ER", guess);
		guess = guesser.guessPronunciationOfTargetWord("S-T-R-IH-P");
		assertEquals("S-T-W-IH-P", guess);
	}
	
	@Test
	public void testLithpData() {
		Map<String, String> map = new HashMap<String, String>();

		map.put("L-IH-S-P", "L-IH-TH-P");
		map.put("D-AA-G-Z", "D-AA-G-DH");
		map.put("SH-IH-P", "S-IH-P");
		map.put("W-AA-CH", "W-AA-SH");
		
		SpecificRuleFormer rp = new SpecificRuleFormer(map);
		RuleGeneralizer rg = new RuleGeneralizer(rp.getRules());
		Collection<GeneralizedRule> genRules = rg.getGeneralizedRules();
		
		
		PronunciationGuesser guesser = new PronunciationGuesser(genRules, rp.getPhonemeToSpecificRules());
		
		String guess = guesser.guessPronunciationOfTargetWord("HH-AE-S");
		assertEquals("HH-AE-TH", guess);
		
		guess = guesser.guessPronunciationOfTargetWord("JH-IH-M");
		assertEquals("ZH-IH-M", guess);

		guess = guesser.guessPronunciationOfTargetWord("SH-AA-P");
		assertEquals("S-AA-P", guess);

		guess = guesser.guessPronunciationOfTargetWord("K-R-AE-SH");
		assertEquals("K-R-AE-S", guess);
		
		guess = guesser.guessPronunciationOfTargetWord("S-L-AE-M");
		assertEquals("TH-L-AE-M", guess);

		guess = guesser.guessPronunciationOfTargetWord("CH-AA-P");
		assertEquals("SH-AA-P", guess);
	}

}
