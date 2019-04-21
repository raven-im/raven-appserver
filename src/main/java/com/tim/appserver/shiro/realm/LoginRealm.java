package com.tim.appserver.shiro.realm;

import com.tim.appserver.utils.Constants;
import java.util.Map;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.util.ByteSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Author zxx
 * @Description
 * @Date Created on 2017/11/10
 */
public class LoginRealm extends AuthorizationBaseRealm {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());


    /**
     * 认证-登录
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token)
        throws AuthenticationException {
        Map<String, Object> info = null;
        logger.info("doGetAuthenticationInfo");
        if (token instanceof UsernamePasswordToken) {
            UsernamePasswordToken authToken = (UsernamePasswordToken) token;
            info = shiroService.getUserUniqueIdentityAndPassword(authToken.getUsername());
        }
        boolean flag =
            info == null || info.isEmpty() || info.get(Constants.DEFAULT_IDENTITY_KEY) == null
                || info.get(Constants.DEFAULT_PWD_KEY) == null;
        if (!flag) {
            SimpleAuthenticationInfo simpleAuthenticationInfo = new SimpleAuthenticationInfo (
                info.get(Constants.DEFAULT_IDENTITY_KEY), info.get(Constants.DEFAULT_PWD_KEY),
                getName());
            simpleAuthenticationInfo.setCredentialsSalt(ByteSource.Util.bytes(info.get(Constants.DEFAULT_SALT_KEY)));
            logger.info("verify account success. username: {}", info.get(Constants.DEFAULT_IDENTITY_KEY));
            return simpleAuthenticationInfo;
        } else {
            // 没有找到账号
            throw new UnknownAccountException("UnknownAccountException");
        }
    }

    @Override
    public boolean supports(AuthenticationToken token) {
        return  (token instanceof UsernamePasswordToken);
    }
}
