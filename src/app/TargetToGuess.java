package app;

import java.util.Objects;

public class TargetToGuess {

    private String target;
    private String guess;

    public TargetToGuess(String target, String guess) {
        this.target = target;
        this.guess = guess;
    }

    public String getTarget() {
        return target;
    }

    public String getGuess() {
        return guess;
    }
    

	@Override
	/**
	 * A = B if B's details are exactly A's details
	 */
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        TargetToGuess r = (TargetToGuess) o;
        return  r.target.equals(target) &&
        		r.guess.equals(guess);
    }
	
	@Override
    public int hashCode() {
        return Objects.hash(target, guess);
    }

}
