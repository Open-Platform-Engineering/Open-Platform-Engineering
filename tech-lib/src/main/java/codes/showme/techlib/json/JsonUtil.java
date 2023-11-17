package codes.showme.techlib.json;

import java.util.Map;

public interface JsonUtil {
    Map<String, Object> toMap(Object object);

    <T> T fromMap(Map<String, Object> map, Class<T> clasz);

    String toJsonString(Object object);

    class JsonProcessingException extends RuntimeException {
        public JsonProcessingException(Throwable cause) {
            super(cause);
        }
    }
}
