package codes.showme.server.auth;

import codes.showme.techlib.hash.HashService;
import codes.showme.techlib.hash.HashServiceImpl;
import jakarta.servlet.DispatcherType;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.authc.pam.AtLeastOneSuccessfulStrategy;
import org.apache.shiro.authc.pam.ModularRealmAuthenticator;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.cache.MemoryConstrainedCacheManager;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.spring.web.config.DefaultShiroFilterChainDefinition;
import org.apache.shiro.spring.web.config.ShiroFilterChainDefinition;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.DelegatingFilterProxy;

import javax.servlet.Filter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
    public DefaultWebSecurityManager defaultWebSecurityManager() {
        UsernamePasswordShiroRealm singleRealm = new UsernamePasswordShiroRealm();
        HashedCredentialsMatcher hashedCredentialsMatcher = new HashedCredentialsMatcher(getAlgorithmName());
        hashedCredentialsMatcher.setHashIterations(getPasswordHashIterations());
        singleRealm.setCredentialsMatcher(hashedCredentialsMatcher);
        DefaultWebSecurityManager result = new DefaultWebSecurityManager();
        result.setAuthenticator(modularRealmAuthenticator());
        List<Realm> realms = new ArrayList<>(2);
        realms.add(singleRealm);
        realms.add(new TokenShiroRealm());
        result.setRealms(realms);

        result.setSessionManager(new CodePlanetSessionManager());
        SecurityUtils.setSecurityManager(result);
        return result;
    }

    @Bean
    protected CacheManager cacheManager() {
        return new MemoryConstrainedCacheManager();
    }

    @Bean
    public ModularRealmAuthenticator modularRealmAuthenticator() {
        CustomModularRealmAuthenticator authenticator = new CustomModularRealmAuthenticator();
        authenticator.setAuthenticationStrategy(new AtLeastOneSuccessfulStrategy());
        return authenticator;
    }

    @Bean
    public ShiroFilterChainDefinition shiroFilterChainDefinition() {
        DefaultShiroFilterChainDefinition chainDefinition = new DefaultShiroFilterChainDefinition();
        Map<String, String> filterChainDefinitions = new LinkedHashMap<>();
        filterChainDefinitions.put("/v1/sign-in", "anon");
        filterChainDefinitions.put("/v1/sign-up", "anon");
        filterChainDefinitions.put("/v1/sign/up/email/validate", "anon");
        filterChainDefinitions.put("/**", "token");
        chainDefinition.addPathDefinitions(filterChainDefinitions);
        return chainDefinition;
    }

//    @Bean
//    public FilterRegistrationBean registerJwtFilter(@Autowired TokenFilter jwtFilter) {
//        // 设置jwt filter不自动注册到spring管理的监听器中，防止与shiro filter同级，导致该监听器必定执行
//        FilterRegistrationBean<TokenFilter> jwtFilterRegister = new FilterRegistrationBean<>(jwtFilter);
//        jwtFilterRegister.setEnabled(false);
//
//        return jwtFilterRegister;
//    }

    @Bean(name = "shiroFilter")
    public ShiroFilterFactoryBean shiroFilterFactoryBean(DefaultWebSecurityManager securityManager) {
        ShiroFilterFactoryBean result = new ShiroFilterFactoryBean();
        result.setSecurityManager(securityManager);
        result.setLoginUrl("login");
        result.setSuccessUrl("system");
        result.setUnauthorizedUrl("/error?code=403");

        Map<String, Filter> filtersMap = new LinkedHashMap<>();
        filtersMap.put("token", new TokenFilter());
        result.setFilters(filtersMap);

        result.setFilterChainDefinitionMap(shiroFilterChainDefinition().getFilterChainMap());

        return result;
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
