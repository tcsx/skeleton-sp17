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
        System.out.println(m.matches());
        Table tb = new Table();
        System.out.println(9.1 - 9);
        System.out.println(9.111- 9.111);
        System.out.println(9.111- 9.1101);
        System.out.println(9.111- 9.112);
        System.out.println("1.9".compareTo("2"));
        System.out.println("1.9".compareTo("2.1"));
        System.out.println("-1.2".compareTo("2"));
        System.out.println("-1".compareTo("1"));
        System.out.println("-1".compareTo("-2"));
        
    }

}