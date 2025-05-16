package cn.edu.usst.cs.campusAid.util;

import io.micrometer.common.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;

import java.lang.reflect.Field;

@Slf4j
public class BizContentLoader {
    public static JSONObject bizContent(Object object) {
    JSONObject json = new JSONObject();
    for (Field field : object.getClass().getDeclaredFields()) {
        field.setAccessible(true);
        BizContentField annotation = field.getAnnotation(BizContentField.class);
        if (annotation == null) {
            log.debug("skipping field {}", field.getName());
            continue;
        }

        try {
            String key = annotation.key();
            key = key.isEmpty() ? field.getName() : key;

            Object value;
            for (; ; ) {
                value = field.get(object);
                if (value != null) break;
                if (!StringUtils.isBlank(annotation.value())) {
                    value = annotation.value();
                    break;
                }
                throw new IllegalArgumentException(
                        BizContentLoader.class + ": value is null for field " + field.getName()
                );
            }

            log.debug("processing field: {}, type: {}, value: {}", field.getName(), field.getType(), value);

            // 自动将数字类型转为字符串
            if (value instanceof Number) {
                value = value.toString();
            }

            json.put(key, value);
        } catch (IllegalAccessException e) {
            log.error("wrapped object error:", e);
        }
    }

    log.info("wrapped object:{}", json);
    return json;
}

}
