package codes.showme.techlib.json;

import java.util.Map;

public interface JsonUtil {
    Map<String, Object> toMap(Object object);

    <T> T fromMap(Map<String, Object> map, Class<T> clasz);

    String toJsonString(Object object);

    <T> T toObject(String str, Class<T> clasz);

    class JsonProcessingException extends RuntimeException {
        public JsonProcessingException(Throwable cause) {
            super(cause);
        }
    }
}
