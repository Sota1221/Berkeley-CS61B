import java.util.*;

public class TrieTree {
    private CharNode root = null;
    private ArrayList<String> words;

    public TrieTree() {
        this.root = new CharNode(null);
    }

    public CharNode getRoot() {
        return root;
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



    public void collectWords(CharNode current) {
        if (current.end) {
            words.add(current.name);
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
        String name;

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
                name = actualName;
                return;
            }
            if (parentNode == null) {
                return;
            }
            String firstChar = word.substring(0, 1);
            CharNode currentNode = null;
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
