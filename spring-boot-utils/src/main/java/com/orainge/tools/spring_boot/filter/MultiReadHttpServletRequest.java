package com.orainge.tools.spring_boot.filter;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * 多次读写 BODY 用 HTTP REQUEST <br>
 * 解决流只能读一次问题
 *
 * @author orainge
 * @since 2022/3/2
 */
@Slf4j
public class MultiReadHttpServletRequest extends HttpServletRequestWrapper {
    private final byte[] body;

    /**
     * 额外的参数
     */
    private Map<String, Object> extraParams;

    public MultiReadHttpServletRequest(HttpServletRequest request) throws IOException {
        super(request);
        this.body = getBodyString(request).getBytes(StandardCharsets.UTF_8);
    }

    public MultiReadHttpServletRequest(HttpServletRequest request, Map<String, Object> extraParams) throws IOException {
        super(request);
        this.body = getBodyString(request).getBytes(StandardCharsets.UTF_8);
        this.extraParams = extraParams;
    }

    /**
     * 获取额外的参数
     *
     * @param key 参数 Key
     * @return 参数值
     */
    public Object getExtraParams(String key) {
        if (extraParams == null || key == null) {
            return null;
        } else {
            return extraParams.get(key);
        }
    }


    /**
     * 添加额外的参数
     *
     * @param key   参数 Key
     * @param value 参数值
     */
    public MultiReadHttpServletRequest addExtraParams(String key, Object value) {
        if (this.extraParams == null) {
            extraParams = new HashMap<>();
        }

        extraParams.put(key, value);

        return this;
    }

    @Override
    public BufferedReader getReader() throws IOException {
        return new BufferedReader(new InputStreamReader(getInputStream()));
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        final ByteArrayInputStream bais = new ByteArrayInputStream(body);

        return new ServletInputStream() {
            @Override
            public int read() throws IOException {
                return bais.read();
            }

            @Override
            public boolean isFinished() {
                return false;
            }

            @Override
            public boolean isReady() {
                return false;
            }

            @Override
            public void setReadListener(ReadListener readListener) {
            }
        };
    }

    /**
     * 获取请求Body
     */
    private String getBodyString(HttpServletRequest request) {
        StringBuilder sb = new StringBuilder();
        InputStream inputStream = null;
        BufferedReader reader = null;
        try {
            inputStream = request.getInputStream();
            reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
            String line = "";
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return sb.toString();
    }

    /**
     * 将前端传递的 json 数据转换成 json 字符串
     */
    public String getJsonBody() {
        StringBuilder json = new StringBuilder();
        String line;
        try {
            BufferedReader reader = this.getReader();
            while ((line = reader.readLine()) != null) {
                json.append(line);
            }
        } catch (Exception e) {
            log.error("请求参数转换错误!", e);
        }
        return json.toString();
    }
}
