package parser_combinator;

public class Tuple<K, V> {
    K first;
    V second;

    public K getFirst() {
        return first;
    }

    public void setFirst(K first) {
        this.first = first;
    }

    public V getSecond() {
        return second;
    }

    public void setSecond(V second) {
        this.second = second;
    }

    public Tuple(K first, V second) {
        this.first = first;
        this.second = second;
    }
}