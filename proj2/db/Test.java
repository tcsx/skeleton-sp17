package db;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class Test{
    public static void main(String[] args) {
        
        String vowel = "[aeiou]";
        String[] s = "google".split(vowel);
        
        for (String string : s) {
			System.out.print(string);
        }
        List<Integer> rows = new LinkedList<Integer>();
        System.out.println(rows.isEmpty());
        
    }


}