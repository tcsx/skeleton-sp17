package db;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.StringJoiner;

/**
 * Table in Database
 */
public class Table {
    /**
    * Column in a Table. Contains type and data of columns.
    */
    public class Column extends ArrayList<String> {
		private static final long serialVersionUID = -4837083264856072491L;
		private String type;

        /**
         * Construct a column with its name and type.
         * @param type Type of the column.
         */
        public Column(String type) {
            this.type = type;
        }

        /**
         * Check if the passed in string matches the type of this column.
         * @param t Type passed in as String
         * @param allowSpecialValue True when special value "NaN" and "NOVALUE" are allowed
         * @return True if passed in string is compatible with column type
         */
        public boolean checkType(String t, boolean allowSpecialValue) {
            String result = Parser.parseType(t);
            if (result == null) {
                return false;
            }
            if (result.equals("NOVALUE")) {
                if (allowSpecialValue == true)
                    return true;
                return false;
            }
            if (result.equals("NaN")) {
                if (allowSpecialValue == false)
                    return false;
                if (type.equals("string")) {
                    return false;
                }
                return true;
            }
            if (!type.equals(result)) {
                return false;
            }
            return true;
        }

        public String getType() {
            return type;
        }

    }

    private HashMap<String, Column> cols;
    private List<String> colNameSeq; //Record the sequence of column names
    public static final String TBL = ".tbl";

    /**
     * Constructs a Table with its column infornation.
     * @param colInfo 
     *        An array of its column names and types. The name of first column is  
     *        at index 0 and its type is at index 1, and so on.
     */
    public Table(String[] colInfo) {
        this.cols = new HashMap<String, Column>();
        colNameSeq = new LinkedList<String>();
        for (int i = 0; i < colInfo.length; i += 2) {
            this.cols.put(colInfo[i], new Column(colInfo[i + 1]));
            colNameSeq.add(colInfo[i]);
        }

    }

    /** Constructs a Table with its column information.
    * @param colInfo 
    *        A List of its column names and types. The name of first column is  
    *        at index 0 and its type is at index 1, and so on.
    */
    public Table(List<String> colInfo) {
        this.cols = new HashMap<String, Column>();
        colNameSeq = new LinkedList<String>();
        Iterator<String> iterator = colInfo.iterator();
        while (iterator.hasNext()) {
            String name = iterator.next();
            String type = iterator.next();
            this.cols.put(name, new Column(type));
            colNameSeq.add(name);
        }
    }

    /**
     * Construct a table whose column names and their types are the same as another table.
     * @param tb Another table
     */
    public Table(Table tb) {
        colNameSeq = tb.getColNameSeq();
        cols = new HashMap<String, Column>();
        for (String name : colNameSeq) {
            cols.put(name, new Column(tb.getType(name)));
        }
    }

    public Table() {
        cols = new HashMap<String, Column>();
        colNameSeq = new LinkedList<String>();
    }

    public List<String> getColNameSeq() {
        return colNameSeq;
    }

    public String getType(String colName) {
        return getColumn(colName).type;
    }

    /**
     *  Get a column by column name.
     * @param name Column name.
     * @return Returns the column if it exists and null if
     * it doesn't.
     */
    public Column getColumn(String colName) {
        if (cols.containsKey(colName)) {
            return cols.get(colName);
        }
        return null;
    }

    /**
     * Get a column by its index.
     * @return Column i. 
     */
    public Column getColumn(int i) {
        return cols.get(colNameSeq.get(i));
    }

    public HashMap<String, Column> getCols() {
        return cols;
    }

    /**
     * Get the name of a column by its index.
     */
    public String getColName(int i) {
        return colNameSeq.get(i);
    }

    /**
     * Get item by column name and row index.
     * @param colName Column name.
     * @param i Row index.
     * @return The specified item.
     */
    public String getItem(String colName, int i) {
        return this.getColumn(colName).get(i);
    }

    /**
     * Add a column into a table
     * @param name Name of the column
     * @param col Column to be added
     */
    public void addCol(String name, Column col) {
        cols.put(name, col);
        colNameSeq.add(name);
    }

    /**
     * Check if this table contains certain column by checking column name
     * @param name Column name needs to be checked
     * @return True if and only if this table contains the column
     */
    public boolean contains(String colName) {
        return cols.containsKey(colName);
    }

    /**
     * @return The number of columns in this table.
     */
    public int colNum() {
        return cols.size();
    }

    /**
     * Return the number of rows in this table.
     */
    public int rowNum() {
        return getColumn(0).size();
    }

