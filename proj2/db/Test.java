package db;

public class Test {
    public static void main(String[] args) {
        String  expr = "2,4,, ";
        System.out.println(Parser.ROW.matcher(expr).matches());
    }
    public static void e() throws Exception{
        try {
             e1();
        } catch (Exception e) {
            throw new Exception("error: " + e.toString());
        }
       
    }
    public static void e1() throws Exception{
        throw new Exception("haha");
    }
}