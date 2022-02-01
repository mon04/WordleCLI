import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class WordleCLI {

    private static final int NUM_TURNS = 6;
    private static final String WORDLE = getRandomWord();
    private static final Row[] rows = new Row[NUM_TURNS];

    public static void main(String[] args) {
        //System.out.println(WORDLE);
        Scanner scan = new Scanner(System.in);
        boolean won=false;
        int i=0;
        while(!won && i < NUM_TURNS) {
            System.out.printf("Enter word %d/%d: ", i+1, NUM_TURNS);
            String input = scan.nextLine().toUpperCase();
            while(!input.matches("[A-Z]{5}")) {
                System.out.print("Invalid. Please try again: ");
                input = scan.nextLine().toUpperCase();
            }
            rows[i] = new Row(input);
            assert WORDLE != null : "Wordle is null";
            rows[i].evaluate(WORDLE);
            printRows();
            if(rows[i].equals(WORDLE))
                won=true;
            i++;
        }
        if(won)
            System.out.printf("You won in %d/%d turns! :)\n", i, NUM_TURNS);
        else
            System.out.println("You lose! :(");
        System.out.printf("The wordle was \"%s\"", WORDLE);
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
