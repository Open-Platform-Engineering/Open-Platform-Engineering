package codes.showme.server.auth;

import codes.showme.domain.team.Account;
import codes.showme.techlib.ioc.InstanceFactory;
import org.apache.shiro.authc.*;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;


public class UsernamePasswordShiroRealm extends AuthorizingRealm {

    private static final Logger logger = LoggerFactory.getLogger(UsernamePasswordShiroRealm.class);

    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof UsernamePasswordToken;
    }


    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        String email = (String) principalCollection.getPrimaryPrincipal();
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        return info;
    }

    @Override
    public void setCredentialsMatcher(CredentialsMatcher credentialsMatcher) {
        HashedCredentialsMatcher sh256CredentialsMatcher = new HashedCredentialsMatcher();
        ShiroAuthConfiguration authConfiguration = InstanceFactory.getInstance(ShiroAuthConfiguration.class);
        sh256CredentialsMatcher.setHashAlgorithmName(authConfiguration.getAlgorithmName());
        sh256CredentialsMatcher.setHashIterations(authConfiguration.getPasswordHashIterations());
        super.setCredentialsMatcher(sh256CredentialsMatcher);
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        String email = token.getPrincipal().toString();
        Optional<Account> accountOptional = Account.findByEmail(email);
        if (accountOptional.isEmpty()) {
            logger.warn("account not found:{}", email);
            throw new AuthenticationException();
        }

        Account account = accountOptional.get();

        if (!account.isEmailValidated()) {
            logger.warn("account not verified,value:{}:{}", account.isEmailValidated(), email);
            throw new UnverifiedEmailAccountException();
        }

        return new SimpleAuthenticationInfo(
                account.getEmail(),
                account.getPassword(),
                ByteSource.Util.bytes(account.getPasswordSalt()),
                "shiroRealm"
        );
    }
}