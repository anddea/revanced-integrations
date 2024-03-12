package app.revanced.integrations.youtube.utils;

/**
 * Text pattern searching using a prefix tree (trie).
 */
public final class StringTrieSearch extends TrieSearch<String> {

    public StringTrieSearch() {
        super(new StringTrieNode());
    }

    private static final class StringTrieNode extends TrieNode<String> {
        StringTrieNode() {
            super();
        }

        StringTrieNode(char nodeCharacterValue) {
            super(nodeCharacterValue);
        }

        @Override
        TrieNode<String> createNode(char nodeValue) {
            return new StringTrieNode(nodeValue);
        }

        @Override
        char getCharValue(String text, int index) {
            return text.charAt(index);
        }

        @Override
        int getTextLength(String text) {
            return text.length();
        }
    }
}