package codes.showme.server.auth;

import org.apache.shiro.authc.BearerToken;

public class Token extends BearerToken {
    private String token;
    private String remoteAddr;

    public Token(String token, String remoteAddr) {
        super(token, remoteAddr);
        this.token = token;
        this.remoteAddr = remoteAddr;
    }

    public String getRemoteAddr() {
        return remoteAddr;
    }

    public void setRemoteAddr(String remoteAddr) {
        this.remoteAddr = remoteAddr;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
