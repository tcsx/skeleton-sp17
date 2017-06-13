import static org.junit.Assert.*;
import org.junit.Test;

public class TestArrayDeque1B {
    @Test
    public void Test() {
        StudentArrayDeque<Integer> sad = new StudentArrayDeque<>();
        ArrayDequeSolution<Integer> ads = new ArrayDequeSolution<>();
        String message = "";
        for (int i = 0; i < 200; i++) {
            int randomMethod = StdRandom.uniform(4);
            Integer addNum = StdRandom.uniform(1000);
            switch (randomMethod) {
            case 0:
                sad.addFirst(addNum);
                ads.addFirst(addNum);
                message = message + "addFirst(" + addNum + ")\r\n";
                break;
            case 1:
                sad.addLast(addNum);
                ads.addLast(addNum);
                message = message + "addLast(" + addNum + ")\r\n";
                break;
            case 2:
                if (sad.size() > 0 && ads.size() > 0) {
                    message = message + "removeFirst()\r\n";
                    assertEquals(message, ads.removeFirst(), sad.removeFirst());
                }
                break;
            case 3:
                if (sad.size() > 0 && ads.size() > 0) {
                    message = message + "removeLast()\r\n";
                    assertEquals(message, ads.removeLast(), sad.removeLast());
                }
                break;
            default:
                break;
            }
        }
    }

    public static void main(String[] args) {
        jh61b.junit.TestRunner.runTests("all", TestArrayDeque1B.class);
    }
}