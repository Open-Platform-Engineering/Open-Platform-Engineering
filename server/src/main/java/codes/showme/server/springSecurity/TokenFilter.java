package codes.showme.server.springSecurity;

import codes.showme.server.account.authentication.Token;
import codes.showme.techlib.cache.CacheService;
import codes.showme.techlib.ioc.InstanceFactory;
import com.google.common.base.Strings;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

public class TokenFilter extends OncePerRequestFilter {
    private static final Logger logger = LoggerFactory.getLogger(TokenFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        boolean tokenNotExists = request.getHeader("token") == null;
        if (tokenNotExists) {
            filterChain.doFilter(request, response);
            return;
        }

        final String tokenKey = request.getHeader("token");
        if (Strings.isNullOrEmpty(tokenKey)) {
            throw new AccessDeniedException("token is empty");
        }
        if (tokenKey != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            Optional<Token> token = Token.getByTokenKey(tokenKey);
            if (token.isEmpty()) {
                throw new AccessDeniedException("token is empty from cache");
            }
            Token tokenValue = token.get();
            logger.debug("token filter email:{}", tokenValue.getEmail());

            if (!Strings.isNullOrEmpty(tokenValue.getEmail())) {
                UserDetails userDetails = new UserDetailsServiceImpl().loadUserByUsername(tokenValue.getEmail());

                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            } else {
                logger.warn("token is incorrect:{}", tokenKey);
                throw new AccessDeniedException("token is incorrect");
            }
        }
        filterChain.doFilter(request, response);
    }
}
