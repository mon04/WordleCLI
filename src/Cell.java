public class Cell {
    private final char value;
    private State state;

    public Cell(char value) {
        this.value = value;
        this.state = null;
    }

    public String toString() {
        if(state == null)
            return String.valueOf(this.value);
        return (state.getAnsiCode() + value);
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