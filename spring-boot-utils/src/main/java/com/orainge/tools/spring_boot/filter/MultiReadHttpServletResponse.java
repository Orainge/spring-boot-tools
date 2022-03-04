package com.orainge.tools.spring_boot.filter;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.servlet.ServletOutputStream;
import javax.servlet.WriteListener;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * 多次读写 BODY 用 HTTP RESPONSE <br>
 * 解决流只能读一次问题
 *
 * @author orainge
 * @since 2022/3/2
 */
public class MultiReadHttpServletResponse extends HttpServletResponseWrapper {
    private final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
    private final HttpServletResponse response;

    /**
     * 额外的参数
     */
    private Map<String, Object> extraParams;

    public MultiReadHttpServletResponse(HttpServletResponse response) {
        super(response);
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        this.response = response;
    }

    public MultiReadHttpServletResponse(HttpServletResponse response, Map<String, Object> extraParams) {
        super(response);
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        this.response = response;
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
    public MultiReadHttpServletResponse addExtraParams(String key, Object value) {
        if (this.extraParams == null) {
            extraParams = new HashMap<>();
        }

        extraParams.put(key, value);

        return this;
    }

    public byte[] getBody() {
        return byteArrayOutputStream.toByteArray();
    }

    @Override
    public ServletOutputStream getOutputStream() {
        return new ServletOutputStreamWrapper(this.byteArrayOutputStream, this.response);
    }

    @Override
    public PrintWriter getWriter() throws IOException {
        return new PrintWriter(new OutputStreamWriter(getOutputStream(), this.response.getCharacterEncoding()));
    }

    @EqualsAndHashCode(callSuper = true)
    @Data
    @AllArgsConstructor
    private static class ServletOutputStreamWrapper extends ServletOutputStream {
        private ByteArrayOutputStream outputStream;
        private HttpServletResponse response;

        @Override
        public boolean isReady() {
            return true;
        }

        @Override
        public void setWriteListener(WriteListener listener) {
        }

        @Override
        public void write(int b) throws IOException {
            this.outputStream.write(b);
        }

        @Override
        public void flush() throws IOException {
            if (!this.response.isCommitted()) {
                byte[] body = this.outputStream.toByteArray();
                ServletOutputStream outputStream = this.response.getOutputStream();
                outputStream.write(body);
                outputStream.flush();
            }
        }
    }
}