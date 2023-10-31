package codes.showme.techlib.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class JsonServiceJacksonImpl implements JsonService {
    private static final Logger logger = LoggerFactory.getLogger(JsonServiceJacksonImpl.class);

    @Override
    public Map<String, Object> toMap(Object object) {
        final ObjectMapper mapper = new ObjectMapper();
        return mapper.convertValue(object, Map.class);
    }

    @Override
    public <T> T fromMap(Map<String, Object> map, Class<T> clasz) {
        final ObjectMapper mapper = new ObjectMapper();
        return mapper.convertValue(map, clasz);
    }

    @Override
    public String toJsonString(Object object) {
        final ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(object);
        }  catch (com.fasterxml.jackson.core.JsonProcessingException e) {
            logger.error("JsonServiceJacksonImpl toJsonString error,o:{}",object,e);
            throw new JsonProcessingException(e);
        }
    }
}
