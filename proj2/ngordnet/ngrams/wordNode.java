package ngordnet.ngrams;

public class wordNode {
    private final int id;
    private final String str;

    public wordNode(int i, String s) {
        id = i;
        str = s;
    }

    public boolean contain(String word) {
        return str.contains(word);
    }

    public String[] words() {
        return str.split(" ");
    }

    @Override
    public int hashCode() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof wordNode other) {
            return id == other.id && str.equals(other.str);
        }
        return false;
    }

}
