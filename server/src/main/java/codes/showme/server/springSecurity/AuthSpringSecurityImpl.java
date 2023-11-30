package codes.showme.server.springSecurity;

import codes.showme.server.auth.Auth;
import codes.showme.server.auth.Subject;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class AuthSpringSecurityImpl implements Auth {
    @Override
    public Subject getSubject() {
        ApiKeyAuthentication authentication = (ApiKeyAuthentication) SecurityContextHolder.getContext().getAuthentication();
        return new Subject(authentication.getAccount());
    }
}
