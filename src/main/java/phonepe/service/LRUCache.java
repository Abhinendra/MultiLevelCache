package phonepe.service;

import phonepe.exception.CacheReadException;
import phonepe.exception.CacheWriteException;
import phonepe.model.CacheData;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class LRUCache<K, V> implements Cache<K, V> {

    private final int size;
    private Map<K, CacheData<K, V>> map;
    private LinkedList<CacheData<K, V>> linkedList;
    private final ReadWriteLock lock;
    private final Lock readLock;
    private final Lock writeLock;

    public LRUCache(int size) {
        this.size = size;
        this.map = new HashMap<>();
        this.linkedList = new LinkedList<>();
        lock = new ReentrantReadWriteLock();
        readLock = lock.readLock();
        writeLock = lock.writeLock();
    }

    @Override
    public V read(K key) {
        readLock.lock();
        try {
            CacheData<K, V> cacheData;
            if (map.containsKey(key)) {
                cacheData = map.get(key);
                linkedList.remove(cacheData);
                linkedList.addFirst(cacheData);
                return cacheData.getValue();
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            throw new CacheReadException("Failed to read from cache", e);
        } finally {
            readLock.unlock();
        }

    }

    @Override
    public boolean write(K key, V value) {
        writeLock.lock();
        try {
            CacheData<K, V> cacheData;
            if (map.containsKey(key)) {
                cacheData = map.get(key);
                linkedList.remove(cacheData);
                if (cacheData.getValue().equals(value)) {
                    linkedList.addFirst(cacheData);
                    return false;
                } else {
                    cacheData = new CacheData<>(key, value);
                    linkedList.addFirst(cacheData);
                    map.put(key, cacheData);
                    return true;
                }
            } else {
                cacheData = new CacheData<>(key, value);
                map.put(key, cacheData);
                linkedList.addFirst(cacheData);
                this.maintainSize();
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            throw new CacheWriteException("Failed to write to cache", e);
        } finally {
            writeLock.unlock();
        }

    }

    private void maintainSize() {
        while (linkedList.size() > size) {
            CacheData<K, V> cacheData = linkedList.getLast();
            map.remove(cacheData.getKey());
            linkedList.removeLast();
            //System.out.println("Removed key: " + cacheData.getKey());
        }
    }
}
