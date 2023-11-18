package codes.showme.techlib.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class JsonUtilJacksonImpl implements codes.showme.techlib.json.JsonUtil {
    private static final Logger logger = LoggerFactory.getLogger(JsonUtilJacksonImpl.class);

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
        } catch (com.fasterxml.jackson.core.JsonProcessingException e) {
            logger.error("toJsonString error,o:{}", object, e);
            throw new JsonProcessingException(e);
        }
    }

    @Override
    public <T> T toObject(String str, Class<T> clasz) {
        final ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(str, clasz);
        } catch (com.fasterxml.jackson.core.JsonProcessingException e) {
            logger.error("toObject error,o:{}", str, e);
            throw new JsonProcessingException(e);
        }
    }
}
