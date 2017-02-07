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
        for (int i = 0; i < len; i++) {
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
        Deque stringDeque = wordToDeque(word);
        int len = stringDeque.size();
        OffByOne obo = new OffByOne();
        for (int i = 0; i < len; i++) {
            Character char1 = (Character) stringDeque.get(i);
            Character char2 = (Character) stringDeque.get(len - 1 - i);
            if (!cc.equalChars(char1, char2)) {
                return false;
            }
        }
        return true;
    }
}
