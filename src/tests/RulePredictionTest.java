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
	public void test1() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("D-AA-G", "D-AA-G");
		map.put("G-AA-D", "D-AA-D");
//		map.put("K-IH T-IY", "T-IH T-IY");
		
		
		
		RulePrediction rp = new RulePrediction(map);
		System.out.println("\n\n\n\n ******RULES ****\n\n\n");
		
		for (Rule r : rp.getRules()) {
			System.out.println("\n\n*****NEW RULE****\n\n");
			System.out.println(r);
		}
		
		
	}

}
