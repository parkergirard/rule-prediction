package analysis;

import java.util.Set;

public interface Rule {
	@SuppressWarnings("unused")
	public static void printRules(Set<SpecificRule> rules) {
		printRules(rules, true);
	}
	
	public static void printRules(Set<SpecificRule> rules, boolean printSelfTransformingGlobals) {
		String str = "";
		if (printSelfTransformingGlobals) {
			str = "RULES";
		} else {
			str = "NON SELF TRANSFORMING GLOBAL RULES";
		}
		System.out.println("\n ******" + str + " ****");
		
		int count = 0;
		for (SpecificRule r : rules) {
			if (r.isGlobal() && r.getTargetPhoneme().equals(r.getActualPhoneme()) 
					&& !printSelfTransformingGlobals) {
				continue;
			}
			count++;
			System.out.println("\n\n*****RULE " + count + "****\n");
			System.out.println(r);
		}
	}
}
