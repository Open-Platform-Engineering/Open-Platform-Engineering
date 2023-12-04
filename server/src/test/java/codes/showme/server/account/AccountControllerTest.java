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

import java.util.List;

import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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

        // list teams
        String teamListContent = mvc.perform(MockMvcRequestBuilders.get(TeamController.URL_LIST_TEAMS)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("token", signResp.getHeader("token"))
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

        JsonUtil jsonUtil = InstanceFactory.getInstance(JsonUtil.class);
        Pagination<Team> pagination = jsonUtil.toObject(teamListContent, Pagination.class);
        assertEquals(30, pagination.getTotalRecord());

        // search teams
        String searchKey = "team";
        String teamSearchResult = mvc.perform(MockMvcRequestBuilders.get(TeamController.URL_SEARCH_TEAMS)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("token", signResp.getHeader("token"))
                        .param("q", searchKey)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        Pagination<Team> searchPagination = jsonUtil.toObject(teamSearchResult, Pagination.class);
        assertEquals(30, searchPagination.getTotalRecord());

        // list zoneids
        mvc.perform(MockMvcRequestBuilders.get(ScheduleController.URL_ZONEIDS)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("token", signResp.getHeader("token"))
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk()).andExpect(content().string("[\"Asia/Aden\",\"America/Cuiaba\",\"Etc/GMT+9\",\"Etc/GMT+8\",\"Africa/Nairobi\",\"America/Marigot\",\"Asia/Aqtau\",\"Pacific/Kwajalein\",\"America/El_Salvador\",\"Asia/Pontianak\",\"Africa/Cairo\",\"Pacific/Pago_Pago\",\"Africa/Mbabane\",\"Asia/Kuching\",\"Pacific/Honolulu\",\"Pacific/Rarotonga\",\"America/Guatemala\",\"Australia/Hobart\",\"Europe/London\",\"America/Belize\",\"America/Panama\",\"Asia/Chungking\",\"America/Managua\",\"America/Indiana/Petersburg\",\"Asia/Yerevan\",\"Europe/Brussels\",\"GMT\",\"Europe/Warsaw\",\"America/Chicago\",\"Asia/Kashgar\",\"Chile/Continental\",\"Pacific/Yap\",\"CET\",\"Etc/GMT-1\",\"Etc/GMT-0\",\"Europe/Jersey\",\"America/Tegucigalpa\",\"Etc/GMT-5\",\"Europe/Istanbul\",\"America/Eirunepe\",\"Etc/GMT-4\",\"America/Miquelon\",\"Etc/GMT-3\",\"Europe/Luxembourg\",\"Etc/GMT-2\",\"Etc/GMT-9\",\"America/Argentina/Catamarca\",\"Etc/GMT-8\",\"Etc/GMT-7\",\"Etc/GMT-6\",\"Europe/Zaporozhye\",\"Canada/Yukon\",\"Canada/Atlantic\",\"Atlantic/St_Helena\",\"Australia/Tasmania\",\"Libya\",\"Europe/Guernsey\",\"America/Grand_Turk\",\"Asia/Samarkand\",\"America/Argentina/Cordoba\",\"Asia/Phnom_Penh\",\"Africa/Kigali\",\"Asia/Almaty\",\"US/Alaska\",\"Asia/Dubai\",\"Europe/Isle_of_Man\",\"America/Araguaina\",\"Cuba\",\"Asia/Novosibirsk\",\"America/Argentina/Salta\",\"Etc/GMT+3\",\"Africa/Tunis\",\"Etc/GMT+2\",\"Etc/GMT+1\",\"Pacific/Fakaofo\",\"Africa/Tripoli\",\"Etc/GMT+0\",\"Israel\",\"Africa/Banjul\",\"Etc/GMT+7\",\"Indian/Comoro\",\"Etc/GMT+6\",\"Etc/GMT+5\",\"Etc/GMT+4\",\"Pacific/Port_Moresby\",\"US/Arizona\",\"Antarctica/Syowa\",\"Indian/Reunion\",\"Pacific/Palau\",\"Europe/Kaliningrad\",\"America/Montevideo\",\"Africa/Windhoek\",\"Asia/Karachi\",\"Africa/Mogadishu\",\"Australia/Perth\",\"Brazil/East\",\"Etc/GMT\",\"Asia/Chita\",\"Pacific/Easter\",\"Antarctica/Davis\",\"Antarctica/McMurdo\",\"Asia/Macao\",\"America/Manaus\",\"Africa/Freetown\",\"Europe/Bucharest\",\"Asia/Tomsk\",\"America/Argentina/Mendoza\",\"Asia/Macau\",\"Europe/Malta\",\"Mexico/BajaSur\",\"Pacific/Tahiti\",\"Africa/Asmera\",\"Europe/Busingen\",\"America/Argentina/Rio_Gallegos\",\"Africa/Malabo\",\"Europe/Skopje\",\"America/Catamarca\",\"America/Godthab\",\"Europe/Sarajevo\",\"Australia/ACT\",\"GB-Eire\",\"Africa/Lagos\",\"America/Cordoba\",\"Europe/Rome\",\"Asia/Dacca\",\"Indian/Mauritius\",\"Pacific/Samoa\",\"America/Regina\",\"America/Fort_Wayne\",\"America/Dawson_Creek\",\"Africa/Algiers\",\"Europe/Mariehamn\",\"America/St_Johns\",\"America/St_Thomas\",\"Europe/Zurich\",\"America/Anguilla\",\"Asia/Dili\",\"America/Denver\",\"Africa/Bamako\",\"Europe/Saratov\",\"GB\",\"Mexico/General\",\"Pacific/Wallis\",\"Europe/Gibraltar\",\"Africa/Conakry\",\"Africa/Lubumbashi\",\"Asia/Istanbul\",\"America/Havana\",\"NZ-CHAT\",\"Asia/Choibalsan\",\"America/Porto_Acre\",\"Asia/Omsk\",\"Europe/Vaduz\",\"US/Michigan\",\"Asia/Dhaka\",\"America/Barbados\",\"Europe/Tiraspol\",\"Atlantic/Cape_Verde\",\"Asia/Yekaterinburg\",\"America/Louisville\",\"Pacific/Johnston\",\"Pacific/Chatham\",\"Europe/Ljubljana\",\"America/Sao_Paulo\",\"Asia/Jayapura\",\"America/Curacao\",\"Asia/Dushanbe\",\"America/Guyana\",\"America/Guayaquil\",\"America/Martinique\",\"Portugal\",\"Europe/Berlin\",\"Europe/Moscow\",\"Europe/Chisinau\",\"America/Puerto_Rico\",\"America/Rankin_Inlet\",\"Pacific/Ponape\",\"Europe/Stockholm\",\"Europe/Budapest\",\"America/Argentina/Jujuy\",\"Australia/Eucla\",\"Asia/Shanghai\",\"Universal\",\"Europe/Zagreb\",\"America/Port_of_Spain\",\"Europe/Helsinki\",\"Asia/Beirut\",\"Asia/Tel_Aviv\",\"Pacific/Bougainville\",\"US/Central\",\"Africa/Sao_Tome\",\"Indian/Chagos\",\"America/Cayenne\",\"Asia/Yakutsk\",\"Pacific/Galapagos\",\"Australia/North\",\"Europe/Paris\",\"Africa/Ndjamena\",\"Pacific/Fiji\",\"America/Rainy_River\",\"Indian/Maldives\",\"Australia/Yancowinna\",\"SystemV/AST4\",\"Asia/Oral\",\"America/Yellowknife\",\"Pacific/Enderbury\",\"America/Juneau\",\"Australia/Victoria\",\"America/Indiana/Vevay\",\"Asia/Tashkent\",\"Asia/Jakarta\",\"Africa/Ceuta\",\"Asia/Barnaul\",\"America/Recife\",\"America/Buenos_Aires\",\"America/Noronha\",\"America/Swift_Current\",\"Australia/Adelaide\",\"America/Metlakatla\",\"Africa/Djibouti\",\"America/Paramaribo\",\"Asia/Qostanay\",\"Europe/Simferopol\",\"Europe/Sofia\",\"Africa/Nouakchott\",\"Europe/Prague\",\"America/Indiana/Vincennes\",\"Antarctica/Mawson\",\"America/Kralendijk\",\"Antarctica/Troll\",\"Europe/Samara\",\"Indian/Christmas\",\"America/Antigua\",\"Pacific/Gambier\",\"America/Indianapolis\",\"America/Inuvik\",\"America/Iqaluit\",\"Pacific/Funafuti\",\"UTC\",\"Antarctica/Macquarie\",\"Canada/Pacific\",\"America/Moncton\",\"Africa/Gaborone\",\"Pacific/Chuuk\",\"Asia/Pyongyang\",\"America/St_Vincent\",\"Asia/Gaza\",\"Etc/Universal\",\"PST8PDT\",\"Atlantic/Faeroe\",\"Asia/Qyzylorda\",\"Canada/Newfoundland\",\"America/Kentucky/Louisville\",\"America/Yakutat\",\"America/Ciudad_Juarez\",\"Asia/Ho_Chi_Minh\",\"Antarctica/Casey\",\"Europe/Copenhagen\",\"Africa/Asmara\",\"Atlantic/Azores\",\"Europe/Vienna\",\"ROK\",\"Pacific/Pitcairn\",\"America/Mazatlan\",\"Australia/Queensland\",\"Pacific/Nauru\",\"Europe/Tirane\",\"Asia/Kolkata\",\"SystemV/MST7\",\"Australia/Canberra\",\"MET\",\"Australia/Broken_Hill\",\"Europe/Riga\",\"America/Dominica\",\"Africa/Abidjan\",\"America/Mendoza\",\"America/Santarem\",\"Kwajalein\",\"America/Asuncion\",\"Asia/Ulan_Bator\",\"NZ\",\"America/Boise\",\"Australia/Currie\",\"EST5EDT\",\"Pacific/Guam\",\"Pacific/Wake\",\"Atlantic/Bermuda\",\"America/Costa_Rica\",\"America/Dawson\",\"Asia/Chongqing\",\"Eire\",\"Europe/Amsterdam\",\"America/Indiana/Knox\",\"America/North_Dakota/Beulah\",\"Africa/Accra\",\"Atlantic/Faroe\",\"Mexico/BajaNorte\",\"America/Maceio\",\"Etc/UCT\",\"Pacific/Apia\",\"GMT0\",\"America/Atka\",\"Pacific/Niue\",\"Australia/Lord_Howe\",\"Europe/Dublin\",\"Pacific/Truk\",\"MST7MDT\",\"America/Monterrey\",\"America/Nassau\",\"America/Jamaica\",\"Asia/Bishkek\",\"America/Atikokan\",\"Atlantic/Stanley\",\"Australia/NSW\",\"US/Hawaii\",\"SystemV/CST6\",\"Indian/Mahe\",\"Asia/Aqtobe\",\"America/Sitka\",\"Asia/Vladivostok\",\"Africa/Libreville\",\"Africa/Maputo\",\"Zulu\",\"America/Kentucky/Monticello\",\"Africa/El_Aaiun\",\"Africa/Ouagadougou\",\"America/Coral_Harbour\",\"Pacific/Marquesas\",\"Brazil/West\",\"America/Aruba\",\"America/North_Dakota/Center\",\"America/Cayman\",\"Asia/Ulaanbaatar\",\"Asia/Baghdad\",\"Europe/San_Marino\",\"America/Indiana/Tell_City\",\"America/Tijuana\",\"Pacific/Saipan\",\"SystemV/YST9\",\"Africa/Douala\",\"America/Chihuahua\",\"America/Ojinaga\",\"Asia/Hovd\",\"America/Anchorage\",\"Chile/EasterIsland\",\"America/Halifax\",\"Antarctica/Rothera\",\"America/Indiana/Indianapolis\",\"US/Mountain\",\"Asia/Damascus\",\"America/Argentina/San_Luis\",\"America/Santiago\",\"Asia/Baku\",\"America/Argentina/Ushuaia\",\"Atlantic/Reykjavik\",\"Africa/Brazzaville\",\"Africa/Porto-Novo\",\"America/La_Paz\",\"Antarctica/DumontDUrville\",\"Asia/Taipei\",\"Antarctica/South_Pole\",\"Asia/Manila\",\"Asia/Bangkok\",\"Africa/Dar_es_Salaam\",\"Poland\",\"Atlantic/Madeira\",\"Antarctica/Palmer\",\"America/Thunder_Bay\",\"Africa/Addis_Ababa\",\"Asia/Yangon\",\"Europe/Uzhgorod\",\"Brazil/DeNoronha\",\"Asia/Ashkhabad\",\"Etc/Zulu\",\"America/Indiana/Marengo\",\"America/Creston\",\"America/Punta_Arenas\",\"America/Mexico_City\",\"Antarctica/Vostok\",\"Asia/Jerusalem\",\"Europe/Andorra\",\"US/Samoa\",\"PRC\",\"Asia/Vientiane\",\"Pacific/Kiritimati\",\"America/Matamoros\",\"America/Blanc-Sablon\",\"Asia/Riyadh\",\"Iceland\",\"Pacific/Pohnpei\",\"Asia/Ujung_Pandang\",\"Atlantic/South_Georgia\",\"Europe/Lisbon\",\"Asia/Harbin\",\"Europe/Oslo\",\"Asia/Novokuznetsk\",\"CST6CDT\",\"Atlantic/Canary\",\"America/Knox_IN\",\"Asia/Kuwait\",\"SystemV/HST10\",\"Pacific/Efate\",\"Africa/Lome\",\"America/Bogota\",\"America/Menominee\",\"America/Adak\",\"Pacific/Norfolk\",\"Europe/Kirov\",\"America/Resolute\",\"Pacific/Kanton\",\"Pacific/Tarawa\",\"Africa/Kampala\",\"Asia/Krasnoyarsk\",\"Greenwich\",\"SystemV/EST5\",\"America/Edmonton\",\"Europe/Podgorica\",\"Australia/South\",\"Canada/Central\",\"Africa/Bujumbura\",\"America/Santo_Domingo\",\"US/Eastern\",\"Europe/Minsk\",\"Pacific/Auckland\",\"Africa/Casablanca\",\"America/Glace_Bay\",\"Canada/Eastern\",\"Asia/Qatar\",\"Europe/Kiev\",\"Singapore\",\"Asia/Magadan\",\"SystemV/PST8\",\"America/Port-au-Prince\",\"Europe/Belfast\",\"America/St_Barthelemy\",\"Asia/Ashgabat\",\"Africa/Luanda\",\"America/Nipigon\",\"Atlantic/Jan_Mayen\",\"Brazil/Acre\",\"Asia/Muscat\",\"Asia/Bahrain\",\"Europe/Vilnius\",\"America/Fortaleza\",\"Etc/GMT0\",\"US/East-Indiana\",\"America/Hermosillo\",\"America/Cancun\",\"Africa/Maseru\",\"Pacific/Kosrae\",\"Africa/Kinshasa\",\"Asia/Kathmandu\",\"Asia/Seoul\",\"Australia/Sydney\",\"America/Lima\",\"Australia/LHI\",\"America/St_Lucia\",\"Europe/Madrid\",\"America/Bahia_Banderas\",\"America/Montserrat\",\"Asia/Brunei\",\"America/Santa_Isabel\",\"Canada/Mountain\",\"America/Cambridge_Bay\",\"Asia/Colombo\",\"Australia/West\",\"Indian/Antananarivo\",\"Australia/Brisbane\",\"Indian/Mayotte\",\"US/Indiana-Starke\",\"Asia/Urumqi\",\"US/Aleutian\",\"Europe/Volgograd\",\"America/Lower_Princes\",\"America/Vancouver\",\"Africa/Blantyre\",\"America/Rio_Branco\",\"America/Danmarkshavn\",\"America/Detroit\",\"America/Thule\",\"Africa/Lusaka\",\"Asia/Hong_Kong\",\"Iran\",\"America/Argentina/La_Rioja\",\"Africa/Dakar\",\"SystemV/CST6CDT\",\"America/Tortola\",\"America/Porto_Velho\",\"Asia/Sakhalin\",\"Etc/GMT+10\",\"America/Scoresbysund\",\"Asia/Kamchatka\",\"Asia/Thimbu\",\"Africa/Harare\",\"Etc/GMT+12\",\"Etc/GMT+11\",\"Navajo\",\"America/Nome\",\"Europe/Tallinn\",\"Turkey\",\"Africa/Khartoum\",\"Africa/Johannesburg\",\"Africa/Bangui\",\"Europe/Belgrade\",\"Jamaica\",\"Africa/Bissau\",\"Asia/Tehran\",\"WET\",\"Europe/Astrakhan\",\"Africa/Juba\",\"America/Campo_Grande\",\"America/Belem\",\"Etc/Greenwich\",\"Asia/Saigon\",\"America/Ensenada\",\"Pacific/Midway\",\"America/Jujuy\",\"Africa/Timbuktu\",\"America/Bahia\",\"America/Goose_Bay\",\"America/Virgin\",\"America/Pangnirtung\",\"Asia/Katmandu\",\"America/Phoenix\",\"Africa/Niamey\",\"America/Whitehorse\",\"Pacific/Noumea\",\"Asia/Tbilisi\",\"Europe/Kyiv\",\"America/Montreal\",\"Asia/Makassar\",\"America/Argentina/San_Juan\",\"Hongkong\",\"UCT\",\"Asia/Nicosia\",\"America/Indiana/Winamac\",\"SystemV/MST7MDT\",\"America/Argentina/ComodRivadavia\",\"America/Boa_Vista\",\"America/Grenada\",\"Asia/Atyrau\",\"Australia/Darwin\",\"Asia/Khandyga\",\"Asia/Kuala_Lumpur\",\"Asia/Famagusta\",\"Asia/Thimphu\",\"Asia/Rangoon\",\"Europe/Bratislava\",\"Asia/Calcutta\",\"America/Argentina/Tucuman\",\"Asia/Kabul\",\"Indian/Cocos\",\"Japan\",\"Pacific/Tongatapu\",\"America/New_York\",\"Etc/GMT-12\",\"Etc/GMT-11\",\"America/Nuuk\",\"Etc/GMT-10\",\"SystemV/YST9YDT\",\"Europe/Ulyanovsk\",\"Etc/GMT-14\",\"Etc/GMT-13\",\"W-SU\",\"America/Merida\",\"EET\",\"America/Rosario\",\"Canada/Saskatchewan\",\"America/St_Kitts\",\"Arctic/Longyearbyen\",\"America/Fort_Nelson\",\"America/Caracas\",\"America/Guadeloupe\",\"Asia/Hebron\",\"Indian/Kerguelen\",\"SystemV/PST8PDT\",\"Africa/Monrovia\",\"Asia/Ust-Nera\",\"Egypt\",\"Asia/Srednekolymsk\",\"America/North_Dakota/New_Salem\",\"Asia/Anadyr\",\"Australia/Melbourne\",\"Asia/Irkutsk\",\"America/Shiprock\",\"America/Winnipeg\",\"Europe/Vatican\",\"Asia/Amman\",\"Etc/UTC\",\"SystemV/AST4ADT\",\"Asia/Tokyo\",\"America/Toronto\",\"Asia/Singapore\",\"Australia/Lindeman\",\"America/Los_Angeles\",\"SystemV/EST5EDT\",\"Pacific/Majuro\",\"America/Argentina/Buenos_Aires\",\"Europe/Nicosia\",\"Pacific/Guadalcanal\",\"Europe/Athens\",\"US/Pacific\",\"Europe/Monaco\"]"));

        // create schedule
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

