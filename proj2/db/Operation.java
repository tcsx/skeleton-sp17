package db;

import java.math.BigDecimal;
import java.util.HashMap;

public class Operation {
    public static final String NAN = "NaN";
    public static final String NOVALUE = "NOVALUE";
    public static final String INT = "int";
    public static final String FLOAT = "float";
    public static final String STRING = "string";

    /**
     * Convert a int literal to float literal by add .0 to its tail
     * @param s Int literal
     * @return float literal
     */
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

    /**
     * @param op1 First operand of the column expression
     * @param oprtr Operator of the column expression
     * @param op2 Second operand of the column expression
     * @param alias The name of the result column
     * @param tb The table to which the column expression is applied
     * @return The result column of the column expression
     * @throws Exception if error happens in the operation
     */
    public static Table.Column operation(String op1, String oprtr, String op2, String alias, Table tb) throws Exception {
        HashMap<String, Table.Column> cols = tb.getCols();
        Table.Column newCol = null;
        if (!cols.containsKey(op1)) {
            throw new ColNotExistException(op1);
        }
        Table.Column col1 = tb.getColumn(op1);
        String type1 = col1.getType();
        if (Parser.NAMES.matcher(op2).matches()) {
            if (!cols.containsKey(op2)) {
                throw new ColNotExistException(op2);
            }
            Table.Column col2 = tb.getColumn(op2);
            String type = typesMatch(type1, col2.getType());
            if (type == null) {
                throw new Exception(String.format("ERROR: COLUMN %s AND COLUMN %s TYPE MISMATCH.", op1, op2));
            }
            newCol = tb.new Column(type);
            operation(col1, col2, oprtr, newCol);
        } else {
            String type2 = Parser.parseType(op2);
            if(type2 == null)
                    return null;
            if (NOVALUE.equals(type2) || NAN.equals(type2)){
                throw new Exception(String.format("ERROR: SPECIAL VALUE IS NOT ALLOWED IN COLUMN EXPRESSIONS"));
            }
            String type = typesMatch(type1, type2);
            if (type == null) {
                throw new Exception(String.format("ERROR: COLUMN %s AND LITERAL %s TYPE MISMATCH.", op1, op2));
            }
            newCol = tb.new Column(type);
            for (int i = 0; i < col1.size(); i++) {
                newCol.add(operation(col1.get(i), type1, op2, type2, oprtr));
            }
        }
        return newCol;
    }

    /**
     * Takes in two columns and an operator. Applies the operator to the items in the two columns and add the result to a new column.
     * @param col1 First column
     * @param col2 Second column
     * @param oprtr Operator
     * @param newCol The result of applying operator to the two columns
     */
    public static void operation(Table.Column col1, Table.Column col2, String oprtr, Table.Column newCol) {
        String type1 = col1.getType();
        String type2 = col2.getType();
        for (int i = 0; i < col1.size(); i++) {
            newCol.add(operation(col1.get(i), type1, col2.get(i), type2, oprtr));
        }
    }

    /**
     * Take in two literals and an operator and returns the result literal.
     * @param s1 First literal
     * @param type1 Type of the first literal
     * @param s2 Second literal
     * @param type2 Type of the second literal
     * @param oprtr Operator
     * @return The result literal
     */
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

    /**
     * Check if two types are compatible for operation.
     * @param t1 First type
     * @param t2 Second type
     * @return The type of the result of the operation with operands of type t1 and 
     *         t2 or null if their types mismatch.
     */
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
