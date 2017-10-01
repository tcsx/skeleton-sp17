package db;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;

import db.Database;

/**
 * Contains main method for this project. This class is provided by CS61B.
 */
public class Main {
    private static final String EXIT   = "exit";
    public static final String PROMPT = "> ";

    public static void main(String[] args) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        Database db = new Database();
        System.out.print(PROMPT);

        String line = "";
        while ((line = in.readLine()) != null) {
            if (EXIT.equals(line)) {
                break;
            }

            if (!line.trim().isEmpty()) {
                Database.transact(line, db);
            }
            System.out.print(PROMPT);
        }

        in.close();
    }

    // public static void printPrompt(){
    //     System.out.print(PROMPT);
    // }
}
