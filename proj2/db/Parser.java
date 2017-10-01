package db;

import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.util.HashMap;
import java.util.StringJoiner;

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
            NAMES = Pattern.compile("[a-zA-Z]+\\w*"),
            STRING = Pattern.compile("'[^\\r\\n\\t',]*'"), FLOAT = Pattern.compile("[\\+\\-]?\\d*\\.\\d*"),
            INT = Pattern.compile("\\d+"),
            COLEXPR = Pattern.compile("[a-zA-Z]+\\w*\\s*([+\\-*/]\\s+\\S+\\s+as\\s+[a-zA-Z]+\\w*)?");
    // Stage 2 syntax, contains the clauses of commands.
    public static final Pattern CREATE_NEW = Pattern
            .compile("([a-zA-Z]+\\w*)\\s+\\(\\s*([a-zA-Z]+\\w*\\s+(?:string|int|float)\\s*" + "(?:,\\s*[a-zA-Z]+\\w*\\s+(?:string|int|float)\\s*)*)\\)"),
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
     * Parse the type of the query and pass the sentence of the query to 
     * corresponding parse function. 
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
            System.err.printf("ERROR: MALFORMED QUERY: %s\n", query);
        }
    }

    /**
     * Parse table name, its column information (name and type) and select 
     * clause and pass them to the next parse function.
     */
    private static void createTable(String expr, Database db) {
        Matcher m;
        if ((m = CREATE_NEW.matcher(expr)).matches()) {
            createNewTable(m.group(1), m.group(2).trim().split(COMMA), db);
        } else if ((m = CREATE_SEL.matcher(expr)).matches()) {
            createSelectedTable(m.group(1), m.group(2), m.group(3), m.group(4), db);
        } else {
            System.err.printf("ERROR: MALFORMED CREATE: %s\n", expr);
        }
    }

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

    private static void createSelectedTable(String name, String exprs, String tables, String conds, Database db) {
        db.addTable(name, select(exprs, tables, conds, db));
    }

    private static void loadTable(String name, Database db) {
        db.loadTable(name);
    }

    private static void storeTable(String name, Database db) {
        db.storeTable(name);
    }

    private static void dropTable(String name, Database db) {
        db.storeTable(name);
    }

    private static void insertRow(String expr, Database db) {
        Matcher m = INSERT_CLS.matcher(expr);
        if (!m.matches()) {
            System.err.printf("ERROR: MALFORMED INSERT: %s\n", expr);
            return;
        }
        String name = m.group(1);
        String[] row = m.group(2).split(COMMA);
        db.insertRowInto(name, row);
    }

    private static void printTable(String name, Database db) {
        db.printTable(name);
    }

    private static void select(String expr, Database db) {
        Matcher m = SELECT_CLS.matcher(expr);
        if (!m.matches()) {
            System.err.printf("ERROR: MALFORMED SELECT: %s\n", expr);
            return;
        }
        Table tb = select(m.group(1), m.group(2), m.group(3), db);
        System.out.println(tb);
    }

    private static Table select(String exprs, String tables, String conds, Database db) {
        System.out.printf(
                "You are trying to select these expressions:"
                        + " '%s' from the join of these tables: '%s', filtered by these conditions: '%s'\n",
                exprs, tables, conds);
                return null;
    }

    /**
     * Parse the column information expression containing column names and types.
     * @param expr Column information expression containing column names and types
     * @return An array of its column names and types. The name of first column is  
     *         at index 0 and its type is at index 1, and so on.
     */
    public static String[] parseColInfo(String expr) throws IllegalArgumentException {
        String[] colHead = expr.split(COMMA);
        String[] colInfo = new String[2 * colHead.length];
        HashMap<String, Integer> colNames = new HashMap<String, Integer>();
        Matcher m;
        for (int i = 0; i < colInfo.length; i += 2) {
            if ((m = COLHEAD.matcher(colHead[i / 2])).matches()) {
                String name = m.group(1);
                if (colNames.put(name, 1) != null) { //If there are duplicate column names.
                    throw new IllegalArgumentException();
                }
                colInfo[i] = name;
                colInfo[i + 1] = m.group(2);
            } else {
                throw new IllegalArgumentException();
            }
        }
        return colInfo;
    }

    public static String[] parseRow(String expr) throws IllegalArgumentException {
        Matcher m;
        String[] row;
        if ((m = ROW.matcher(expr)).matches()) {
            row = expr.split(",");
            for (String r : row) {
                r = r.trim();
            }
        } else {
            throw new IllegalArgumentException("ERROR: ILLEGAL ROW EXPRESSION.");
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
