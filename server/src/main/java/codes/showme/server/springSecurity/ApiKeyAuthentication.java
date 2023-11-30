package codes.showme.server.springSecurity;

import codes.showme.domain.account.Account;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class ApiKeyAuthentication extends AbstractAuthenticationToken {
    private final String token;

    private final Account account;


    public ApiKeyAuthentication(String token, Account account, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.token = token;
        this.account = account;
        setAuthenticated(true);
    }

    public Account getAccount() {
        return account;
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return token;
    }
}
