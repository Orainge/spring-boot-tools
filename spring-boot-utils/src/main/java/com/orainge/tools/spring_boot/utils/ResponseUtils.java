package com.orainge.tools.spring_boot.utils;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;

import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Http Response 工具类
 *
 * @author orainge
 * @since 2022/3/2
 */
@Slf4j
public class ResponseUtils {
    /**
     * response 上输出 JSON
     */
    public static void writeBody(ServletResponse response, Object obj) {
        PrintWriter out = null;
        try {
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json");
            out = response.getWriter();
            out.println(JsonUtils.toJSONString(obj));
        } catch (Exception e) {
            log.error("Response 写入 Body 出错", e);
        } finally {
            if (out != null) {
                out.flush();
                out.close();
            }
        }
    }

    public static void writeBody(HttpServletResponse httpServletResponse, Object obj, HttpStatus httpStatus) {
        PrintWriter out = null;
        httpServletResponse.setCharacterEncoding("UTF-8");
        httpServletResponse.setContentType("application/json; charset=utf-8");
        httpServletResponse.setStatus(httpStatus.value());
        try {
            out = httpServletResponse.getWriter();
            out.print(JsonUtils.toJSONString(obj));
        } catch (IOException e) {
            log.error("Response 写入 Body 出错", e);
        } finally {
            if (out != null) {
                out.flush();
                out.close();
            }
        }
    }
}
