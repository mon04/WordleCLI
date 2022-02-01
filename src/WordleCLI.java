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
        System.out.println();
        //System.out.println(wordle+"\n");
        int t=1;
        for(; t <= NUM_TURNS; t++) {
            System.out.printf("Enter guess %d/%d: ", t, NUM_TURNS);
            String guess = scan.nextLine().toUpperCase();
            while (!guess.matches("[A-Z]{5}")) {
                System.out.println("Error! Your guess must be a five-letter word.");
                System.out.printf("Enter guess %d/%d: ", t, NUM_TURNS);
                guess = scan.nextLine().toUpperCase();
            }
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
            System.out.printf("%sYou guessed the Wordle! (%d/%d guesses)%s\n", Ansi.GREEN, t, NUM_TURNS, Ansi.RESET);
        else
            System.out.printf("%sYou have not managed to guess the Wordle! (%d/%d guesses)%s\n", Ansi.RED, t, NUM_TURNS, Ansi.RESET);
        System.out.printf("THE WORD WAS: \"%s\"", wordle);
        System.out.println();
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
        System.out.print(Ansi.RESET);
        System.out.println("+ - - - - - +");
        for(Cell[] row: board) {
            System.out.print("| ");
            for(Cell cell: row) {
                if(cell == null)
                    System.out.print("  ");
                else
                    System.out.print(cell+(Ansi.RESET+" "));
            }
            System.out.println("|");
        }
        System.out.println("+ - - - - - +");
        System.out.println(Ansi.RESET);
    }

    public static String getRandomWord() {
        try {
            Scanner scan = new Scanner(new File("src/data/wordles.txt"));
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