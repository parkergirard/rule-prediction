package analysis;

import java.util.Objects;
import java.util.Set;

import enums.RULE_PROPERTY;

/**
 * Rule has information about phonetic environment, and
 * about what sounds change
 */
public class Rule implements Comparable<Rule> {
	
	RULE_PROPERTY wordPlacement;
	RULE_PROPERTY syllablePlacement;
	RULE_PROPERTY vowelPlacement;
	Properties fromProperties;
	Properties toProperties;
	int sureness = 0; // how sure we are that this rule is correct
	int precedent = 0; // order of precedence for other rules (0 done first)
	
	/**
	 * Construct a rule with info about
	 * phonetic environment and info about
	 * what sounds change
	 * @param wordPlacement: Is it at the beginning or end of a word?
	 * @param syllablePlacement: Is it at the beginning or end of a syllable?
	 * @param vowelPlacement: Is it before, after, or surrounded by vowels?
	 * @param fromProperties: What properties does it change from?
	 * @param toProperties: What properties does it change to?
	 */
	public Rule(RULE_PROPERTY wordPlacement, 
			RULE_PROPERTY syllablePlacement,
			RULE_PROPERTY vowelPlacement, Properties fromProperties,
			Properties toProperties) {
		this.wordPlacement = wordPlacement;
		this.syllablePlacement = syllablePlacement;
		this.vowelPlacement = vowelPlacement;
		this.fromProperties = fromProperties;
		this.toProperties = toProperties;
	}
	
	
	@Override
	/**
	 * Rules are equal if they have the exact same properties
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
        		Objects.equals(wordPlacement, r.wordPlacement) &&
                Objects.equals(syllablePlacement, r.syllablePlacement) &&
                Objects.equals(vowelPlacement, r.vowelPlacement) &&
                Objects.equals(fromProperties, r.fromProperties) &&
                Objects.equals(toProperties, r.toProperties);
    }

    @Override
    public int hashCode() {
        return Objects.hash(wordPlacement, syllablePlacement, vowelPlacement, 
        		fromProperties, toProperties);
    }

    @Override
    /**
     * Compare by sureness
     */
    public int compareTo(Rule other) {
        return Integer.compare(sureness, other.sureness);
    }
	
}
