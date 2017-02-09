/**
 * Created by sota_ on 2/4/2017.
 */
public class Palindrome {

    public static Deque<Character> wordToDeque(String word) {
        Deque<Character> stringDeque = new LinkedListDeque<Character>();
        for (int i = 0; i < word.length(); i++) {
            stringDeque.addLast(word.charAt(i));
        }
        return stringDeque;
    }

    public static boolean isPalindrome(String word) {
        if (word.length() == 1 || word.length() == 0) {
            return true;
        }
        Deque stringDeque = wordToDeque(word);
        int len = stringDeque.size();
        for (int i = 0; i < len / 2; i++) {
            if (stringDeque.get(i) != stringDeque.get(len - 1 - i)) {
                return false;
            }
        }
        return true;
    }

    public static boolean isPalindrome(String word, CharacterComparator cc) {
        if (word.length() == 1 || word.length() == 0) {
            return true;
        }
        Deque<Character> stringDeque = wordToDeque(word);
        int len = stringDeque.size();
        for (int i = 0; i < (len - 1) / 2; i++) {
            if (!cc.equalChars(stringDeque.get(i), stringDeque.get(len - 1 - i))) {
                return false;
            }
        }
        return true;
    }
}
