package codes.showme.server.springSecurity;

import codes.showme.server.account.authentication.Token;
import codes.showme.server.account.authentication.TokenRepository;
import codes.showme.techlib.cache.CacheService;
import codes.showme.techlib.ioc.InstanceFactory;
import codes.showme.techlib.json.JsonUtil;
import com.google.common.base.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;
import java.util.UUID;

public class TokenRepositoryImpl implements TokenRepository {

    private static final Logger logger = LoggerFactory.getLogger(TokenRepositoryImpl.class);

    private static final String TOKEN_CACHE_KEY_PREFIX = "code_planet_account_token_";

    public int tokenCacheSeconds;

    @Override
    public int getTokenCacheSeconds() {
        return tokenCacheSeconds;
    }

    @Override
    public String generateKey() {
        return UUID.randomUUID().toString();
    }

    @Override
    public String cacheToken(Token token) {
        CacheService cacheService = InstanceFactory.getInstance(CacheService.class);
        JsonUtil jsonUtil = InstanceFactory.getInstance(JsonUtil.class);
        String value = jsonUtil.toJsonString(token);
        String tokenKey = generateKey();
        String cacheKey = getCacheKey(tokenKey);
        cacheService.cache(cacheKey, value, getTokenCacheSeconds());
        logger.info("cache tokenKey:{}, cacheKey:{},email:{}", token.getEmail());
        return tokenKey;
    }

    @Override
    public Optional<Token> getByTokenKey(String tokenKey) {
        CacheService cacheService = InstanceFactory.getInstance(CacheService.class);
        String result = cacheService.getValue(getCacheKey(tokenKey));
        if (Strings.isNullOrEmpty(result)) {
            return Optional.empty();
        }
        JsonUtil jsonUtil = InstanceFactory.getInstance(JsonUtil.class);
        Token token = jsonUtil.toObject(result, Token.class);
        return Optional.ofNullable(token);
    }

    @Override
    public void remove(String tokenKey) {
        CacheService cacheService = InstanceFactory.getInstance(CacheService.class);
        cacheService.remove(getCacheKey(tokenKey));
        logger.info("remove tokenKey's cache:{}", tokenKey);
    }

    private static String getCacheKey(String tokenKey) {
        return TOKEN_CACHE_KEY_PREFIX + tokenKey;
    }

    public void setTokenCacheSeconds(int tokenCacheSeconds) {
        this.tokenCacheSeconds = tokenCacheSeconds;
    }
}
