package codes.showme.server.auth;

import codes.showme.techlib.hash.HashService;
import codes.showme.techlib.hash.HashServiceImpl;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.cache.MemoryConstrainedCacheManager;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.session.mgt.DefaultSessionManager;
import org.apache.shiro.spring.web.config.DefaultShiroFilterChainDefinition;
import org.apache.shiro.spring.web.config.ShiroFilterChainDefinition;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ShiroAuthConfiguration {

    @Value("${codeplanet.crypto.account.algorithmName}")
    private String algorithmName;

    @Value("${codeplanet.crypto.account.randomSaltNum}")
    private byte randomSaltNum;

    @Value("${codeplanet.crypto.account.hashIterations:3}")
    private int passwordHashIterations;

    @Bean
    public HashService hashService() {
        return new HashServiceImpl();
    }

    @Bean
    public SecurityManager securityManager() {
        ShiroRealm singleRealm = new ShiroRealm();
        HashedCredentialsMatcher hashedCredentialsMatcher = new HashedCredentialsMatcher(getAlgorithmName());
        hashedCredentialsMatcher.setHashIterations(getPasswordHashIterations());
        singleRealm.setCredentialsMatcher(hashedCredentialsMatcher);
        DefaultWebSecurityManager result = new DefaultWebSecurityManager(singleRealm);
        DefaultSessionManager sessionManager = new DefaultSessionManager();
        result.setSessionManager(sessionManager);
        SecurityUtils.setSecurityManager(result);
        return result;
    }

    @Bean
    protected CacheManager cacheManager() {
        return new MemoryConstrainedCacheManager();
    }

    @Bean
    public ShiroFilterChainDefinition shiroFilterChainDefinition() {

        DefaultShiroFilterChainDefinition chainDefinition = new DefaultShiroFilterChainDefinition();

        // logged in users with the 'document:read' permission
        chainDefinition.addPathDefinition("/v1/sign-in", "anon");
        chainDefinition.addPathDefinition("/v1/sign-up", "anon");
        chainDefinition.addPathDefinition("/v1/sign-out", "authc");
        chainDefinition.addPathDefinition("/v1/sign/up/email/validate", "anon");
        chainDefinition.addPathDefinition("/v2/enqueue", "anon");

        // all other paths require a logged in user
        chainDefinition.addPathDefinition("/**", "authc");
        return chainDefinition;
    }

    public byte getRandomSaltNum() {
        return randomSaltNum;
    }

    public void setRandomSaltNum(byte randomSaltNum) {
        this.randomSaltNum = randomSaltNum;
    }

    public String getAlgorithmName() {
        return algorithmName;
    }

    public void setAlgorithmName(String algorithmName) {
        this.algorithmName = algorithmName;
    }

    public int getPasswordHashIterations() {
        return passwordHashIterations;
    }

    public void setPasswordHashIterations(int passwordHashIterations) {
        this.passwordHashIterations = passwordHashIterations;
    }
}
