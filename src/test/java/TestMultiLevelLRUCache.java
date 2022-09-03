import org.junit.Assert;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import phonepe.service.MultiLevelLRUCache;

import java.util.HashMap;
import java.util.Map;

@SpringBootTest
public class TestMultiLevelLRUCache {

    @Test
    public void testSingleLevelCacheRead() {
        Map<Integer, Integer> levelCapacity = new HashMap<>();
        levelCapacity.put(1, 5);
        MultiLevelLRUCache<String, String> multiLevelCache = new MultiLevelLRUCache<>(1, levelCapacity);
        multiLevelCache.write("1", "11");
        multiLevelCache.write("2", "12");
        multiLevelCache.write("3", "13");
        multiLevelCache.write("4", "14");
        multiLevelCache.write("5", "15");
        multiLevelCache.write("6", "16");

        Assert.assertNull(multiLevelCache.read("1"));

        Assert.assertEquals("12", multiLevelCache.read("2"));
        Assert.assertEquals("13", multiLevelCache.read("3"));
        Assert.assertEquals("14", multiLevelCache.read("4"));
        Assert.assertEquals("15", multiLevelCache.read("5"));
        Assert.assertEquals("16", multiLevelCache.read("6"));
    }


    @Test
    public void testSingleLevelCacheReadTwice() {
        Map<Integer, Integer> levelCapacity = new HashMap<>();
        levelCapacity.put(1, 5);
        MultiLevelLRUCache<String, String> multiLevelCache = new MultiLevelLRUCache<>(1, levelCapacity);
        multiLevelCache.write("1", "11");
        multiLevelCache.write("2", "12");
        multiLevelCache.write("3", "13");
        multiLevelCache.write("4", "14");
        multiLevelCache.write("5", "15");
        multiLevelCache.read("1");
        multiLevelCache.write("6", "16");

        Assert.assertNull(multiLevelCache.read("2"));

        Assert.assertEquals("11",multiLevelCache.read("1"));
        Assert.assertEquals("13", multiLevelCache.read("3"));
        Assert.assertEquals("14", multiLevelCache.read("4"));
        Assert.assertEquals("15", multiLevelCache.read("5"));
        Assert.assertEquals("16", multiLevelCache.read("6"));
    }


    @Test
    public void testTwoLevelCacheRead() {
        Map<Integer, Integer> levelCapacity = new HashMap<>();
        levelCapacity.put(1, 3);
        levelCapacity.put(2, 5);
        MultiLevelLRUCache<String, String> multiLevelCache = new MultiLevelLRUCache<>(2, levelCapacity);
        multiLevelCache.write("1", "11");
        multiLevelCache.write("2", "12");
        multiLevelCache.write("3", "13");
        multiLevelCache.write("4", "14");
        multiLevelCache.write("5", "15");
        multiLevelCache.write("6", "16");

        Assert.assertNull(multiLevelCache.read("1"));
        Assert.assertEquals("12", multiLevelCache.read("2"));
    }
}
