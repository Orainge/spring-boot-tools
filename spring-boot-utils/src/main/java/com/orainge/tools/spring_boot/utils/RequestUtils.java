package com.orainge.tools.spring_boot.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * Http Request 工具类
 *
 * @author orainge
 * @since 2022/3/5
 */
@Slf4j
public class RequestUtils {
    /**
     * 获取请求参数
     *
     * @param request       HttpServletRequest
     * @param typeReference typeReference
     * @param <T>           生成的请求对象类型
     * @return null: 获取请求参数失败; 非 null: 请求参数
     */
    public <T> T getQueryParams(HttpServletRequest request, TypeReference<T> typeReference) {
        try {
            Map<String, String[]> requestParameterMap = request.getParameterMap();

            // 创建 Map
            Map<String, String> queryParamsMap = new HashMap<>();

            // 只获取每个参数的第一个值
            requestParameterMap.forEach((key, value) -> queryParamsMap.put(key, value[0]));

            // 先转换成 JSON 字符串
            String jsonStr = JsonUtils.toJSONString(queryParamsMap);

            // 再转换成 java 对象返回
            return JsonUtils.parseObject(jsonStr, new TypeReference<T>() {
            });
        } catch (Exception e) {
            log.info("[Http Request 工具] - 获取请求参数失败", e);
            return null;
        }
    }


    /**
     * 获取请求参数，返回 Map
     *
     * @param request HttpServletRequestÚ
     * @return null: 获取请求参数失败; 非 null: 请求参数
     */
    public Map<String, String> getQueryParamsMap(HttpServletRequest request) {
        try {
            Map<String, String[]> requestParameterMap = request.getParameterMap();

            // 创建 Map
            Map<String, String> queryParamsMap = new HashMap<>();

            // 只获取每个参数的第一个值
            requestParameterMap.forEach((key, value) -> queryParamsMap.put(key, value[0]));

            // 返回 java 对象
            return queryParamsMap;
        } catch (Exception e) {
            log.info("[Http Request 工具] - 获取请求参数失败", e);
            return null;
        }
    }
}
