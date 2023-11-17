package codes.showme.server.auth;

import jakarta.servlet.http.HttpServletRequest;
import org.apache.shiro.web.filter.AccessControlFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

public class TokenFilter extends AccessControlFilter {
    private static final Logger logger = LoggerFactory.getLogger(TokenFilter.class);
    public static final String TOKEN_HEADER_NAME = "token";
//
//    @Override
//    protected boolean isLoginAttempt(ServletRequest request, ServletResponse response) {
//        HttpServletRequest req = (HttpServletRequest) request;
//        String authorization = req.getHeader(TOKEN_HEADER_NAME);
//        return authorization != null;
//    }
//
//    @Override
//    protected boolean executeLogin(ServletRequest request, ServletResponse response) throws Exception {
//        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
//        String tokenHeader = httpServletRequest.getHeader(TOKEN_HEADER_NAME);
//        Token token = new Token(tokenHeader, request.getRemoteAddr());
//        getSubject(request, response).login(token);
//        return true;
//    }
//
//    @Override
//    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
//        if (isLoginAttempt(request, response)) {
//            try {
//                executeLogin(request, response);
//            } catch (Exception e) {
//                response401(request, response);
//            }
//        }
//        return true;
//    }
//
//    @Override
//    protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {
//        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
//        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
//        httpServletResponse.setHeader("Access-control-Allow-Origin", httpServletRequest.getHeader("Origin"));
//        httpServletResponse.setHeader("Access-Control-Allow-Methods", "GET,POST,OPTIONS,PUT,DELETE");
//        httpServletResponse.setHeader("Access-Control-Allow-Headers", httpServletRequest.getHeader("Access-Control-Request-Headers"));
//        httpServletResponse.setHeader(TOKEN_HEADER_NAME, httpServletRequest.getHeader(TOKEN_HEADER_NAME));
//        if (httpServletRequest.getMethod().equals(RequestMethod.OPTIONS.name())) {
//            httpServletResponse.setStatus(HttpStatus.OK.value());
//            return false;
//        }
//        return super.preHandle(request, response);
//    }
//    /**
//     * Illege request foward to /401
//     */
//    private void response401(ServletRequest req, ServletResponse resp) {
//        try {
//            HttpServletResponse httpServletResponse = (HttpServletResponse) resp;
//            httpServletResponse.sendRedirect("/401");
//        } catch (IOException e) {
//            logger.error(e.getMessage());
//        }
//    }


    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) throws Exception {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        String tokenHeader = httpServletRequest.getHeader(TOKEN_HEADER_NAME);
        Token token = new Token(tokenHeader, request.getRemoteAddr());
        getSubject(request, response).login(token);
        return true;
    }

    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        return false;
    }
}
