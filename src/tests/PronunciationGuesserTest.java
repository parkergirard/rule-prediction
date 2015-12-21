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
//		GeneralizedRule.printRules(genRules);
		PronunciationGuesser guesser = new PronunciationGuesser(genRules);
		String guess = guesser.guessPronunciationOfTargetWord("G-EY-M");
		System.out.println("G-EY-M to " + guess);
	}

}
