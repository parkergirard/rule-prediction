package analysis;

import java.util.Objects;

import enums.*;

/**
 * Rule has information about phonetic environment, and
 * about what sounds change
 */
public class Rule implements Comparable<Rule> {
	
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
	public Rule(PhoneticEnvironment env,
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
	public Rule(PHONEME targetPhoneme, PHONEME actualPhoneme, boolean globalEnv) {
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
	public Rule(boolean global) {
		init(global);
	}
	
	/**
	 * If not specified, assume global rule
	 */
	public Rule() {
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
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		sb.append(targetPhoneme.name() + " to " + actualPhoneme.name());
		
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
        Rule r = (Rule) o;
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
    public int compareTo(Rule other) {
        return Integer.compare(sureness, other.sureness);
    }

	public void remove(POSITION w, POSITION s, POSITION v, 
			PLACE comesBeforePlace, PLACE comesAfterPlace,
			MANNER comesBeforeManner, MANNER comesAfterManner,
			VOICE comesBeforeVoice, VOICE comesAfterVoice) {
		
		if (w != null) {
			environment.removeWordPlacement(w);
		}
		if (s != null) {
			environment.removeSyllablePlacement(s);
		}
		if (v != null) {
			environment.removeVowelPlacement(v);
		}

		environment.
		removeComesAfter(comesAfterPlace, comesAfterManner, comesAfterVoice);
		
		environment.
		removeComesBefore(comesBeforePlace, 
				comesBeforeManner, comesBeforeVoice);
		
	}
	
}
