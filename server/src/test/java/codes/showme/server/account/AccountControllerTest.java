package codes.showme.server.account;

import codes.showme.server.AbstractIntegrationTest;
import codes.showme.server.Main;
import codes.showme.server.api.EnqueueController;
import codes.showme.techlib.cache.CacheService;
import codes.showme.techlib.json.JsonServiceJacksonImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = Main.class,
        useMainMethod = SpringBootTest.UseMainMethod.ALWAYS,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@ContextConfiguration(initializers = AccountControllerTest.Initializer.class)
public class AccountControllerTest extends AbstractIntegrationTest {

    @Autowired
    private AccountController accountController;

    @Autowired
    private EnqueueController enqueueController;

    @Autowired
    private CacheService cacheService;

    @Autowired
    private MockMvc mvc;

    @LocalServerPort
    private int port;

    @Test
    public void testSignUp() throws Exception {
        String email = "abc@example.com";
        String password = "password";
        SignUpReq signUpReq = new SignUpReq();
        signUpReq.setEmail(email);
        signUpReq.setPassword(password);

        mvc.perform(MockMvcRequestBuilders.post(AccountController.API_URI_SIGN_UP)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new JsonServiceJacksonImpl().toJsonString(signUpReq))
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk());

        String code = cacheService.getValue(email);

        mvc.perform(MockMvcRequestBuilders.get(AccountController.API_URI_SIGNUP_EMAIL_VALIDATE)
                .param("code", code).param("email", email).accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk());

        assertNull(cacheService.getValue(email));



        SignInReq incorrectSignInReq = new SignInReq();
        incorrectSignInReq.setEmail(email);
        incorrectSignInReq.setPassword("123");
        mvc.perform(MockMvcRequestBuilders.post(AccountController.API_URI_SIGN_IN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new JsonServiceJacksonImpl().toJsonString(incorrectSignInReq))
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isUnauthorized());


    }

    static class Initializer implements
            ApplicationContextInitializer<ConfigurableApplicationContext> {
        @Override
        public void initialize(ConfigurableApplicationContext context) {
            TestPropertyValues.of(
                            "codeplanet.db.writeonly.url=" + getPostgreSQLContainer().getJdbcUrl(),
                            "codeplanet.db.writeonly.username=" + getPostgreSQLContainer().getUsername(),
                            "codeplanet.db.writeonly.password=" + getPostgreSQLContainer().getPassword(),
                            "codeplanet.db.writeonly.db_name=" + getPostgreSQLContainer().getDatabaseName(),
                            "codeplanet.db.writeonly.driver_class=" + getPostgreSQLContainer().getDriverClassName(),
                            "codeplanet.db.writeonly.packages=" + "codes.showme.domain",
                            "codeplanet.db.readonly.url=" + getPostgreSQLContainer().getJdbcUrl(),
                            "codeplanet.db.readonly.username=" + getPostgreSQLContainer().getUsername(),
                            "codeplanet.db.readonly.password=" + getPostgreSQLContainer().getPassword(),
                            "codeplanet.db.readonly.db_name=" + getPostgreSQLContainer().getDatabaseName(),
                            "codeplanet.db.readonly.driver_class=" + getPostgreSQLContainer().getDriverClassName(),
                            "codeplanet.db.readonly.packages=" + "codes.showme.domain"
                    )
                    .applyTo(context.getEnvironment());
        }
    }

}

