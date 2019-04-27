package com.raven.appserver.common;

import com.raven.appserver.utils.ShiroUtils;
import com.raven.appserver.utils.JacksonUtils;
import com.raven.appserver.utils.RestResultCode;
import java.io.IOException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.shiro.web.filter.AccessControlFilter;
import org.apache.shiro.web.util.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMethod;

public class AuthFilter extends AccessControlFilter {
    private static final Logger logger = LoggerFactory.getLogger(AuthFilter.class);

    @Override
    public boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) throws IOException {
        if (request instanceof HttpServletRequest) {
            HttpServletRequest httpRequest = (HttpServletRequest) request;
            return RequestMethod.OPTIONS.name().equals(httpRequest.getMethod());
        }
        return false;
    }

    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        if (ShiroUtils.getSubject().isAuthenticated()) {
            return true;
        }
        onLoginFail(request, response);
        return false;
    }

    /**
     * 认证失败
     */
    private void onLoginFail(ServletRequest request, ServletResponse response) throws IOException {
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        httpResponse.setStatus(HttpServletResponse.SC_OK);

        RestResult r = RestResult.generate(RestResultCode.USER_USER_NOT_LOGIN);
        logger.info("global resources authentication failed, url={}, result={}",
            WebUtils.getRequestUri(httpRequest),
            JacksonUtils.toJSon(r));
        httpResponse.setContentType(MediaType.APPLICATION_JSON_VALUE);
        httpResponse.getWriter().write(JacksonUtils.toJSon(r));
    }
}
