package db;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Database {
    private Map<String, Table> tables;

    public Database() {
        tables = new HashMap<String, Table>();
    }

    public Map<String, Table> getTables() {
        return tables;
    }

    /**
     * Add a table into the database.
     * @param name Name of the table
     * @param tb Table to be added
     */
    public void addTable(String name, Table tb) {
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
    public void createTable(String name, String[] colInfo) throws Exception{
        if (tables.containsKey(name)) {
            throw new Exception("ERROR: TABLE ALREADY EXISTS.");
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
    public void createTable(String name, List<String> colInfo) throws Exception{
        String[] cols = colInfo.toArray(new String[0]);
        createTable(name, cols);
    }

    /**
     * Get a table by its name.
     */
    public Table getTable(String name) {
        return tables.get(name);
    }

    /**
     * Load certain table from a tbl file to the database
     * @param name Name of the table
     */
    public void loadTable(String name) throws Exception{
        try {
            BufferedReader in = new BufferedReader(new FileReader(name + Table.TBL));
            try {
                String s = in.readLine();
                String[] colInfo = Parser.parseColInfo(s);
                Table table = new Table(colInfo);
                String rowExper;
                while ((rowExper = in.readLine()) != null) {
                    String[] row = Parser.parseRow(rowExper);
                    table.insertRow(row, true);
                }
                tables.put(name, table);
            } catch (IOException e) {
                System.err.println("ERROR: PARSE FILE %s FAILED." + e.getMessage());
            } catch(Exception e){
                throw e;
            }finally {
                try {
                    in.close();
                } catch (Exception e) {
                }
            }
        } catch (FileNotFoundException e) {
            throw new FileNotFoundException(String.format("ERROR: FILE NAMED %s NOT FOUND.", name));
        }
    }

    /**
     * Insert a row into a table.
     * @param name The name of the table
     * @param row The row to be inserted
     */
    public void insertRowInto(String name, String[] row) throws Exception{
        if (!tables.containsKey(name)) {
            throw new TableNotExistException(name);
        }
        getTable(name).insertRow(row, false);
    }


    /**
     * Print table
     * @param name Table name.
     * @return the string representation of the table
     */
    public String printTable(String name) throws Exception{
        if (!tables.containsKey(name)) {
            throw new TableNotExistException(name);
        }
        return getTable(name).toString();
    }

    /**
     * Store table to file.
     * @param name Table name.
     */
    public void storeTable(String name) throws Exception{
        if (!tables.containsKey(name)) {
            throw new TableNotExistException(name);
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
            throw e;
        } finally {
            try {
                fout.close();
            } catch (Exception e) {
            }
        }
    }

    /**
    * Drop certain table from the database.
    * @param name Table name.
    */
    public void dropTable(String name) throws Exception{
        if (!tables.containsKey(name)) {
            throw new TableNotExistException(name);
        }
        tables.remove(name);
    }

    /**
    * Transact function of the database
    * @param query Command passed to this function
    * @return String resulted by the command
    */
    public String transact(String query) {
        return Parser.eval(query, this);
    }
}
