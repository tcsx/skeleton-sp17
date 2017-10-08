package db;

import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.util.HashMap;

/**
 * This class contains essential methods and fields for parsing the commands that the users pass in. Its framework is provided by CS61B and has been modified and added other parsing methods and fields to help the functioning of the database project.
 */
public class Parser {
    // Various common constructs, simplifies parsing.
    private static final String REST = "\\s*(.*?)\\s*", COMMA = "\\s*,\\s*", AND = "\\s+and\\s+", WHITESPACE = "\\s+";
    // Stage 1 syntax, contains the command name.
    public static final Pattern CREATE_CMD = Pattern.compile("create\\s+table\\s+" + REST),
            LOAD_CMD = Pattern.compile("load " + REST), STORE_CMD = Pattern.compile("store " + REST),
            DROP_CMD = Pattern.compile("drop table " + REST), INSERT_CMD = Pattern.compile("insert into " + REST),
            PRINT_CMD = Pattern.compile("print " + REST), SELECT_CMD = Pattern.compile("select " + REST),
            COLHEAD = Pattern.compile("\\s*([a-zA-Z]+\\w*)\\s+(string|int|float)\\s*"),
            ROW = Pattern.compile("\\s*([^\\r\\n\\t,]+)\\s*(?:,\\s*[^\\r\\n\\t,]+\\s*)*"),
            NAMES = Pattern.compile("[a-zA-Z]+\\w*"), STRING = Pattern.compile("'[^\\r\\n\\t',]*'"),
            FLOAT = Pattern.compile("[\\+\\-]?\\d*\\.\\d*"), INT = Pattern.compile("\\d+"),
            COLEXPR = Pattern.compile("(\\S+)\\s*([+\\-*/])\\s*([^,]+?)\\s+as\\s+([a-zA-Z]+\\w*)"),
            CONDS = Pattern.compile("(\\S+)\\s*((?:<=)|(?:>=)|<|>|(?:==)|(?:!=))\\s*([^,]+)");

    // Stage 2 syntax, contains the clauses of commands.
    public static final Pattern CREATE_NEW = Pattern
            .compile("([a-zA-Z]+\\w*)\\s+\\(\\s*([a-zA-Z]+\\w*\\s+(?:string|int|float)\\s*"
                    + "(?:,\\s*[a-zA-Z]+\\w*\\s+(?:string|int|float)\\s*)*)\\)"),
            SELECT_CLS = Pattern
                    .compile("([^,]+?(?:,[^,]+?)*)\\s+from\\s+" + "(\\S+\\s*(?:,\\s*\\S+\\s*)*)(?:\\s+where\\s+"
                            + "([\\w\\s+\\-*/'<>=!.]+?(?:\\s+and\\s+" + "[\\w\\s+\\-*/'<>=!.]+?)*))?"),
            CREATE_SEL = Pattern.compile("(\\S+)\\s+as\\s+select\\s+" + SELECT_CLS.pattern()),
            INSERT_CLS = Pattern.compile("(\\S+)\\s+values\\s+(.+?" + "(?:\\s*,\\s*.+?)*)\\s*");

    // public static void main(String[] args) {
    //     if (args.length != 1) {
    //         System.err.println("Expected a single query argument");
    //         return;
    //     }

    //     eval(args[0]);
    // }

    /**
     * Parse the type of the command and pass the sentence of the query to 
     * corresponding parse function. 
     * 
     * @param query Command
     * @param db The database that the command is applied to
     */
    public static void eval(String query, Database db) {
        Matcher m;
        if ((m = CREATE_CMD.matcher(query)).matches()) {
            createTable(m.group(1), db);
        } else if ((m = LOAD_CMD.matcher(query)).matches()) {
            loadTable(m.group(1), db);
        } else if ((m = STORE_CMD.matcher(query)).matches()) {
            storeTable(m.group(1), db);
        } else if ((m = DROP_CMD.matcher(query)).matches()) {
            dropTable(m.group(1), db);
        } else if ((m = INSERT_CMD.matcher(query)).matches()) {
            insertRow(m.group(1), db);
        } else if ((m = PRINT_CMD.matcher(query)).matches()) {
            printTable(m.group(1), db);
        } else if ((m = SELECT_CMD.matcher(query)).matches()) {
            select(m.group(1), db);
        } else {
            System.err.printf("ERROR: MALFORMED QUERY: %s\r\n", query);
        }
    }

    /**
     * Parse table name, its column information (name and type) and select 
     * clause and pass them to the next parse function.
     * @param expr Expression for creating a table
     * @param db The database that the command is applied to
     */
    private static void createTable(String expr, Database db) {
        Matcher m;
        if ((m = CREATE_NEW.matcher(expr)).matches()) {
            createNewTable(m.group(1), m.group(2).trim().split(COMMA), db);
        } else if ((m = CREATE_SEL.matcher(expr)).matches()) {
            createSelectedTable(m.group(1), m.group(2), m.group(3), m.group(4), db);
        } else {
            System.err.printf("ERROR: MALFORMED CREATE: %s\r\n", expr);
        }
    }

    /**
     * Create a new table. This method is used when there is not select clause in the creation command sentence.
     * @param name Name of the new table
     * @param cols An array of string. Each item contains the name and type of a column.
     * @param db The database where the new table will be
     */
    private static void createNewTable(String name, String[] cols, Database db) {
        String[] colInfo = new String[cols.length * 2];
        for (int i = 0; i < colInfo.length; i += 2) {
            //Get each column's name and type.
            String[] temp = cols[i / 2].split(WHITESPACE);
            if (temp.length != 2) {
                System.err.println("ERROR: INCORRECT COLUMN INFORMATION: " + cols[i / 2]);
                System.out.print(Main.PROMPT);
                return;
            } else {
                colInfo[i] = temp[0];
                colInfo[i + 1] = temp[1];
            }
        }
        db.createTable(name, colInfo);
    }

