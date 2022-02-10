import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class WordleCLI {

    private static final int NUM_TURNS = 6;
    private static final String WORDLE = getRandomWord();
    private static final Row[] rows = new Row[NUM_TURNS];
    private static final String[] VALIDS = getValids();

    public static void main(String[] args) {
        assert WORDLE != null : "Wordle is null";
        Scanner scan = new Scanner(System.in);
        boolean won=false;
        int i=0;

        while(!won && i < NUM_TURNS) {
            System.out.printf("Enter word %d/%d: ", i+1, NUM_TURNS);
            String input = scan.nextLine().toUpperCase();
            while(!isValid(input)) {
                System.out.println("Invalid. Please try again: ");
                System.out.printf("\nEnter word (%d/%d): ", i+1, NUM_TURNS);
                input = scan.nextLine().toUpperCase();
            }
            rows[i] = new Row(input);
            rows[i].evaluateAgainst(WORDLE);
            printRows();
            if(rows[i].equals(WORDLE))
                won=true;
            i++;
        }

        System.out.printf("The wordle was \"%s\"\n", WORDLE);
        if(won)
            System.out.printf("%s You won in %d/%d turns! :) %s\n",Ansi.GREEN, i, NUM_TURNS, Ansi.RESET);
        else
            System.out.printf("%s You lose! :( %s\n\n", Ansi.RED, Ansi.RESET);
    }

    public static boolean isValid(String word) {
        if(!word.matches("[A-Z]{5}"))
            return false;
        for(String v: VALIDS) {
            if(v.equals(word))
                return true;
        }
        return false;
    }

    private static void printRows() {
        StringBuilder sb = new StringBuilder();
        for(Row row: rows) {
            if(row == null) break;
            sb.append(row.visualised());
            sb.append("\n");
        }
        System.out.println(sb.toString());
    }

    public static String[] getValids() {
        try {
            Scanner scan = new Scanner(new File("src/data/valids.txt"));
            String[] out = new String[12972];
            for(int i=0; i < out.length; i++)
                out[i] = scan.nextLine().toUpperCase();
            scan.close();
            return out;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
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
