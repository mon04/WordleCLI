/**
 * An object to represent a cell (or a letter) in the Wordle game
 */
public class Cell {
    private final char value;
    private State state;

    public Cell(char value) {
        this.value = value;
        this.state = null;
    }

    public String visualised() {
        if (state == null)
            return " "+value+" ";
        return String.format("%s %c ", state.getAnsiCode(), value);
    }

    public char getValue() {
        return value;
    }

    public void setState(State state) {
        this.state=state;
    }

    public State getState() {
        return state;
    }
}