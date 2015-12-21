package tests;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import analysis.*;
import enums.*;
import static org.junit.Assert.*;

import org.junit.Test;

public class RuleGeneralizationTest {

	@Test
	public void secondExamQuestion() {
			Map<String, String> map = new HashMap<String, String>();
			map.put("P-AA-T", "P-AA-T");
			map.put("T-AA-P", "T-AA-P");
			map.put("K-AE-T", "T-AE-T");
			map.put("B-AE-K", "B-AE-T");
			map.put("D-EY", "D-EY");
			
			SpecificRuleFormer rp = new SpecificRuleFormer(map);
			RuleGeneralizer rg = new RuleGeneralizer(rp.getRules());
			
			Collection<GeneralizedRule> rules = rg.getGeneralizedRules();
			assertEquals(1, rules.size());
			// there is only one
			for (GeneralizedRule r : rules) {
				// input
				FeatureProperties input = r.getInputPhonemeFeatures();
				
				assertEquals(1, input.getPlaces().size());
				assertTrue(input.getPlaces().contains(PLACE.VELAR));
				
				assertEquals(MANNER.values().length, input.getManners().size());
				
				assertEquals(VOICE.values().length, input.getVoices().size());
				
				// output

				FeatureProperties output = r.getOutputPhonemeFeatures();
				
				assertEquals(1, output.getPlaces().size());
				assertTrue(output.getPlaces().contains(PLACE.ALVEOLAR));

				assertEquals(0, output.getManners().size());
				assertEquals(0, output.getVoices().size());
				
				// remains same
				assertEquals(2, r.getFeatureTypesThatRemainSame().size());
				assertTrue(r.getFeatureTypesThatRemainSame().contains(FEATURE_TYPE.VOICE));
				assertTrue(r.getFeatureTypesThatRemainSame().contains(FEATURE_TYPE.MANNER));
				
				// phonetic env
				assertTrue(r.getPhoneticEnvironment().isGlobal());
				
			}
			
	}
	
	@Test
	public void example2() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("K-AE-S-T",	"K-AE-S-T");
		map.put("S-AA-K", "TH-AA-K");
		map.put("Z-IY B-R-AX", "DH-IY B-R-AX");
		map.put("P-L-IY-Z", "P-L-IY-Z");
		
		SpecificRuleFormer rp = new SpecificRuleFormer(map);
		
		System.out.println(CONTRASTING_PHONEME.valueOf(PHONEME.P.name()));
		
		RuleGeneralizer rg = new RuleGeneralizer(rp.getRules());
		
		Collection<GeneralizedRule> rules = rg.getGeneralizedRules();
//		GeneralizedRule.printRules(rules);
	}

}
