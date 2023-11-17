package codes.showme.server.auth;

import org.apache.shiro.session.ExpiredSessionException;
import org.apache.shiro.session.InvalidSessionException;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.SessionContext;
import org.apache.shiro.session.mgt.SessionKey;
import org.apache.shiro.util.StringUtils;
import org.apache.shiro.web.servlet.ShiroHttpServletRequest;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.apache.shiro.web.util.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.Serializable;
import java.util.Objects;

public class CodePlanetWebSessionManager extends DefaultWebSessionManager {

    private static final Logger logger = LoggerFactory.getLogger(CodePlanetWebSessionManager.class);
    public static final String TOKEN = "token";
    public static final String SESSION_SOURCE = "header";


    @Override
    protected Serializable getSessionId(javax.servlet.ServletRequest request, javax.servlet.ServletResponse response) {
        if (!(request instanceof HttpServletRequest)) {
            logger.warn("the request is not a http request");
            return null;
        }

        HttpServletRequest httpServletRequest = WebUtils.toHttp(request);
        String token = httpServletRequest.getHeader(TOKEN);
        if (StringUtils.hasText(token)) {
            HttpServletResponse httpServletResponse = WebUtils.toHttp(response);
            request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID_SOURCE, SESSION_SOURCE);
            request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID, token);
            request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID_IS_VALID, true);
            request.setAttribute(ShiroHttpServletRequest.SESSION_ID_URL_REWRITING_ENABLED, false);
            return token;
        }
        request.setAttribute(ShiroHttpServletRequest.SESSION_ID_URL_REWRITING_ENABLED, false);
        return super.getSessionId(request, response);
    }

    public void storeSessionId(Serializable sessionId, HttpServletRequest request, HttpServletResponse response) {
        String sessionStr = sessionId.toString();
        response.setHeader(TOKEN, sessionStr);
    }

    @Override
    protected void onStop(Session session, SessionKey key) {
        super.onStop(session, key);
        if (WebUtils.isHttp(key)) {
            HttpServletResponse response = WebUtils.getHttpResponse(key);
            response.setHeader(TOKEN, null);
        }
    }

    @Override
    protected void onExpiration(Session s, ExpiredSessionException ese, SessionKey key) {
        super.onExpiration(s, ese, key);
        onInvalidation(s, ese, key);
    }

    @Override
    protected void onInvalidation(Session session, InvalidSessionException ise, SessionKey key) {
        super.onInvalidation(session, ise, key);
        if (WebUtils.isHttp(key)) {
            HttpServletRequest request = WebUtils.getHttpRequest(key);
            request.removeAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID_IS_VALID);
            HttpServletResponse response = WebUtils.getHttpResponse(key);
            response.setHeader(TOKEN, null);
        }
    }

    @Override
    protected void onStart(Session session, SessionContext context) {
        super.onStart(session, context);

        HttpServletRequest request = WebUtils.getHttpRequest(context);
        HttpServletResponse response = WebUtils.getHttpResponse(context);
        if (isSessionIdCookieEnabled()) {
            Serializable sessionId = session.getId();
            if (Objects.isNull(sessionId)) {
                logger.error("session id should no be null");
                throw new UnsupportedOperationException("session id should no be null");
            } else {
                storeSessionId(sessionId, request, response);
            }
        } else {
            logger.debug("session id cookie is disabled");
            return;
        }
        request.removeAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID_IS_VALID);
        request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_IS_NEW, true);

    }
}
