package com.tim.appserver.shiro.util;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.pam.AtLeastOneSuccessfulStrategy;
import org.apache.shiro.util.CollectionUtils;

/**
 * @Author zxx
 * @Description
 * @Date Created on 2017/11/11
 */
public class CustomAtLeastOneSuccessfulStrategy extends AtLeastOneSuccessfulStrategy {

    @Override
    public AuthenticationInfo afterAllAttempts(AuthenticationToken token,
        AuthenticationInfo aggregate) throws AuthenticationException {
        //we know if one or more were able to succesfully authenticate if the aggregated account object does not
        //contain null or empty data:
        if (aggregate == null || CollectionUtils.isEmpty(aggregate.getPrincipals())) {
            if (token instanceof UsernamePasswordToken) {
                throw new IncorrectCredentialsException();
            } else {
                throw new AuthenticationException(
                    "Authentication token of type [" + token.getClass() + "] " +
                        "could not be authenticated by any configured realms.  Please ensure that at least one realm can "
                        +
                        "authenticate these tokens.");
            }
        }
        return aggregate;
    }
}
