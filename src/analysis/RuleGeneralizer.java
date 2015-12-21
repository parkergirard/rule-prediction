package analysis;

import java.util.HashSet;
import java.util.Set;

public class RuleGeneralizer {

	Set<SpecificRule> givenRules;
	
	Set<GeneralizedRule> generalizedRules;
	
	/**
	 * Begin generalizing rules given a set of specific rules
	 * @param rules
	 */
	public RuleGeneralizer(Set<SpecificRule> rules) {
		this.givenRules = rules;
		generalizedRules = new HashSet<GeneralizedRule>();
	}
	
	
	
}
