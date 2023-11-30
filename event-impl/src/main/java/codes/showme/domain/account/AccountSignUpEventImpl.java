package codes.showme.domain.account;

import codes.showme.domain.account.Account;
import codes.showme.domain.account.AccountSignUpEvent;
import codes.showme.techlib.cache.CacheService;
import codes.showme.techlib.email.EmailSender;
import codes.showme.techlib.ioc.InstanceFactory;
import codes.showme.techlib.validation.ValidationCodeGeneration;
import com.google.common.base.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

public class AccountSignUpEventImpl implements AccountSignUpEvent {
    private static final Logger logger = LoggerFactory.getLogger(AccountSignUpEventImpl.class);

    public static final int CODE_LENGTH = 6;
    public static final int CACHE_SECONDS = 10 * 60 * 60;


    @Override
    public boolean validateEmail(String email, String code) {
        if (Strings.isNullOrEmpty(code) || Strings.isNullOrEmpty(email)) {
            logger.warn("validateEmail email or code is empty,{},{}", code, email);
            return false;
        }
        CacheService cacheService = InstanceFactory.getInstance(CacheService.class);
        String cachedCode = cacheService.getValue(email);
        boolean pass = Objects.equals(cachedCode, code);
        if (pass) {
            Account.signUpSuccess(email);
            cacheService.remove(email);
        }
        return pass;
    }

    @Override
    public void fired(Account account) {
        // validate the account
        if (Objects.isNull(account)) {
            logger.warn("account is null");
            return;
        }
        // generate a validation code
        ValidationCodeGeneration codeGeneration = InstanceFactory.getInstance(ValidationCodeGeneration.class);
        String code = codeGeneration.generate(CODE_LENGTH);
        logger.info("generate account validation code:{},{}", account.getEmail(), code);

        // cache the code
        CacheService cacheService = InstanceFactory.getInstance(CacheService.class);
        cacheService.cache(account.getEmail(), code, CACHE_SECONDS);
        logger.info("account validation code cached:{},it will be cached in {}s", account.getEmail(), CACHE_SECONDS);

        // send email. this can be optimized to send a message to a mq
        EmailSender emailSender = InstanceFactory.getInstance(EmailSender.class);
        emailSender.send(account.getEmail(), code);
        logger.info("account validation have be sent:{}", account.getEmail());
    }

}
