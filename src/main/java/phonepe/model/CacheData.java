package phonepe.model;

public class CacheData<K, V> {
    K key;
    V value;

    public CacheData(K key, V value) {
        this.key = key;
        this.value = value;
    }

    public K getKey() {
        return key;
    }

    public V getValue() {
        return value;
    }


}
