package analysis;

import java.util.Collection;

import enums.*;

/**
 * Rule has information about phonetic environment, and
 * about what sounds change
 */
public class SpecificRule {
	
	PhoneticEnvironment environment;
	
	// properties from the target sound to the pronounced sound
	PHONEME targetPhoneme;
	PHONEME actualPhoneme;
	
	/**
	 * Construct a rule with info about
	 * phonetic environment and info about
	 * what sounds change
	 * @param env: What phonetic environment does it occur in?
	 * @param originalProperties: What properties does it change from?
	 * @param transformsToProperties: What properties does it change to?
	 */
	public SpecificRule(PhoneticEnvironment env,
			PHONEME targetPhoneme,
			PHONEME actualPhoneme) {

		this.environment = env;
		
		this.targetPhoneme = targetPhoneme;
		this.actualPhoneme = actualPhoneme;
		
	}

	/**
	 * Construct a rule with info about
	 * phonetic environment and info about
	 * what sounds change
	 * @param env: What phonetic environment does it occur in?
	 * @param targetPhoneme: original phoneme
	 * @param actualPhoneme: the original transformed into this one
	 * @param globalEnv: makes a global phonetic environment if true
	 */
	public SpecificRule(PHONEME targetPhoneme, PHONEME actualPhoneme, boolean globalEnv) {
		this.targetPhoneme = targetPhoneme;
		this.actualPhoneme = actualPhoneme;
		
		this.environment = new PhoneticEnvironment(globalEnv);
	}

	/**
	 * Construct rule
	 * Global rule: phonetic environment encompasses everything
	 * @param global: whether or not to make rule apply to all
	 * phonetic environments
	 */
	public SpecificRule(boolean global) {
		init(global);
	}
	
	/**
	 * If not specified, assume global rule
	 */
	public SpecificRule() {
		init(true);
	}
	
	/**
	 * Construct rule
	 * Global rule: phonetic environment encompasses everything
	 * @param global: whether or not to make rule apply to all
	 * phonetic environments
	 */
	private void init(boolean global) {
		environment = new PhoneticEnvironment(global);
	}


	public PHONEME getTargetPhoneme() {
		return targetPhoneme;
	}
	
	public PHONEME getActualPhoneme() {
		return actualPhoneme;
	}
	
	public void setEnvironment(PhoneticEnvironment e) {
		this.environment = e;
	}
	
	public PhoneticEnvironment getEnvironment() {
		return environment;
	}
	
	/**
	 * Returns whether or not this rule applies under any condition
	 * starting with the targetPhoneme
	 * @return if global or not
	 */
	public boolean isGlobal() {
		return environment.isGlobal();
	}
	
	public boolean transformsToSelf() {
		return targetPhoneme.equals(actualPhoneme);
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		sb.append("\n" + targetPhoneme.name() + " to " + actualPhoneme.name());
		if (isGlobal()) {
			sb.append(" (GLOBAL)");
		}
		
		sb.append("\n***PHONETIC ENV***\n");
		sb.append(environment.toString());
		
		return sb.toString();
	}
	
	@Override
	/**
	 * Rule A = Rule B if everything is the same
	 */
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SpecificRule r = (SpecificRule) o;
        return targetPhoneme.equals(r.targetPhoneme) &&
        		actualPhoneme.equals(r.actualPhoneme) &&
        		environment.equals(r.environment);
    }

    @Override
    public int hashCode() {
        return targetPhoneme.hashCode() +
        		actualPhoneme.hashCode() + environment.hashCode();
    }

	
	public static SpecificRule getGlobalRuleForPhonemes(PHONEME p1, PHONEME p2) {
		// PHONEME -> PHONEME always
		PhoneticEnvironment e = new PhoneticEnvironment(true);
		SpecificRule globalRule = new SpecificRule(e, p1, p2);
		return globalRule;
	}
	public static void printRules(Collection<SpecificRule> rules) {
		printRules(rules, true);
	}
	
	public static void printRules(Collection<SpecificRule> rules,
			boolean printSelfTransformingGlobals) {
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
