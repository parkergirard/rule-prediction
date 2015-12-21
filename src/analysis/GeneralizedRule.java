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
	
	private PhoneticEnvironment env;
	
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
		this.env = env;
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
		return env;
	}

	public void setPhoneticEnvironment(PhoneticEnvironment env) {
		this.env = env;
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
        		env.equals(r.env);
    }

    @Override
    public int hashCode() {
        return inputPhonemeFeatures.hashCode() +
        		outputPhonemeFeatures.hashCode() + Objects.hash(remainsSame)
        		+ env.hashCode();
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
		sb.append(env.toString());
		
		return sb.toString();
	}
    
    public static void printRules(Collection<GeneralizedRule> rules) {
		String str = "RULES";
		System.out.println("\n ******" + str + " ****");
		
		int count = 0;
		for (GeneralizedRule r : rules) {
			count++;
			System.out.println("\n\n*****RULE " + count + "****\n");
			System.out.println(r);
		}
	}
	
}
