package db;

import java.math.BigDecimal;
import java.util.HashMap;

public class Operation {
    public static final String NAN = "NaN";
    public static final String NOVALUE = "NOVALUE";
    public static final String INT = "int";
    public static final String FLOAT = "float";
    public static final String STRING = "string";

    private static String intToFloat(String s) {
        return s + ".0";
    }

    public static String add(String s1, String type1, String s2, String type2) {
        if (NAN.equals(s1) || NAN.equals(s2))
            return NAN;
        if (NOVALUE.equals(s1) && NOVALUE.equals(s2))
            return NOVALUE;
        if (NOVALUE.equals(s1)) {
            if (FLOAT.equals(type1) && INT.equals(type2)) {
                return intToFloat(s2);
            }
            return s2;
        }
        if (NOVALUE.equals(s2))
            return add(s2, type2, s1, type1);
        if (STRING.equals(type1)) {
            return s1.substring(0, s1.length() - 1) + s2.substring(1);
        }
        if (INT.equals(type1) && INT.equals(type2)) {
            return String.valueOf(Integer.parseInt(s1) + Integer.parseInt(s2));
        }
        BigDecimal d1 = new BigDecimal(s1);
        BigDecimal d2 = new BigDecimal(s2);
        return String.valueOf(d1.add(d2).doubleValue());
    }

    public static String minus(String s1, String type1, String s2, String type2) {
        if (NAN.equals(s1) || NAN.equals(s2))
            return NAN;
        if (NOVALUE.equals(s1) && NOVALUE.equals(s2))
            return NOVALUE;
        if (NOVALUE.equals(s1)) {
            if (INT.equals(type1) && INT.equals(type2))
                return String.valueOf(-Integer.parseInt(s2));
            return String.valueOf(-Double.parseDouble(s2));
        }
        if (NOVALUE.equals(s2)) {
            if (INT.equals(type1) && FLOAT.equals(type2))
                return intToFloat(s1);
            return s1;
        }
        if (INT.equals(type1) && INT.equals(type2))
            return String.valueOf(Integer.parseInt(s1) - Integer.parseInt(s2));
        BigDecimal d1 = new BigDecimal(s1);
        BigDecimal d2 = new BigDecimal(s2);
        return String.valueOf(d1.subtract(d2).doubleValue());
    }

    public static String multiply(String s1, String type1, String s2, String type2) {
        if (NAN.equals(s1) || NAN.equals(s2))
            return NAN;
        if (NOVALUE.equals(s1) && NOVALUE.equals(s2))
            return NOVALUE;
        if (NOVALUE.equals(s1)) {
            if (INT.equals(type1) && INT.equals(type2))
                return "0";
            return "0.0";
        }
        if (NOVALUE.equals(s2))
            return multiply(s2, type2, s1, type1);
        if (INT.equals(type1) && INT.equals(type2))
            return String.valueOf(Integer.parseInt(s1) * Integer.parseInt(s2));
        BigDecimal d1 = new BigDecimal(s1);
        BigDecimal d2 = new BigDecimal(s2);
        return String.valueOf(d1.multiply(d2).doubleValue());
    }

    public static String divide(String s1, String type1, String s2, String type2) {
        if (NAN.equals(s1) || NAN.equals(s2))
            return NAN;
        if (NOVALUE.equals(s1) && NOVALUE.equals(s2))
            return NOVALUE;
        if (NOVALUE.equals(s2) || (Double.parseDouble(s2)) == 0)
            return NAN;
        if (NOVALUE.equals(s1)) {
            if (INT.equals(type1) && INT.equals(type2))
                return "0";
            return "0.0";
        }
        if (INT.equals(type1) && INT.equals(type2))
            return String.valueOf(Integer.parseInt(s1) / Integer.parseInt(s2));
        BigDecimal d1 = new BigDecimal(s1);
        BigDecimal d2 = new BigDecimal(s2);
        return String.valueOf(d1.divide(d2, 3, BigDecimal.ROUND_HALF_UP).doubleValue());
    }

    public static Table.Column operation(String op1, String oprtr, String op2, String alias, Table tb) {
        HashMap<String, Table.Column> cols = tb.getCols();
        Table.Column newCol = null;
        if (!cols.containsKey(op1)) {
            Select.printColNotExist(op1);
            return null;
        }
        Table.Column col1 = tb.getColumn(op1);
        String type1 = col1.getType();
        if (Parser.NAMES.matcher(op2).matches()) {
            if (!cols.containsKey(op2)) {
                Select.printColNotExist(op2);
                return null;
            }
            Table.Column col2 = tb.getColumn(op2);
            String type = typesMatch(type1, col2.getType());
            if (type == null) {
                System.err.printf("ERROR: COLUMN %s AND COLUMN %s TYPE MISMATCH.\r\n", op1, op2);
                return null;
            }
            newCol = tb.new Column(type);
            operation(col1, col2, oprtr, newCol);
        } else {
            String type2 = Parser.parseType(op2);
            if(type2 == null)
                    return null;
            if (NOVALUE.equals(type2) || NAN.equals(type2)){
                System.err.println("ERROR: SPECIAL VALUE IS NOT ALLOWED IN COLUMN EXPRESSIONS");
                return null;
            }
            String type = typesMatch(type1, type2);
            if (type == null) {
                System.err.printf("ERROR: COLUMN %s AND LITERAL %s TYPE MISMATCH.\r\n", op1, op2);
                return null;
            }
            newCol = tb.new Column(type);
            for (int i = 0; i < col1.size(); i++) {
                newCol.add(operation(col1.get(i), type1, op2, type2, oprtr));
            }
        }
        return newCol;
    }

    public static void operation(Table.Column col1, Table.Column col2, String oprtr, Table.Column newCol) {
        String type1 = col1.getType();
        String type2 = col2.getType();
        for (int i = 0; i < col1.size(); i++) {
            newCol.add(operation(col1.get(i), type1, col2.get(i), type2, oprtr));
        }
    }

    public static String operation(String s1, String type1, String s2, String type2, String oprtr) {
        if ("+".equals(oprtr)) {
            return add(s1, type1, s2, type2);
        }
        if ("-".equals(oprtr)) {
            return minus(s1, type1, s2, type2);
        }
        if ("*".equals(oprtr)) {
            return multiply(s1, type1, s2, type2);
        }
        return divide(s1, type1, s2, type2);
    }

    public static String typesMatch(String t1, String t2) {
        if (STRING.equals(t1)) {
            if (!STRING.equals(t2)) {
                return null;
            }
            return STRING;
        }
        if (INT.equals(t1)) {
            if (STRING.equals(t2)) {
                return null;
            }
            return t2;
        }
        if (STRING.equals(t2)) {
            return null;
        }
        return t1;
    }

}
