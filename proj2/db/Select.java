package db;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;

/**
 * Contains methods essential to select commands.
 */
public class Select {

    /**
     * Take in arrays of column expressions, conditional statements and tables and return the result table of the select operation.
     * @param exprs Column expressions
     * @param cond Conditional statements
     * @param table Tables passed in
     * @throws Exception if error happens in the select operation
     */
    public static Table select(String[] exprs, String[] conds, Table[] tbs) throws Exception{
        Table tb = join(tbs);
        if (exprs.length != 1 || !"*".equals(exprs[0])) {
            tb = colFilter(exprs, tb);
        }
        if (conds != null) {
            tb = rowFilter(conds, tb);
        }
        return tb;
    }

    /**
     * Apply conditional statements to table and returns the result table.
     * @param cond Conditional statements passed in as array
     * @param tb Table to which conditional statements will be applied
     * @return The table after conditional statements are applied or null if any error happens
     * 
     * @throws Exception if the conditional statements are malformed or incompatible with the table
     */
    public static Table rowFilter(String[] conds, Table tb) throws Exception{
        Table newTable = new Table(tb);
        Matcher m;
        for (int i = 0; i < tb.rowNum(); i++) {
            boolean match = true;
            for (String cond : conds) {
                if ((m = Parser.CONDS.matcher(cond)).matches()) {
                    String cp1 = m.group(1);
                    if (!tb.contains(cp1)) {
                        throw new ColNotExistException(cp1);
                    }
                    String comparator = m.group(2);
                    String cp2 = m.group(3);
                    boolean isString = false;
                    String type1 = tb.getType(cp1);
                    if (Parser.NAMES.matcher(cp2).matches()) {
                        if (!tb.contains(cp2)) {
                            throw new ColNotExistException(cp2);
                        }
                        String type2 = tb.getType(cp2);
                        if (Operation.STRING.equals(type1) && Operation.STRING.equals(type2)) {
                            isString = true;
                        } else if (Operation.STRING.equals(type1) || Operation.STRING.equals(type2)) {
                            throw new Exception(String.format("ERROR: CANNOT COMPARE COLUMN %s AND COLUMN %s. TYPE MISMATCH.", cp1,
                                    cp2));
                        }
                        if (!Compare.compare(tb.getItem(cp1, i), tb.getItem(cp2, i), comparator, isString)) {
                            match = false;
                            break;
                        }
                    } else {
                        String type2 = Parser.parseType(cp2);
                        if (type2 == null)
                            return null;
                        if (Operation.STRING.equals(type1) && Operation.STRING.equals(type2)) {
                            isString = true;
                        } else if (Operation.STRING.equals(type1) || Operation.STRING.equals(type2)) {
                            throw new Exception(String.format("ERROR: CANNOT COMPARE COLUMN %s AND LITERAL %s. TYPE MISMATCH.", cp1,
                                    cp2));
                        }
                        if (!Compare.compare(tb.getItem(cp1, i), cp2, comparator, isString)) {
                            match = false;
                            break;
                        }
                    }
                } else {
                    throw new Exception("ERROR: INCORRECT CONDITIONAL STATEMENTS: " + cond);
                }
            }
            if (match == true) {
                newTable.insertRow(tb.getRow(i), true);
            }
        }
        return newTable;
    }

    /**
     * Apply column expressions to table and returns the result.
     * @param exprs Column expressions passed in as array
     * @param tb Table to which column expressions will be applied
     * @return The table after column expressions are applied or null if any error happens
     *@throws Exception if column expressions are malformed or incompatibel with the table 
     */
    public static Table colFilter(String[] exprs, Table tb) throws Exception{
        Table newTable = new Table();
        Matcher m;
        for (String expr : exprs) {
            HashMap<String, Table.Column> cols = tb.getCols();
            if (Parser.NAMES.matcher(expr).matches()) {
                if (cols.containsKey(expr))
                    newTable.addCol(expr, tb.getColumn(expr));
                else {
                    throw new ColNotExistException(expr);
                }
            } else if ((m = Parser.COLEXPR.matcher(expr)).matches()) {
                Table.Column col = Operation.operation(m.group(1), m.group(2), m.group(3), m.group(4), tb);
                newTable.addCol(m.group(4), col);
            } else {
                throw new Exception("ERROR: WRONG COLUMN EXPRESSION: " + expr);
            }

        }
        return newTable;
    }

    /**
    * @return The result of joining t1 and t2.
    */
    public static Table join(Table t1, Table t2) {
        Table joinedTb = new Table(t1.joinColInfo(t2));
        List<String> joinedColSeq = joinedTb.getColNameSeq();
        for (int i = 0; i < t1.rowNum(); i++) {
            List<Integer> matchRows = t1.matchRow(i, t2);
            if (matchRows.isEmpty())
                continue;
            String[] newRow = new String[joinedTb.colNum()];
            for (Integer rowId : matchRows) {
                Iterator<String> iterator = joinedColSeq.iterator();
                int j = 0;
                while (j < t1.colNum()) {
                    newRow[j] = t1.getItem(iterator.next(), i);
                    j++;
                }
                while (j < newRow.length) {
                    newRow[j] = t2.getItem(iterator.next(), rowId);
                    j++;
                }
                try{
                joinedTb.insertRow(newRow, true);
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        }
        return joinedTb;
    }

    /**
     * @return The result of joining an array of tables.
     */
    public static Table join(Table[] tables) {
        if (tables.length == 1) {
            return tables[0];
        }
        Table result = tables[0];
        for (int i = 1; i < tables.length; i++) {
            result = join(result, tables[i]);
        }
        return result;
    }

    public static String colNotExist(String col) {
        return String.format("ERROR: COLUMN " + col + " DOES NOT EXIST.");
    }

}