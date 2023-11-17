package codes.showme.server.auth;

import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.DefaultSessionManager;
import org.apache.shiro.session.mgt.SessionContext;

public class CodePlanetSessionManager extends DefaultSessionManager {
    @Override
    protected void onStart(Session session, SessionContext context) {
        super.onStart(session, context);
    }
}
