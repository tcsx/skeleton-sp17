package db;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;

import db.Database;

/**
 * Contains main method for this project. This class is provided by CS61B.
 */
public class Main {
    private static final String EXIT = "exit";
    public static final String PROMPT = "> ";

    public static void main(String[] args) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        Database db = new Database();
        System.out.print(PROMPT);

        String line = "";
        while ((line = in.readLine().trim()) != null) {
            if (EXIT.equals(line)) {
                break;
            }

            if (!line.isEmpty()) {
                String result = db.transact(line);
                if (result.length() > 0) {
                    System.out.println(result);
                }
            }
            System.out.print(PROMPT);
        }

        in.close();
    }
}
