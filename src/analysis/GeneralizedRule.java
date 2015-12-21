package analysis;
import java.util.Collection;
import java.util.Objects;
import java.util.Set;

import enums.*;

public class GeneralizedRule {
	
	private FeatureProperties inputPhonemeFeatures;
	private FeatureProperties outputPhonemeFeatures;
	// set of features types that remain the same as the input
	// phoneme features (place/manner/voice)
	private Set<FEATURE_TYPE> remainsSame;
	
	private PhoneticEnvironment ruleEnv;
	
	/**
	 * Construct an "empty" generalized rule
	 * @param inputPhoneme
	 */
	public GeneralizedRule(FeatureProperties inputPhonemeFeatures, 
			FeatureProperties outputPhonemeFeatures, 
			Set<FEATURE_TYPE> remainsSame, PhoneticEnvironment env) {
		
		this.inputPhonemeFeatures = inputPhonemeFeatures;
		this.outputPhonemeFeatures = outputPhonemeFeatures;
		this.remainsSame = remainsSame;
		this.ruleEnv = env;
	}

	public FeatureProperties getInputPhonemeFeatures() {
		return inputPhonemeFeatures;
	}
	
	public FeatureProperties getOutputPhonemeFeatures() {
		return outputPhonemeFeatures;
	}
	
	public Set<FEATURE_TYPE> getFeatureTypesThatRemainSame() {
		return remainsSame;
	}
	
	public void addFeatureTypesThatRemainsSame(FEATURE_TYPE f) {
		remainsSame.add(f);
	}

	public PhoneticEnvironment getPhoneticEnvironment() {
		return ruleEnv;
	}

	public void setPhoneticEnvironment(PhoneticEnvironment env) {
		this.ruleEnv = env;
	}
	
	/**
	 * Whether or not this rule applies to a given phoneme
	 * @param p: the phoneme
	 * @return true if all of the rule's 
	 * 	input features contain all of p's features
	 */
	public boolean appliesToPhoneme(PHONEME p) {
		return inputPhonemeFeatures.getPlaces().contains(p.getPlace()) &&
				inputPhonemeFeatures.getManners().contains(p.getManner()) &&
						inputPhonemeFeatures.getVoices().contains(p.getVoice());
	}
	
	/**
	 * Whether or not this rule applies to a given environment
	 * @param e: the environment
	 * @param ignoreDoesntCome: whether or not to check if doesntComeAfter/Before contains
	 * the other environment's
	 * @return true if all aspects of the rule's environment contain all aspects
	 * of the environment
	 */
	public boolean appliesToEnvironment(PhoneticEnvironment e, boolean ignoreDoesntCome) {
		return ruleEnv.containsEnvironment(e, ignoreDoesntCome);
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
        GeneralizedRule r = (GeneralizedRule) o;
        return inputPhonemeFeatures.equals(r.inputPhonemeFeatures) &&
        		outputPhonemeFeatures.equals(r.outputPhonemeFeatures) &&
        		remainsSame.equals(r.remainsSame) &&
        		ruleEnv.equals(r.ruleEnv);
    }

    @Override
    public int hashCode() {
        return inputPhonemeFeatures.hashCode() +
        		outputPhonemeFeatures.hashCode() + Objects.hash(remainsSame)
        		+ ruleEnv.hashCode();
    }
    
    @Override
	public String toString() {
		StringBuilder sb = new StringBuilder();

		sb.append("\n***INPUT FEATURES***\n");
		sb.append(inputPhonemeFeatures.toString());

		sb.append("\n\n***OUTPUT FEATURES***\n");
		sb.append(outputPhonemeFeatures.toString());

		sb.append("\n\n***REMAINS SAME***\n");
		sb.append(remainsSame.toString());
		
		sb.append("\n\n***PHONETIC ENV***\n");
		sb.append(ruleEnv.toString());
		
		return sb.toString();
	}
    
    public static void printRules(Collection<GeneralizedRule> rules) {
		String str = "GEN RULES (" + rules.size() + ")";
		System.out.println("\n ******" + str + " ****");
		
		int count = 0;
		for (GeneralizedRule r : rules) {
			count++;
			System.out.println("\n\n*****RULE " + count + "****\n");
			System.out.println(r);
		}
	}
	
}
