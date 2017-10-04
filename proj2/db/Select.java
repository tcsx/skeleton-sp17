package db;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;

public class Select {
    public static Table select(String[] exprs, String[] conds, Table[] tbs) {
        Table tb = join(tbs);
        if (exprs.length != 1 || !"*".equals(exprs[0])){
            tb = colFilter(exprs, tb);
        }
        // tb = rowFilter(conds, tb);
        return tb;
    }

    // public static Table rowFilter(String[] conds, Table tb) {

    // }

    // public static boolean matches(String[] conds, Table tb) {

    // }

    public static Table colFilter(String[] exprs, Table tb) {
        Table newTable = new Table();
        Matcher m;
        for (String expr : exprs) {
            HashMap<String, Table.Column> cols = tb.getCols();
            if (Parser.NAMES.matcher(expr).matches()) {
                if (cols.containsKey(expr))
                    newTable.addCol(expr, tb.getColumn(expr));
                else{
                    printColNotExist(expr);
                    return null;
                }
            }else if ((m = Parser.COLEXPR.matcher(expr)).matches()){
                Table.Column col = Operation.operation(m.group(1),m.group(2),m.group(3),m.group(4),tb);
                if (col == null) {
                    return null;
                }
                newTable.addCol(m.group(4), col);
            }else{
                System.err.println("ERROR: WRONG COLUMN EXPRESSION: " + expr);
                return null;
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
                joinedTb.insertRow(newRow, true);
            }
        }
        return joinedTb;
    }

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

    public static void printColNotExist(String col) {
        System.err.println("ERROR: COLUMN " + col + " DOES NOT EXIST.");
    }

}