package codes.showme.server.springSecurity;

import codes.showme.server.account.authentication.TokenKeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Role;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.Collection;
import java.util.List;

import static org.springframework.http.HttpMethod.GET;
import static org.springframework.security.config.Customizer.withDefaults;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private static final String[] WHITE_LIST_URL = {"/v1/sign-in", "/v1/sign-up", "/v1/sign/up/email/validate"};

    //    @Bean
//    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http.authorizeHttpRequests(register -> register.anyRequest().access((authentication, object) -> {
//                    //表示请求的 URL 地址和数据库的地址是否匹配上了
//                    boolean isMatch = false;
//                    //获取当前请求的 URL 地址
//                    String requestURI = object.getRequest().getRequestURI();
//                    List<MenuWithRoleVO> menuWithRole = menuService.getMenuWithRole();
//                    for (MenuWithRoleVO m : menuWithRole) {
//                        if (antPathMatcher.match(m.getUrl(), requestURI)) {
//                            isMatch = true;
//                            //说明找到了请求的地址了
//                            //这就是当前请求需要的角色
//                            List<Role> roles = m.getRoles();
//                            //获取当前登录用户的角色
//                            Collection<? extends GrantedAuthority> authorities = authentication.get().getAuthorities();
//                            for (GrantedAuthority authority : authorities) {
//                                for (Role role : roles) {
//                                    if (authority.getAuthority().equals(role.getName())) {
//                                        //说明当前登录用户具备当前请求所需要的角色
//                                        return new AuthorizationDecision(true);
//                                    }
//                                }
//                            }
//                        }
//                    }
//                    if (!isMatch) {
//                        //说明请求的 URL 地址和数据库的地址没有匹配上，对于这种请求，统一只要登录就能访问
//                        if (authentication.get() instanceof AnonymousAuthenticationToken) {
//                            return new AuthorizationDecision(false);
//                        } else {
//                            return new AuthorizationDecision(true);
//                        }
//                    }
//                    return new AuthorizationDecision(false);
//                }))
//
//                .csrf(csrf ->
//                       csrf.disable()
//                )
////                .exceptionHandling(e ->
////                        //...
////                )
////                .logout(logout ->
////                        //...
////                );
//        return http.build();
//    }
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(req ->
                        req.requestMatchers(WHITE_LIST_URL).permitAll()
                                .anyRequest().authenticated()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(STATELESS))
                .authenticationProvider(new UsernamePasswordAuthenticationProvider())
                .logout(logout ->
                        logout.logoutUrl("/v1/sign-out")
                                .addLogoutHandler(new CodePlanetLogoutHandler())
                                .logoutSuccessHandler((request, response, authentication) -> SecurityContextHolder.clearContext())
                );
        http.addFilterBefore(new TokenFilter(), UsernamePasswordAuthenticationFilter.class);
        http.csrf(AbstractHttpConfigurer::disable);
        http.headers(httpSecurityHeadersConfigurer -> httpSecurityHeadersConfigurer.cacheControl(HeadersConfigurer.CacheControlConfig::disable));
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return new UserDetailsServiceImpl();
    }

    @Bean
    public TokenKeyGenerator tokenGenerator(){
        return new TokenGeneratorImpl();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager() {
        return new ProviderManager(authenticationProvider());
    }
}
