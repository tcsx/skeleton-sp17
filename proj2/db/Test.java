package db;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class Test{
    public static void main(String[] args) {
        Pattern p = Pattern.compile("[+\\-*/]");
        String s = "*";
        Matcher m = p.matcher(s);
        System.out.println(m.matches());
        double a = 4.5;
        double b = 5.5;
        int c = 6;
        double d = 8.0;
        double e = 6.9;
        Integer f = 0;
        Double g = 0.0;
        System.out.println(Integer.valueOf("0000"));
        System.out.println(Double.parseDouble("000.0000"));
        
    }

}