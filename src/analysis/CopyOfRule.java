package analysis;

import java.util.Objects;

import enums.*;

/**
 * Rule has information about phonetic environment, and
 * about what sounds change
 */
public class CopyOfRule implements Comparable<CopyOfRule> {
	
	PhoneticEnvironment environment;
	
	// properties from the target sound to the pronounced sound
	PHONEME targetPhoneme;
	PHONEME actualPhoneme;
	FeatureProperties originalFeatures;
	FeatureProperties transformsToFeatures;
	int sureness = 0; // how sure we are that this rule is correct
	int precedent = 0; // order of precedence for other rules (0 done first)
	
	/**
	 * Construct a rule with info about
	 * phonetic environment and info about
	 * what sounds change
	 * @param env: What phonetic environment does it occur in?
	 * @param originalProperties: What properties does it change from?
	 * @param transformsToProperties: What properties does it change to?
	 */
	public CopyOfRule(PhoneticEnvironment env,
			PHONEME targetPhoneme,
			PHONEME actualPhoneme) {

		this.targetPhoneme = targetPhoneme;
		this.actualPhoneme = actualPhoneme;
		
		this.environment = env;
		this.originalFeatures = targetPhoneme.getProperties();
		this.transformsToFeatures = actualPhoneme.getProperties();
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
	public CopyOfRule(PHONEME targetPhoneme, PHONEME actualPhoneme, boolean globalEnv) {
		this.targetPhoneme = targetPhoneme;
		this.actualPhoneme = actualPhoneme;
		
		this.originalFeatures = targetPhoneme.getProperties();
		this.transformsToFeatures = actualPhoneme.getProperties();
		this.environment = new PhoneticEnvironment(globalEnv);
	}

	/**
	 * Construct rule
	 * Global rule: phonetic environment encompasses everything
	 * @param global: whether or not to make rule apply to all
	 * phonetic environments
	 */
	public CopyOfRule(boolean global) {
		init(global);
	}
	
	/**
	 * If not specified, assume global rule
	 */
	public CopyOfRule() {
		init(true);
	}
	
	/**
	 * Construct rule
	 * Global rule: phonetic environment encompasses everything
	 * @param global: whether or not to make rule apply to all
	 * phonetic environments
	 */
	private void init(boolean global) {
		originalFeatures = new FeatureProperties();
		transformsToFeatures = new FeatureProperties();
		environment = new PhoneticEnvironment(global);
	}


	public PHONEME getTargetPhoneme() {
		return targetPhoneme;
	}
	
	public PHONEME getActualPhoneme() {
		return actualPhoneme;
	}
	
	public void setOriginalFeatures(FeatureProperties p) {
		this.originalFeatures = p;
	}
	
	public void setTransformsToFeatures(FeatureProperties p) {
		this.transformsToFeatures = p;
	}

	public FeatureProperties getOriginalFeatures() {
		return this.originalFeatures;
	}
	
	public FeatureProperties getTransformsToFeatures() {
		return this.transformsToFeatures;
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
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		sb.append("\n" + targetPhoneme.name() + " to " + actualPhoneme.name());
		if (isGlobal()) {
			sb.append(" (GLOBAL)");
		}
		
		sb.append("\nOriginal Features\n");
		sb.append(originalFeatures.toString());
		
		sb.append("\nTransforms to Features\n");
		sb.append(transformsToFeatures.toString());
		
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
        CopyOfRule r = (CopyOfRule) o;
        return Objects.equals(sureness, r.sureness) &&
        		Objects.equals(precedent, r.precedent) &&
        		environment.equals(r.environment) &&
                originalFeatures.equals(r.originalFeatures) &&
                transformsToFeatures.equals(r.transformsToFeatures);
    }

    @Override
    public int hashCode() {
        return environment.hashCode() + 
        		originalFeatures.hashCode() +  transformsToFeatures.hashCode();
    }

    @Override
    /**
     * Compare by sureness
     */
    public int compareTo(CopyOfRule other) {
        return Integer.compare(sureness, other.sureness);
    }
	
}
