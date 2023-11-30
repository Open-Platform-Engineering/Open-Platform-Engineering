package codes.showme.server.account;

import codes.showme.domain.team.Team;
import codes.showme.server.AbstractIntegrationTest;
import codes.showme.server.Main;
import codes.showme.server.api.EnqueueController;
import codes.showme.server.schedule.CreateReq;
import codes.showme.server.schedule.ScheduleController;
import codes.showme.server.team.TeamController;
import codes.showme.techlib.cache.CacheService;
import codes.showme.techlib.ioc.InstanceFactory;
import codes.showme.techlib.json.JsonUtil;
import codes.showme.techlib.json.JsonUtilJacksonImpl;
import codes.showme.techlib.pagination.Pagination;
import org.junit.Assert;
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
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
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
    public void accountControllerTest() throws Exception {
        String email = "abc@example.com";
        String password = "password";
        SignUpReq signUpReq = new SignUpReq();
        signUpReq.setEmail(email);
        signUpReq.setPassword(password);
//
//        mvc.perform(MockMvcRequestBuilders.post(AccountController.API_URI_SIGN_OUT)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .accept(MediaType.APPLICATION_JSON)
//                )
//                .andExpect(status().isOk());

        mvc.perform(MockMvcRequestBuilders.post(AccountController.API_URI_SIGN_UP)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new JsonUtilJacksonImpl().toJsonString(signUpReq))
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk());

        String code = cacheService.getValue(email);

        EmailValidationReq emailValidationReq = new EmailValidationReq();
        emailValidationReq.setCode(code);
        emailValidationReq.setEmail(email);
        mvc.perform(MockMvcRequestBuilders.post(AccountController.API_URI_SIGNUP_EMAIL_VALIDATE)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new JsonUtilJacksonImpl().toJsonString(emailValidationReq)).accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk());

        assertNull(cacheService.getValue(email));


        SignInReq signInReq = new SignInReq();
        signInReq.setEmail(email);
        signInReq.setPassword(password);
        MockHttpServletResponse signResp = mvc.perform(MockMvcRequestBuilders.post(AccountController.API_URI_SIGN_IN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new JsonUtilJacksonImpl().toJsonString(signInReq))
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk()).andReturn().getResponse();

        assertNotNull(signResp.getHeader("token"));

        for (int i = 0; i < 30; i++) {
            String team = "team" + i;
            codes.showme.server.team.CreateReq createTeamReq = new codes.showme.server.team.CreateReq();
            createTeamReq.setName(team);
            mvc.perform(MockMvcRequestBuilders.post(TeamController.URL_CREATE_TEAM)
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("token", signResp.getHeader("token"))
                            .content(new JsonUtilJacksonImpl().toJsonString(createTeamReq))
                            .accept(MediaType.APPLICATION_JSON)
                    )
                    .andExpect(status().isOk()).andExpect(header().exists("id"));
        }

        String teamListContent = mvc.perform(MockMvcRequestBuilders.get(TeamController.URL_LIST_TEAMS)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("token", signResp.getHeader("token"))
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

        JsonUtil jsonUtil = InstanceFactory.getInstance(JsonUtil.class);
        Pagination pagination = jsonUtil.toObject(teamListContent, Pagination.class);
        assertEquals(30, pagination.getTotalRecord());

        CreateReq createScheduleReq = new CreateReq();
        createScheduleReq.setName("newSchdule");
        createScheduleReq.setDescription("descrition");
        createScheduleReq.setZoneId("Europe/London");
        mvc.perform(MockMvcRequestBuilders.post(ScheduleController.URL_CREATE_SCHEDULE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("token", signResp.getHeader("token"))
                        .content(new JsonUtilJacksonImpl().toJsonString(createScheduleReq))
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk()).andExpect(header().string("id", "1"));


        mvc.perform(MockMvcRequestBuilders.post(AccountController.API_URI_SIGN_OUT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("token", signResp.getHeader("token"))
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk());


        mvc.perform(MockMvcRequestBuilders.post(ScheduleController.URL_CREATE_SCHEDULE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("token", signResp.getHeader("token"))
                        .content(new JsonUtilJacksonImpl().toJsonString(createScheduleReq))
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

