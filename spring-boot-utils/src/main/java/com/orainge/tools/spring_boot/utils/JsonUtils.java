package com.orainge.tools.spring_boot.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.stereotype.Component;
import org.springframework.ui.ModelMap;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.Map;

/**
 * JSON 工具类
 *
 * @author orainge
 * @since 2022/3/2
 */
@Component
@ConditionalOnMissingBean(JsonUtils.class)
public class JsonUtils {
    @Resource
    private ObjectMapper objectMapperBean;

    private static ObjectMapper objectMapper = null;

    @PostConstruct
    public void init() {
        objectMapper = objectMapperBean;
        if (objectMapper == null) {
            objectMapper = new ObjectMapper();
        }
    }

    public static String toJSONString(Object obj) {
        try {
            return obj == null ? null : objectMapper.writeValueAsString(obj);
        } catch (Exception ignore) {
            return null;
        }
    }

    public static <T> T parseObject(String text, Class<T> clazz) {
        try {
            return StringUtils.isBlank(text) || clazz == null ? null : objectMapper.readValue(text, clazz);
        } catch (Exception ignore) {
            return null;
        }
    }

    public static Map<String, Object> parseObjectToMap(String text) {
        try {
            return StringUtils.isBlank(text) ? null : objectMapper.readValue(text, ModelMap.class);
        } catch (Exception ignore) {
            return null;
        }
    }
}
