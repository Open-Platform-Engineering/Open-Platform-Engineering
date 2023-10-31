package codes.showme.techlib.email;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EmailSenderMockImpl implements codes.showme.techlib.email.EmailSender {

    private final static Logger logger = LoggerFactory.getLogger(EmailSenderMockImpl.class);

    @Override
    public void send(String email, String content) {
        logger.info("the {} email have been sent, content:{}", email, content);
    }
}
