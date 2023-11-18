package codes.showme.server.account.authentication;

import codes.showme.techlib.cache.CacheService;
import codes.showme.techlib.ioc.InstanceFactory;
import codes.showme.techlib.json.JsonUtil;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.common.base.Strings;

import java.io.Serializable;
import java.util.Optional;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Token implements Serializable {


    private static final String TOKEN_CACHE_KEY_PREFIX = "code_planet_account_token_";
    private static final long serialVersionUID = -2565772518992762268L;
    private String email;
    private String remoteAddr;
    private String tenantId;

    public Token() {
    }

    public Token(String email, String remoteAddr) {
        this.email = email;
        this.remoteAddr = remoteAddr;
    }

    public String generateRandomToken() {
        TokenKeyGenerator generator = InstanceFactory.getInstance(TokenKeyGenerator.class);
        return generator.generate();
    }

    public String saveWithExpiredSeconds(int seconds) {
        String tokenKey = generateRandomToken();
        cacheStore(this, seconds, tokenKey);
        return tokenKey;
    }

    private void cacheStore(Token token, int seconds, String tokenKey) {
        CacheService cacheService = InstanceFactory.getInstance(CacheService.class);
        JsonUtil jsonUtil = InstanceFactory.getInstance(JsonUtil.class);
        String value = jsonUtil.toJsonString(token);
        cacheService.cache(getCacheKey(tokenKey), value, seconds);
    }

    public static Optional<Token> getByTokenKey(String tokenKey) {
        CacheService cacheService = InstanceFactory.getInstance(CacheService.class);
        String result = cacheService.getValue(getCacheKey(tokenKey));
        if (Strings.isNullOrEmpty(result)) {
            return Optional.empty();
        }
        JsonUtil jsonUtil = InstanceFactory.getInstance(JsonUtil.class);
        Token token = jsonUtil.toObject(result, Token.class);
        return Optional.ofNullable(token);

    }


    private static String getCacheKey(String tokenKey) {
        return TOKEN_CACHE_KEY_PREFIX + tokenKey;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRemoteAddr() {
        return remoteAddr;
    }

    public void setRemoteAddr(String remoteAddr) {
        this.remoteAddr = remoteAddr;
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }


}
