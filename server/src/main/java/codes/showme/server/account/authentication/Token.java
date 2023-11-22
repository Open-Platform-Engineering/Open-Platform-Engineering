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

    public String save() {
        TokenRepository tokenRepository = InstanceFactory.getInstance(TokenRepository.class);
        return tokenRepository.cacheToken(this);
    }

    public static Optional<Token> getByTokenKey(String tokenKey) {
        TokenRepository tokenRepository = InstanceFactory.getInstance(TokenRepository.class);
        return tokenRepository.getByTokenKey(tokenKey);
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
