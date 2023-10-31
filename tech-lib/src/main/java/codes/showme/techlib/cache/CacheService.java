package codes.showme.techlib.cache;

public interface CacheService {
    void cache(String key, String value, int cacheSeconds);

    String getValue(String key);

    void remove(String key);
}
