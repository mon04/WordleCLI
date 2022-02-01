/**
 * An object to represent a row (or a guess) in the Wordle game
 */
public class Row {
    public final int length;
    private final Cell[] cells;
    private boolean isEvaluated;

    /**
     * @param cellValues A string of the letter values for each cell
     */
    public Row(String cellValues) {
        length=cellValues.length();
        cells = new Cell[length];
        for(int i=0; i < length; i++)
            cells[i] = new Cell(cellValues.charAt(i));
    }

    /**
     * @return a char array of the letters from each cell
     */
    public char[] toCharArray() {
        char[] out = new char[length];
        for(int i=0; i < length; i++)
            out[i] = cells[i].getValue();
        return out;
    }

    /**
     * @return each cell's letter concatenated in a string
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for(char c: this.toCharArray())
            sb.append(c);
        return sb.toString();
    }

    public String visualised() {
        StringBuilder sb = new StringBuilder();
        sb.append(Ansi.RESET);
        for(Cell cell: cells) {
            sb.append(cell.visualised());
            sb.append(Ansi.RESET);
            sb.append(" ");
        }
        sb.append("\b"); //remove trailing space
        return sb.toString();
    }

    /**
     * Determines whether the letters in this row form the wordle
     * @param wordle the solution word to be compared against
     * @return true/false whether or not this row's word matches the wordle
     */
    public boolean equals(String wordle) {
        return this.toString().equals(wordle);
    }

    /**
     *
     * @return whether or not this row has yet been evaluated
     */
    public boolean isEvaluated() {
        return isEvaluated();
    }

    /**
     * Set the state (green, yellow, gray) of each cell in the row.
     *
     * First it finds all green cells, then yellows, and then the rest
     * are set as gray.
     *
     * @param wordle the answer against which to evaluate
     */
    public void evaluate(String wordle) {
        char[] rowTemp = this.toCharArray();
        char[] wordleTemp = wordle.toCharArray();
        for(int i=0; i < length; i++) { // Determine greens
            Cell cell = cells[i];
            if (rowTemp[i] != '_' && rowTemp[i] == wordleTemp[i]) {
                rowTemp[i] = '_';
                wordleTemp[i] = '_';
                cell.setState(State.GREEN);
            }
        }
        for(int i=0; i < length; i++) { // Determine yellows
            Cell cell = cells[i];
            if (cell.getState() != State.GREEN && rowTemp[i] != '_' && yellowCell(rowTemp, wordleTemp, i))
                cell.setState(State.YELLOW);
        }
        for(int i=0; i < length; i++) { // Determine grays
            Cell cell = cells[i];
            if(cell.getState() != State.GREEN && cell.getState() != State.YELLOW)
                cell.setState(State.GRAY);
        }
        isEvaluated=true;
    }

    private boolean yellowCell(char[] rowTemp, char[] wordleTemp, int i) {
        char c = rowTemp[i];
        if(c == '_') return false;
        for(int j=0; j < length; j++) {
            if (i != j && c == wordleTemp[j]) {
                wordleTemp[j] = '_';
                return true;
            }
        }
        return false;
    }
}
