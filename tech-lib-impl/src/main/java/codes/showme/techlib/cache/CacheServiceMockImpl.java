package codes.showme.techlib.cache;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CacheServiceMockImpl implements CacheService {

    private final static Logger logger = LoggerFactory.getLogger(CacheServiceMockImpl.class);

    Cache<String, String> cache = CacheBuilder.newBuilder()
            .concurrencyLevel(Runtime.getRuntime().availableProcessors())
            .build();

    @Override
    public void cache(String key, String value, int cacheSeconds) {
        cache.put(key, value);
        logger.info("MockImpl cached,key:{},value:{},seconds:{}", key, value, cacheSeconds);
    }

    @Override
    public String getValue(String key) {
        return cache.getIfPresent(key);
    }

    @Override
    public void remove(String key) {
        cache.invalidate(key);
        logger.info("MockImpl cached,invalidate key:{}", key);
    }
}
