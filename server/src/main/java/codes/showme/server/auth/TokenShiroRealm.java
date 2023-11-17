package codes.showme.server.auth;

import com.google.common.base.Strings;
import com.google.common.collect.Sets;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TokenShiroRealm extends AuthorizingRealm {

    private static final Logger logger = LoggerFactory.getLogger(TokenShiroRealm.class);

    public boolean supports(AuthenticationToken token) {
        return token != null && token instanceof Token;
    }
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        Token token = (Token) principals.getPrimaryPrincipal();
        String tokenValue = token.getToken();
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        authorizationInfo.setRoles(Sets.newHashSet(""));
        authorizationInfo.setStringPermissions(Sets.newHashSet(""));
        return authorizationInfo;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        Token token = (Token) authenticationToken;
        if (token == null) {
            throw new AuthenticationException("jwtToken不能为空");
        }
        String salt = "jwtToken.getSalt()";
        if (Strings.isNullOrEmpty(salt)) {
            throw new AuthenticationException("salt不能为空");
        }
        return new SimpleAuthenticationInfo(
                token,
                salt,
                getName()
        );
    }
}
