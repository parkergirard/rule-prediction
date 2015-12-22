package app;

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

}
