package codes.showme.server.account.authentication;

import java.util.Optional;

public interface TokenRepository {
    int getTokenCacheSeconds();
    String generateKey();

    /**
     *
     * @param token
     * @return cache key
     */
    String cacheToken(Token token);

    Optional<Token> getByTokenKey(String tokenKey);
}
