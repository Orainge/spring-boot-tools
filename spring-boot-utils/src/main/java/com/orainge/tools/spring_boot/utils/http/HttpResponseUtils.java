package com.orainge.tools.spring_boot.utils.http;

import com.orainge.tools.spring_boot.utils.JsonUtils;
import lombok.extern.slf4j.Slf4j;
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
public class HttpResponseUtils {
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

    public static void writeBody(ServletResponse servletResponse, Object obj, HttpStatus httpStatus) {
        PrintWriter out = null;
        servletResponse.setCharacterEncoding("UTF-8");
        servletResponse.setContentType("application/json; charset=utf-8");

        if (servletResponse instanceof HttpServletResponse) {
            ((HttpServletResponse) servletResponse).setStatus(httpStatus.value());
        }

        try {
            out = servletResponse.getWriter();
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
