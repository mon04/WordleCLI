import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class WordleCLI {

    private static final int NUM_TURNS = 6;
    private static final int WORD_LENGTH = 5;
    private static final Cell[][] board = new Cell[NUM_TURNS][WORD_LENGTH];
    private static final String wordle = getRandomWord();
    private static boolean hasWon=false;

    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        //System.out.println(wordle+"\n");
        for(int t=1; t <= NUM_TURNS; t++) {
            System.out.printf("Enter guess %d/%d: ", t, NUM_TURNS);
            String guess = scan.nextLine().toUpperCase();
            Cell[] row = new Cell[guess.length()];
            for(int i=0; i < WORD_LENGTH; i++)
                row[i] = new Cell(guess.charAt(i));
            evaluate(row);
            board[t-1] = row;
            printBoard();
            if(allGreen(row)) {
                hasWon=true;
                break;
            }
        }
        if(hasWon)
            System.out.println(Ansi.GREEN+"CONGRATULATIONS! YOU WIN."+Ansi.RESET);
        else
            System.out.println(Ansi.RED+"SORRY! YOU LOSE."+Ansi.RESET);
        System.out.print("THE WORD WAS: "+Ansi.YELLOW+"\""+wordle+"\""+Ansi.RESET+".");
    }

    public static void evaluate(Cell[] guess) {
        char[] wordleCopy = wordle.toCharArray();
        char[] guessCopy = new char[WORD_LENGTH];
        for(int i=0; i < WORD_LENGTH; i++)
            guessCopy[i] = guess[i].getValue();

        //Check greens
        for(int i=0; i < WORD_LENGTH; i++) {
            Cell cell = guess[i];
            if (guessCopy[i] != '_' && guessCopy[i] == wordleCopy[i]) {
                guessCopy[i] = '_';
                wordleCopy[i] = '_';
                cell.setState(State.GREEN);
            }
        }

        //Check yellows
        for(int i=0; i < WORD_LENGTH; i++) {
            Cell cell = guess[i];
            State cellState = cell.getState();
            if (cellState != State.GREEN && guessCopy[i] != '_' && yellowCheck(guessCopy, wordleCopy, i))
                cell.setState(State.YELLOW);
        }

        //Check grays
        for(int i=0; i < WORD_LENGTH; i++) {
            Cell cell = guess[i];
            State cellState = cell.getState();
            if(cellState != State.GREEN && cellState != State.YELLOW)
                cell.setState(State.GRAY);
        }
    }

    private static boolean yellowCheck(char[] guess, char[] soln, int i) {
        char gc = guess[i];
        if(gc == '_')
            return false;
        for(int j=0; j < WORD_LENGTH; j++) {
            if (i != j && gc == soln[j]) {
                soln[j] = '_';
                return true;
            }
        }
        return false;
    }

    private static boolean allGreen(Cell[] row) {
        for(Cell cell: row) {
            if(cell.getState() != State.GREEN)
                return false;
        }
        return true;
    }

    public static void printBoard() {
        System.out.print(Ansi.BLACK);
        System.out.println("+ - - - - - +");
        for(Cell[] row: board) {
            System.out.print("| ");
            for(Cell cell: row) {
                if(cell == null)
                    System.out.print("  ");
                else
                    System.out.print(cell+Ansi.BLACK.toString()+" ");
            }
            System.out.println("|");
        }
        System.out.println("+ - - - - - +");
        System.out.println(Ansi.RESET);
    }

    public static String getRandomWord() {
        try {
            Scanner scan = new Scanner(new File("src/wordles.txt"));
            int size=2089;
            int r = (int)(Math.random() * size);
            int i=0;
            while((i++) < r)
                scan.nextLine();
            String word = scan.nextLine();
            scan.close();
            return word.toUpperCase();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}

class Cell {
    private final char value;
    private State state;

    public Cell(char value) {
        this.value = value;
        this.state = null;
    }

    public String toString() {
        if(state == null)
            return " ";
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

enum State {
    GREEN (Ansi.GREEN),
    YELLOW (Ansi.YELLOW),
    GRAY (Ansi.GRAY);

    private final Ansi ansi;

    State(Ansi ansi) {
        this.ansi=ansi;
    }

    public String getAnsiCode() {
        return ansi.toString();
    }
}

enum Ansi {
    GREEN ("\033[38;5;15;48;5;22m"),
    YELLOW ("\033[38;5;15;48;5;94m"),
    GRAY ("\033[37;48;5;234m"),
    BLACK ("\033[37;40m"),
    RED ("\033[38;5;15;48;5;88m"),
    RESET ("\033[0m");

    private final String esc; //Escape code

    Ansi(String esc) {
        this.esc=esc;
    }

    public String toString() {
        return esc;
    }
}