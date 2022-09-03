package phonepe.service;

public interface Cache<K, V> {
    V read(K key);

    boolean write(K key, V value);
}
