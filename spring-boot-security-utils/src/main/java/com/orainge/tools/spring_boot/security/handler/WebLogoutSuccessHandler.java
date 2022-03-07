package com.orainge.tools.spring_boot.security.handler;

import com.orainge.tools.spring_boot.bean.http.ApiResult;
import com.orainge.tools.spring_boot.security.config.CustomSecurityConfig;
import com.orainge.tools.spring_boot.utils.http.HttpResponseUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Handler: 退出成功
 *
 * @author orainge
 * @since 2022/3/3
 */
@Component
@ConditionalOnMissingBean(WebLogoutSuccessHandler.class)
public class WebLogoutSuccessHandler implements LogoutSuccessHandler {
    @Resource
    private com.orainge.tools.spring_boot.security.interfaces.handler.LogoutSuccessHandler logoutSuccessHandler;

    @Resource
    private CustomSecurityConfig config;

    private static String logoutSuccessTips;

    @PostConstruct
    private void init() {
        logoutSuccessTips = config.getTips().getLogoutSuccess();
    }

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        // 执行 Handler
        logoutSuccessHandler.onSuccess(request, response, authentication);

        // 返回退出登录成功信息
        HttpResponseUtils.writeBody(response, ApiResult.success().setMessage(logoutSuccessTips));
    }
}
