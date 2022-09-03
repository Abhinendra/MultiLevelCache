package phonepe.service;

import java.util.HashMap;
import java.util.Map;

public class MultiLevelLRUCache<K, V> implements Cache<K, V> {

    private Map<Integer, LRUCache<K, V>> multiLevelCache;
    private int levels;

    public MultiLevelLRUCache(int levels, Map<Integer, Integer> capacityPerLevels) {
        this.levels = levels;
        multiLevelCache = new HashMap<>();
        for (int i = 1; i <= levels; i++) {
            multiLevelCache.put(i, new LRUCache<>(capacityPerLevels.get(i)));
        }
    }

    @Override
    public V read(K key) {
        long startTime = System.nanoTime();

        boolean found = false;
        int currentLevel = 1;
        V value = null;
        while (!found && (currentLevel <= levels)) {
            value = multiLevelCache.get(currentLevel).read(key);
            // System.out.println("Value: " + value + " at level: " + currentLevel);
            if (value != null) {
                found = true;
                for (int i = 1; i <= currentLevel; i++) {
                    LRUCache<K, V> cache = multiLevelCache.get(i);
                    cache.write(key, value);
                }
            }
            currentLevel++;
        }

        long endTime = System.nanoTime();
        long seconds = (endTime - startTime);
        System.out.println("Value: " + value + " timeTaken: " + seconds + "ns");
        return value;
    }

    @Override
    public boolean write(K key, V value) {
        long startTime = System.nanoTime();
        boolean stopWrite = false;
        int currentLevel = 1;
        while (!stopWrite && currentLevel <= levels) {
            boolean valueNotPresent = multiLevelCache.get(currentLevel).write(key, value);
            if (!valueNotPresent) {
                stopWrite = true;
            }
            currentLevel++;
        }
        long endTime = System.nanoTime();
        long seconds = (endTime - startTime) / 1000;
        System.out.println("Total timeTaken: " + seconds + "ns" + " levels Written : " + (currentLevel - 1));
        return true;
    }
}
