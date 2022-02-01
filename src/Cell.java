/**
 * An object to represent a cell (or a letter) in the Wordle game
 */
public class Cell {
    private final char value;
    private State state;

    /**
     * Constructor
     * @param value the letter value of this cell
     */
    public Cell(char value) {
        this.value = value;
        this.state = null;
    }

    /**
     * Get the letter coloured with ANSI
     * @return cell's letter coloured by state with an ANSI escape code
     */
    public String visualised() {
        if(state == null)
            return String.valueOf(this.value);
        return (state.getAnsiCode() + value);
    }

    /**
     * Get the cell's letter as a char
     * @return the cell's letter as a char
     */
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