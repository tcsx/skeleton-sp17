package db;

import edu.princeton.cs.introcs.In;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Database {
    Map<String, Table> tables;

    public Database() {
        tables = new HashMap<String, Table>();
    }

    public void addTable(String name, Table tb){
        tables.put(name, tb);
    }
    
    /**
     * Create and add a table to the database.
     * @param name
     * 		  Name of the table.
     * @param colInfo
     * 		  An array of its column names and types. The name of first column is 
     *        at index 0 and its type is at index 1, and so on.
     */
    public void createTable(String name, String[] colInfo) {
        if (tables.containsKey(name)) {
            System.err.println("ERROR: TABLE ALREADY EXISTS.");
            return;
        }
        tables.put(name, new Table(colInfo));
    }

    /**
     * Create and add a table to the database.
     * @param name
     * 		  Name of the table.
     * @param colInfo
     * 		  A List of its column names and types. The name of first column is 
     *        at index 0 and its type is at index 1, and so on.
     */
    public void createTable(String name, List<String> colInfo) {
        String[] cols = colInfo.toArray(new String[0]);
        createTable(name, cols);
    }

    /**
     * Get a table by its name.
     */
    public Table getTable(String name) {
        return tables.get(name);
    }

    public void loadTable(String name) {
        try {
            BufferedReader in = new BufferedReader(new FileReader(name + Table.TBL));
            try {
                String s = in.readLine();
                String[] colInfo = Parser.parseColInfo(s);
                Table table = new Table(colInfo);
                String rowExper;
                while ((rowExper = in.readLine()) != null) {
                    String[] row = Parser.parseRow(rowExper);
                    if (!table.insertRow(row, true))
                        return;
                }
                tables.put(name, table);
            } catch (Exception e) {
                System.err.printf("ERROR: PARSE FILE %s FAILED. INCORRECT TABLE FORMAT.\r\n", name);
                return;
            } finally {
                try {
                    in.close();
                } catch (Exception e) {
                }
            }
        } catch (FileNotFoundException e) {
            System.err.printf("ERROR: FILE NAMED %s NOT FOUND.\r\n", name);
            return;
        }
    }

    public void insertRowInto(String name, String[] row) {
    	if (!tables.containsKey(name)) {
			printNotExist(name);
			return;
		}
        getTable(name).insertRow(row, false);
    }

    /**
     * Print error message if the table doesn't exist.
     * @param name Table name;
     */
    public static void printNotExist(String name) {
        System.err.printf("ERROR: THE TABLE NAMED %s DOESN'T EXIST.\r\n", name);
        return;
    }

    /**
     * Print table
     * @param name Table name.
     */
    public void printTable(String name) {
        if (!tables.containsKey(name)) {
            printNotExist(name);
            return;
        }
        System.out.println(getTable(name));
    }

    /**
     * Store table to file.
     * @param name Table name.
     */
    public void storeTable(String name) {
        if (!tables.containsKey(name)) {
            printNotExist(name);
            return;
        }
        File file = new File(name + Table.TBL);
        BufferedWriter fout = null;
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
            fout = new BufferedWriter(new FileWriter(file));
            fout.write(this.getTable(name).toString(false));
            fout.flush();
        } catch (Exception e) {
            System.err.println("ERROR: STORE FILE FAILED.");
        } finally {
            try {
                fout.close();
            } catch (Exception e) {
            }
        }
    }

    public void dropTable(String name) {
        if (!tables.containsKey(name)) {
            printNotExist(name);
            return;
        }
        tables.remove(name);
    }



    // public Table selectTable(String colExpr[], String table[], String cond[]){

    // }

        /**
     * Transact function of the database
     * @param query Command passed to this function
     * @return String message as a result of the executed command
     */
    public static void transact(String query, Database db) {
        Parser.eval(query, db);
    }
}
