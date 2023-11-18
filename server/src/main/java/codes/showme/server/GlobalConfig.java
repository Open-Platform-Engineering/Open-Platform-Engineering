package codes.showme.server;

import codes.showme.techlib.json.JsonUtil;
import codes.showme.techlib.json.JsonUtilJacksonImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GlobalConfig {

    @Bean
    public JsonUtil jsonUtil(){
        return new JsonUtilJacksonImpl();
    }

}
