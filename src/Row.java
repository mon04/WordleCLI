/**
 * An object to represent a row (or a guess) in the Wordle game
 */
public class Row {
    public final int length;
    private final Cell[] cells;
    private boolean isEvaluated;

    public Row(String cellValues) {
        length=cellValues.length();
        cells = new Cell[length];
        for(int i=0; i < length; i++)
            cells[i] = new Cell(cellValues.charAt(i));
    }

    public char[] toCharArray() {
        char[] out = new char[length];
        for(int i=0; i < length; i++)
            out[i] = cells[i].getValue();
        return out;
    }

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
        }
        return sb.toString();
    }

    public boolean equals(String wordle) {
        return this.toString().equals(wordle);
    }

    public boolean isEvaluated() {
        return isEvaluated();
    }

    public void evaluateAgainst(String wordle) {
        char[] rowTemp = this.toCharArray();
        char[] wordleTemp = wordle.toCharArray();
        for(int i=0; i < length; i++) { // Determine greens
            Cell cell = cells[i];
            if (rowTemp[i] != '_' && rowTemp[i] == wordleTemp[i]) {
                //rowTemp[i] = '_';
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
            if(cell.getState() == null)
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
