package codes.showme.server.debug;

import codes.showme.domain.account.AccountSignUpEventImpl;
import codes.showme.domain.team.AccountSignUpEvent;
import codes.showme.techlib.cache.CacheService;
import codes.showme.techlib.cache.CacheServiceMockImpl;
import codes.showme.techlib.email.EmailSender;
import codes.showme.techlib.email.EmailSenderMockImpl;
import codes.showme.techlib.hash.HashService;
import codes.showme.techlib.hash.HashServiceImpl;
import codes.showme.techlib.validation.ValidationCodeGeneration;
import codes.showme.techlib.validation.ValidationCodeGenerationImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DebugBeanConfig {

    @Bean
    public HashService hashService() {
        return new HashServiceImpl();
    }

    @Bean
    public CacheService cacheService() {
        return new CacheServiceMockImpl();
    }

    @Bean
    public AccountSignUpEvent accountSignUpEvent() {
        return new AccountSignUpEventImpl();
    }

    @Bean
    public EmailSender emailSender() {
        return new EmailSenderMockImpl();
    }

    @Bean
    public ValidationCodeGeneration validationCodeGeneration() {
        return new ValidationCodeGenerationImpl();
    }

}
