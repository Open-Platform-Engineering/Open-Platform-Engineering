package codes.showme.server.springSecurity;

import codes.showme.server.account.authentication.Token;
import codes.showme.techlib.ioc.InstanceFactory;
import com.google.common.base.Strings;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Optional;

public class TokenFilter extends GenericFilterBean {
    private static final Logger logger = LoggerFactory.getLogger(TokenFilter.class);

    private static final String AUTH_TOKEN_HEADER_NAME = "token";

    private Authentication getAuthentication(HttpServletRequest request) {
        String tokenHeader = request.getHeader(AUTH_TOKEN_HEADER_NAME);
        Optional<Token> token = Token.getByTokenKey(tokenHeader);
        if (token.isEmpty()) {
            logger.warn("The token is not valid:{}", tokenHeader);
            throw new BadCredentialsException("API Key is empty");
        }
        Token tokenValue = token.get();
        if (!Strings.isNullOrEmpty(tokenValue.getEmail())) {
            UserDetailsService userDetailsService = InstanceFactory.getInstance(UserDetailsService.class);
            CustomAccountUserDetails userDetails = (CustomAccountUserDetails)userDetailsService.loadUserByUsername(tokenValue.getEmail());
            if (!userDetails.isEnabled()) {
                throw new AccessDeniedException("account is unable");
            } else {
                return new ApiKeyAuthentication(tokenHeader, userDetails.getAccount(), AuthorityUtils.NO_AUTHORITIES);
            }
        } else {
            logger.warn("token is incorrect:{}", tokenHeader);
            throw new AccessDeniedException("token is incorrect");
        }
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        try {
            String apiKey = ((HttpServletRequest) request).getHeader(AUTH_TOKEN_HEADER_NAME);
            if (apiKey == null) {
                chain.doFilter(request, response);
                return;
            }
            Authentication authentication = getAuthentication((HttpServletRequest) request);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            chain.doFilter(request, response);
        } catch (Exception exp) {
            HttpServletResponse httpResponse = (HttpServletResponse) response;
            httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            httpResponse.setContentType(MediaType.APPLICATION_JSON_VALUE);
            PrintWriter writer = httpResponse.getWriter();
            logger.error(exp.getMessage());
            writer.print("");
            writer.flush();
            writer.close();
        }
    }
}