    /**
     * Create a table by using select clause.
     * @param name Table name
     * @param exprs Column expressions
     * @param tables Table names
     * @param conds Condition statements
     * @param database The database which contains the tables
     */
    private static void createSelectedTable(String name, String exprs, String tables, String conds, Database db) {
        Table tb = select(exprs, tables, conds, db);
        if (tb == null)
            return;
        db.addTable(name, tb);
    }

    /**
     * Load certain table from a tbl file to the database
     * @param name Name of the table
     * @param db The database
     */
    private static void loadTable(String name, Database db) {
        db.loadTable(name);
    }

    /**
    * Store table to file.
    * @param name Table name.
    * @param db The database which contains the table
    */
    private static void storeTable(String name, Database db) {
        db.storeTable(name);
    }

    /**
    * Drop certain table from the database.
    * @param name Table name
    * @param db The database which contains the table
    */
    private static void dropTable(String name, Database db) {
        db.storeTable(name);
    }

    /**
    * Insert a row into a table.
    * @param expr The command line which contains table name and data of row
    * @param db The database which contains the table
    */
    private static void insertRow(String expr, Database db) {
        Matcher m = INSERT_CLS.matcher(expr);
        if (!m.matches()) {
            System.err.printf("ERROR: MALFORMED INSERT: %s\r\n", expr);
            return;
        }
        String name = m.group(1);
        String[] row = m.group(2).split(COMMA);
        db.insertRowInto(name, row);
    }

    /**
    * Print certain table
    * @param name Table name.
    * @param db The database which contains the table
    */
    private static void printTable(String name, Database db) {
        db.printTable(name);
    }

    /**
     * This method performs the select command. It will print the table generated by 
     * select clause or print error information if error happens.
     * @param expr Select clause
     * @param db The database to be operated
     */
    private static void select(String expr, Database db) {
        Matcher m = SELECT_CLS.matcher(expr);
        if (!m.matches()) {
            System.err.printf("ERROR: MALFORMED SELECT: %s\r\n", expr);
            return;
        }
        Table tb = select(m.group(1), m.group(2), m.group(3), db);
        if (tb == null)
            return;
        System.out.println(tb);
    }

    /**
    * This method performs the select command. It will return the table generated 
    *  by select clause or print error information and return null if error happens.
    * @param colExprs Column expressions in the select clause
    * @param tables Table names in the select clause
    * @param conditions Condition statements in the select clause
    * @param db The database to be operated
    * @return The table generated by select clause or null if error happens
    */
    private static Table select(String colExprs, String tables, String conditions, Database db) {
        String[] tbStrings = tables.split(COMMA);
        Table[] tbs = new Table[tbStrings.length];

        for (int i = 0; i < tbs.length; i++) {
            String tb = tbStrings[i];
            if (!db.getTables().containsKey(tb)) {
                Database.printNotExist(tb);
                return null;
            }
            tbs[i] = db.getTables().get(tb);
        }

        String[] exprs = colExprs.split(COMMA, 8);
        if (conditions == null)
            return Select.select(exprs, null, tbs);
        String[] conds = conditions.split(AND);
        return Select.select(exprs, conds, tbs);
    }

    /**
     * Parse the column information expression containing column names and types.
     * @param expr Column information expression containing column names and types
     * @return An array of its column names and types. The name of first column is  
     *         at index 0 and its type is at index 1, and so on.
     * @throws IllegalArgumentException if the column information expression is incorrect
     */
    public static String[] parseColInfo(String expr) throws IllegalArgumentException {
        String[] colHead = expr.split(COMMA);
        String[] colInfo = new String[2 * colHead.length];
        HashMap<String, Integer> colNames = new HashMap<String, Integer>();
        Matcher m;
        for (int i = 0; i < colInfo.length; i += 2) {
            if ((m = COLHEAD.matcher(colHead[i / 2])).matches()) {
                String name = m.group(1);
                if (colNames.containsKey(name)) {//If there are duplicate column names.
                    throw new IllegalArgumentException("DUPLICATE COLUMNS.");
                }
                colInfo[i] = name;
                colInfo[i + 1] = m.group(2);
            } else {
                throw new IllegalArgumentException("INCORRECT COLUMN HEADS.");
            }
        }
        return colInfo;
    }

    /**
     * This method is used to parse row expression. If the format of the row is 
     * correct, it will return an array of string containing the data of the row. 
     * Otherwise, it will return null and throw exception.
     * @param expr Row expression
     * @return An array of string containing the data of the row
     * @throws IllegalArgumentException if the format of the row expression is incorrect
     */
    public static String[] parseRow(String expr) throws IllegalArgumentException {
        Matcher m;
        String[] row;
        if ((m = ROW.matcher(expr)).matches()) {
            row = expr.split(",");
            for (String r : row) {
                r = r.trim();
            }
        } else {
            throw new IllegalArgumentException("ILLEGAL ROW EXPRESSION.");
        }
        return row;
    }

    /**
     * Parse the type of data.
     * @param type Data passed in as string
     * @return Type of the data.
     */
    public static String parseType(String type) {
        Matcher m;
        if (type.equals("NOVALUE")) {
            return "NOVALUE";
        }
        if (type.equals("NaN")) {
            return "NaN";
        }
        if ((m = STRING.matcher(type)).matches()) {
            return "string";
        }
        if ((m = FLOAT.matcher(type)).matches()) {
            return "float";
        }
        if ((m = INT.matcher(type)).matches()) {
            return "int";
        }
        System.err.printf("ERROR: ILLEGAL DATA TYPE: %s.\r\n", type);
        return null;
    }

}
