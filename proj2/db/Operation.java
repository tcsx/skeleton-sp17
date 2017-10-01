package db;

import java.math.BigDecimal;

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

    
}
