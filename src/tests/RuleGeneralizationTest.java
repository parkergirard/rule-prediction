package tests;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import analysis.*;
import static org.junit.Assert.*;

import org.junit.Test;

import enums.PHONEME;

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
			
	}

}
