package db;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class Test{
    public static void main(String[] args) {
        Pattern p = Pattern.compile("w(e)?");
        String s = "w";
        String ss[] = s.split(",");
        Matcher m = p.matcher(s);
        Table tb = new Table();
        Table.Column col = tb.new Column("int");
        col.add("8");
        col.add("8");
        col.add("8");
        Table tb1 = new Table();
        tb1.addCol("he", col);
        tb = null;
        for (String item : col) {
            System.out.println(item);
        }
        
    }

}