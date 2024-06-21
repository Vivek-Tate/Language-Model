public class Pair<K, V> {
    private K key;
    private V value;

    // Constructor to create a Pair with a key and a value
    public Pair(K key, V value) {
        this.key = key;
        this.value = value;
    }

    // Getter methods for key and value
    public K getKey() {
        return key;
    }

    public V getValue() {
        return value;
    }

    // Setter methods for key and value
    public void setKey(K key) {
        this.key = key;
    }

    public void setValue(V value) {
        this.value = value;
    }
}