package workshop05code;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.Scanner;
//Included for the logging exercise
import java.io.FileInputStream;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

/**
 *
 * @author sqlitetutorial.net
 */
public class App {
    // Start code for logging exercise
    static {
        // must set before the Logger
        // loads logging.properties from the classpath
        try {// resources\logging.properties
            LogManager.getLogManager().readConfiguration(new FileInputStream("resources/logging.properties"));
        } catch (SecurityException | IOException e1) {
            e1.printStackTrace();
        }
    }

    private static final Logger logger = Logger.getLogger(App.class.getName());
    // End code for logging exercise
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        SQLiteConnectionManager wordleDatabaseConnection = new SQLiteConnectionManager("words.db");

        wordleDatabaseConnection.createNewDatabase("words.db");
        if (wordleDatabaseConnection.checkIfConnectionDefined()) {
            logger.log(Level.INFO, "Wordle created and connected.");
            System.out.println("Wordle created and connected.");
        } else {
            logger.log(Level.SEVERE, "Not able to connect. Sorry!");
            System.out.println("Not able to connect. Sorry!");
            return;
        }
        if (wordleDatabaseConnection.createWordleTables()) {
            logger.log(Level.INFO, "Wordle structures in place.");
            System.out.println("Wordle structures in place.");
        } else {
            logger.log(Level.SEVERE,"Not able to launch. Sorry!");
            System.out.println("Not able to launch. Sorry!");
            return;
        }

        // let's add some words to valid 4 letter words from the data.txt file

        try (BufferedReader br = new BufferedReader(new FileReader("resources/data.txt"))) {
            String line;
            int i = 1;
            while ((line = br.readLine()) != null) {
                if (isValidWord(line)) {
                    wordleDatabaseConnection.addValidWord(i, line);
                    logger.log(Level.INFO, "Added valid word from data.txt: " + line);
                    System.out.println("Added valid word from data.txt: " + line);
                } else {
                    logger.log(Level.SEVERE, "Invalid word from data.txt: " + line);
                    System.out.println("Invalid word from data.txt: " + line);
                }
                i++;
            }

        } catch (IOException e) {
            logger.log(Level.SEVERE, "Not able to load . Sorry!");
            logger.log(Level.SEVERE, e.getMessage());
            System.out.println("Not able to load . Sorry!");
            return;
        }

        // let's get them to enter a word

        try (Scanner scanner = new Scanner(System.in)) {
            System.out.print("Enter a 4 letter word for a guess or q to quit: ");
            String guess = scanner.nextLine();

            while (!guess.equals("q")) {
                if (isValidInput(guess)) {
                    logger.log(Level.INFO, "You've guessed '" + guess + "'.");
                    System.out.println("You've guessed '" + guess + "'.");

                    if (wordleDatabaseConnection.isValidWord(guess)) { 
                        logger.log(Level.INFO, "Success! It is in the the list.\n");
                        System.out.println("Success! It is in the the list.\n");
                    }
                    else{
                        logger.log(Level.INFO, "Sorry. This word is NOT in the the list.\n");
                        System.out.println("Sorry. This word is NOT in the the list.\n");
                    }
                }
                else {
                    logger.log(Level.INFO, "Invalid Input. Ignoring unacceptable input.");
                    System.out.println("Invalid Input. Ignoring unacceptable input.");
                }

                System.out.print("Enter a 4 letter word for a guess or q to quit: " );
                guess = scanner.nextLine();
            }
        } catch (NoSuchElementException | IllegalStateException e) {
            logger.log(Level.WARNING, "Error: ", e);
            e.printStackTrace();
        }

    }

    private static boolean isValidWord(String line) {
        // TODO Auto-generated method stub
        return line.matches("[a-z]{4}");
    }

    private static boolean isValidInput(String input) {
        // TODO Auto-generated method stub
        return input.matches("[a-z]{4}");
    }
}