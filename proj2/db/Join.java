package db;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class Join {

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
                joinedTb.insertRow(newRow);
            }
        }
        return joinedTb;
    }

    public static Table join(Table[] tables) {
        if(tables.length == 1) {
            return tables[0];
        }
        Table result = tables[0];
        for(int i = 1; i < tables.length; i++) {
            result = join(result, tables[i]);
        }
        return result;
    }

}