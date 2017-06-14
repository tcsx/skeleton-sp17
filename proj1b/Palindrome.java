public class Palindrome {
    public static Deque<Character> wordToDeque(String word) {
        Deque<Character> queue = new ArrayDequeSolution<>();
        for (int i = 0; i < word.length(); i++) {
            queue.addLast(word.charAt(i));
        }
        return queue;
    }

    public static boolean isPalindrome(String word) {
        int length = word.length();
        for (int i = 0; i < length / 2; i++) {
            if (word.charAt(i) != word.charAt(length - 1 - i)) {
                return false;
            }
        }
        return true;
    }

    public static boolean isPalindrome(String word, CharacterComparator cc) {
        int length = word.length();
        for (int i = 0; i < length / 2; i++) {
            if (!cc.equalChars(word.charAt(i), word.charAt(length - 1 - i))) {
                return false;
            }
        }
        return true;
    }
}
