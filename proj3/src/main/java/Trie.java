import java.util.*;

public class Trie {
    CharNode root = null;
    ArrayList<String> words;
    Map<CharNode, Integer> tempNodes;

    public Trie() {
        this.root = new CharNode(null);
    }



    public List<String> getLocationsByPrefix(String prefix) {
        words = new ArrayList<>();
        ArrayList<String> result = new ArrayList<>();
        CharNode currentNode = root;
        int prefixLen = prefix.length();
        for (int k = 0; k < prefixLen; k++) {
            String  ch = prefix.substring(k, k + 1);
            if (currentNode.child.containsKey(ch)) {
                currentNode = currentNode.child.get(ch);
            } else {
                return result;
            }
        }
        collectWords(currentNode);
        currentNode = root;
        return words;
    }


 /*   public void searchKeyWord(CharNode current, Character firstLetterOfKey, String key, int digit, int count) {
        if (count == key.length() - 1) {
            if (current.child.containsKey(' ')) {
                tempNodes.put(current.child.get(' '), digit);
                return;
            }
            return;
        }
        if (count > 0) {
            firstLetterOfKey = key.charAt(count + 1);
            if (current.child.containsKey(firstLetterOfKey)) {
                searchKeyWord(current.child.get(firstLetterOfKey), firstLetterOfKey, key, digit, count + 1);
            }
            return;
        }
        if (digit != 0 && current.child.containsKey(firstLetterOfKey)) {
            searchKeyWord(current.child.get(firstLetterOfKey), firstLetterOfKey, key, digit, count);
        }
        if (digit != 0 && current.charValue.equals(firstLetterOfKey)) {
            firstLetterOfKey = key.charAt(1);
            if (current.child.containsKey(firstLetterOfKey)) {
                searchKeyWord(current.child.get(firstLetterOfKey), firstLetterOfKey, key, digit, count + 1);
            }
            return;
        }
        if (digit == 0) {
            tempNodes = new HashMap<>();
        }
        for (int i = 0; i < 10; i++) {
            char num = (char) ('0' + i);
            if (current.child.containsKey(num)) {
                searchKeyWord(current.child.get(num), firstLetterOfKey, key, digit * 10 + i, 0);
            }
        }
    }
*/

    public void collectWords(CharNode current) {
        if (current.end) {
            for (String name: current.names) {
                words.add(name);
            }
            if (current.child == null) {
                return;
            }
        }
        for (CharNode n: current.child.values()) {
            collectWords(n);
        }
    }


    class CharNode {
        String charValue;
        Map<String, CharNode> child = new HashMap<>();
        boolean end;
        Set<String> names;

        CharNode(String charValue) {
            this.charValue = charValue;
        }

        public void add(String word) {
            add(word, this, word);
        }

        public void add(String word, CharNode parentNode, String actualName) {
            int wordLen = word.length();
            if (word == null || wordLen < 1) {
                parentNode.end = true;
                if (names == null) {
                    names = new HashSet<>();
                }
                names.add(actualName);
                return;
            }
            if (parentNode == null) {
                return;
            }
            String firstChar = word.substring(0, 1);
            if (firstChar.matches("[^A-Za-z|^ ]")) {
                add(word.substring(1, wordLen), this, actualName);
                return;
            }
            CharNode currentNode = null;
            firstChar = firstChar.toLowerCase();
            if (parentNode.child.containsKey(firstChar)) {
                currentNode = parentNode.child.get(firstChar);
            } else {
                currentNode = new CharNode(firstChar);
                parentNode.child.put(firstChar, currentNode);
            }
            currentNode.add(word.substring(1, wordLen), currentNode, actualName);
        }

    }
}