    /**
     * Return the String representation of row i.
     * @param i Index of row
     * @param round If round is true, float numbers will be specified to 3 decimal places
     * @return String representation of this row
     */
    public String getRow(int i, boolean round) {
        StringJoiner jointer = new StringJoiner(",");
        DecimalFormat df = new DecimalFormat("#.000");
        for (String s : colNameSeq) {
            String item = getItem(s, i);
            if (round == true) {
                if (getType(s).equals("float")) {
                    if (!"NaN".equals(item) && !"NOVALUE".equals(item)) {
                        double d = Double.parseDouble(item);
                        item = df.format(d);
                    }

                }
            }
            jointer.add(item);
        }
        return jointer.toString();
    }

    /**
     * Get items in row i in an string array.
     * @param i Index of row.
     * @return An array of row items.
     */
    public String[] getRow(int i) {
        String[] row = new String[this.colNum()];
        int j = 0;
        for (String s : colNameSeq) {
            row[j++] = getItem(s, i);
        }
        return row;
    }

    /**
     * Check if the row expression matches the types of this table.
     * @param row Row expression passed to this method.
     * @return True if and only if row expression matches the types of this table.
     */
    public void checkRowType(String[] row, boolean allowSpecialValue) throws IllegalArgumentException {
        if (row.length == colNum()) {
            for (int i = 0; i < row.length; i++) {
                Column col = getColumn(i);
                if (!col.checkType(row[i], allowSpecialValue)) {
                    throw new IllegalArgumentException(String.format("ERROR: DATA %s DOES NOT MATCH COLUMN TYPE.", row[i]));
                }
            }
        }else{
            throw new IllegalArgumentException("ERROR: THE LENGTH OF ROW DOES NOT MATCH THE NUMBER OF COLUMNS. ");
        }
    }

    /**
     * Insert row into table;
     * @param row Row expression passed in.
     * @return True on success.
     */
    public void insertRow(String[] row, boolean allowSpecialValue) throws IllegalArgumentException{
        checkRowType(row, allowSpecialValue);
        for (int i = 0; i < colNum(); i++) {
            getColumn(i).add(row[i]);
        }
    }

    /**
     * Return the String representation of this table. Float numbers will be specified to 3 decimal places.
     */
    public String toString() {
        return this.toString(true);
    }

    /**
     * Return the String representation of this table.
     * @param round If round is true, float numbers will be specified to 3 decimal places
     */
    public String toString(boolean round) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < colNum(); i++) {
            Column col = getColumn(i);
            String name = getColName(i);
            if (i < colNum() - 1) {
                builder.append(name).append(' ').append(col.type).append(',');
            } else {
                builder.append(name).append(' ').append(col.type);
            }
        }
        StringJoiner jointer = new StringJoiner("\r\n");
        jointer.add(builder.toString());
        for (int i = 0; i < rowNum(); i++) {
            jointer.add(getRow(i, round));
        }
        return jointer.toString();
    }

    /**
     * Check which columns in this table has the same name as another
     * table's.
     * @param tb Another table to be compared.
     * @return The indexes of the common columns. 
     */
    public List<String> commonCols(Table tb) {
        LinkedList<String> common = new LinkedList<String>();
        for (String name : colNameSeq) {
            if (tb.contains(name)) {
                common.add(name);
            }
        }
        return common;
    }

    /**
    *@param tb Another table to be joined with this table.  
    *@return Column information after joining two tables.
    */
    public List<String> joinColInfo(Table tb) {
        List<String> common = this.commonCols(tb);
        List<String> colInfo = new LinkedList<String>();
        for (String colName : common) {
            colInfo.add(colName);
            colInfo.add(this.getType(colName));
        }
        for (String colName : this.getColNameSeq()) {
            if (!common.contains(colName)) {
                colInfo.add(colName);
                colInfo.add(this.getType(colName));
            }
        }
        for (String colName : tb.getColNameSeq()) {
            if (!common.contains(colName)) {
                colInfo.add(colName);
                colInfo.add(tb.getType(colName));
            }
        }
        return colInfo;
    }

    /**
    * Check which rows in another table matches row i in this table when joining
    * them and return their indexes in a List.
    * @param i Index of row in this table. 
    * @param tb Another table.
    * @return The indexes of matched rows in tb;
    */
    public List<Integer> matchRow(int i, Table tb) {
        List<String> common = this.commonCols(tb);
        List<Integer> rows = new LinkedList<Integer>();
        for (int j = 0; j < tb.rowNum(); j++) {
            boolean match = true;
            for (String colName : common) {
                if (!this.getItem(colName, i).equals(tb.getItem(colName, j))) {
                    match = false;
                    break;
                }
            }
            if (match == true)
                rows.add(j);
        }
        return rows;
    }
}