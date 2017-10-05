package db;

/**
 * Auxiliary class for condition statements. Contains methods for comparing two literals.
 */
public class Compare {
    /**
     * @param s1 First literal
     * @param s2 Second literal
     * @param isString True if both literals are string
     * @return A positive double if s1 > s2, zero if s1 == s2 and negative double if s1 < s2
     */
    public static double compare(String s1, String s2, boolean isStrings) {
        if (Operation.NAN.equals(s1) && Operation.NAN.equals(s2)) {
            return 0;
        }
        if (Operation.NAN.equals(s1)) {
            return 1;
        }
        if (Operation.NAN.equals(s2)) {
            return -1;
        }
        if (isStrings) {
            return s1.compareTo(s2);
        }
        double d1 = Double.parseDouble(s1);
        double d2 = Double.parseDouble(s2);
        return d1 - d2;
    }

    /**
     * Check if comparison is true.
     * @param s1 First literal in the comparison
     * @param s2 Second literal in the comparison
     * @param comparator Comparison operator in the comparison
     * @param isStrings True is both literals are strings
     * @return True if and only if the comparison is true
     */
    public static boolean compare(String s1, String s2, String comparator, boolean isStrings) {
        if (Operation.NOVALUE.equals(s1) || Operation.NOVALUE.equals(s2)) {
            return false;
        }
        if (">=".equals(comparator) && compare(s1, s2, isStrings) >= 0)
            return true;
        if ("<=".equals(comparator) && compare(s1, s2, isStrings) <= 0)
            return true;
        if ("<".equals(comparator) && compare(s1, s2, isStrings) < 0)
            return true;
        if (">".equals(comparator) && compare(s1, s2, isStrings) > 0)
            return true;
        if ("==".equals(comparator) && compare(s1, s2, isStrings) == 0)
            return true;
        if ("!=".equals(comparator) && compare(s1, s2, isStrings) != 0)
            return true;
        return false;
    }

}