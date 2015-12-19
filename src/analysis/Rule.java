package analysis;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import enums.*;

/**
 * Rule has information about phonetic environment, and
 * about what sounds change
 */
public class Rule implements Comparable<Rule> {
	
	Set<POSITION> wordPlacement;
	Set<POSITION> syllablePlacement;
	Set<POSITION> vowelPlacement;

	// sets of descriptions of sounds before/after the rule occurs
	Properties comesAfterProperties;
	Properties comesBeforeProperties;
	
	// properties from the target sound to the pronounced sound
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
	 * @param afterProperties: Descriptions of sounds after the rule occurs
	 * @param beforeProperties: Descriptions of sounds before the rule occurs
	 * @param fromProperties: What properties does it change from?
	 * @param toProperties: What properties does it change to?
	 */
	public Rule(Set<POSITION> wordPlacement, 
			Set<POSITION> syllablePlacement,
			Set<POSITION> vowelPlacement, 
			Properties afterProperties,
			Properties beforeProperties,
			Properties fromProperties,
			Properties toProperties) {
		
		this.wordPlacement = wordPlacement;
		this.syllablePlacement = syllablePlacement;
		this.vowelPlacement = vowelPlacement;
		
		this.comesAfterProperties = afterProperties;
		this.comesBeforeProperties = beforeProperties;
		
		this.fromProperties = fromProperties;
		this.toProperties = toProperties;
	}
	
	/**
	 * Construct empty rule
	 */
	public Rule() {
		wordPlacement = new HashSet<POSITION>();
		syllablePlacement = new HashSet<POSITION>();
		vowelPlacement = new HashSet<POSITION>();

		comesAfterProperties = new Properties();
		comesBeforeProperties = new Properties();
		
		fromProperties = new Properties();
		toProperties = new Properties();
	}
	
	// OPTIONS TO UPDATE AND GET INFO ABOUT THE RULE
	
	public void addWordPlacement(POSITION p) {
		wordPlacement.add(p);
	}
	
	public Set<POSITION> getWordPlacement() {
		return wordPlacement;
	}
	
	public void addSyllablePlacement(POSITION p) {
		syllablePlacement.add(p);
	}
	
	public Set<POSITION> getSyllablePlacement() {
		return syllablePlacement;
	}
	
	public void addVowelPlacement(POSITION p) {
		vowelPlacement.add(p);
	}
	
	public Set<POSITION> getVowelPlacement() {
		return vowelPlacement;
	}

	/**
	 * If a param is null, it is ignored
	 * @param p
	 * @param m
	 * @param v
	 */
	public void addComesAfterProperties(PLACE p, MANNER m, VOICE v) {
		comesAfterProperties.add(p, m, v);
	}
	
	public Properties getComesAfterProperties() {
		return comesAfterProperties;
	}

	/**
	 * If a param is null, it is ignored
	 * @param p
	 * @param m
	 * @param v
	 */
	public void addComesBeforeProperties(PLACE p, MANNER m, VOICE v) {
		comesBeforeProperties.add(p, m, v);
	}
	
	public Properties getComesBeforeProperties() {
		return comesBeforeProperties;
	}

	

	public void setFromProperties(Properties p) {
		this.fromProperties = p;
	}
	
	public Properties getFromProperties() {
		return fromProperties;
	}
	
	public void setToProperties(Properties p) {
		this.toProperties = p;
	}
	
	public Properties getToProperties() {
		return toProperties;
	}
	
	public boolean samePropertyChanges(Rule r) {
		return Objects.equals(fromProperties, r.fromProperties) &&
                Objects.equals(toProperties, r.toProperties);
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();

		sb.append("Word Placements: ");
		sb.append(wordPlacement);
		sb.append("\nSyllable Placements: ");
		sb.append(syllablePlacement);
		sb.append("\nVowel Placements: ");
		sb.append(vowelPlacement);

		sb.append("\nFrom Properties\n");
		sb.append(fromProperties.toString());
		
		sb.append("\nTo Properties\n");
		sb.append(toProperties.toString());

		sb.append("\nComes After Properties\n");
		sb.append(comesAfterProperties.toString());
		
		sb.append("\nComes Before Properties\n");
		sb.append(comesBeforeProperties.toString());
		
		return sb.toString();
	}
	
	@Override
	/**
	 * Rules are equal if they have the exact same from and to (change)
	 * properties
	 */
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Rule r = (Rule) o;
        return Objects.equals(wordPlacement, r.wordPlacement) &&
                Objects.equals(syllablePlacement, r.syllablePlacement) &&
                Objects.equals(vowelPlacement, r.vowelPlacement) &&
                Objects.equals(fromProperties, r.fromProperties) &&
                Objects.equals(toProperties, r.toProperties);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fromProperties, toProperties);
    }

    @Override
    /**
     * Compare by sureness
     */
    public int compareTo(Rule other) {
        return Integer.compare(sureness, other.sureness);
    }
	
}
