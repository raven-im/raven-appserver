package com.raven.appserver.shiro.service;

import java.util.Collection;
import java.util.Map;

/**
 * @Author zxx
 * @Description
 * @Date Created on 2017/11/10
 */
public interface ShiroService {

    /**
     * 获取用户的角色和权限
     *
     * @return map
     */
    Map<String, Collection<String>> getUserRolesAndPerms(Object uniqueIdentity);

    /**
     * 获取用户的唯一标识和密码以及用户状态，此方法必须实现
     *
     * @return map
     * @link com.github.boot.shiro.realm.LoginRealm
     */
    Map<String, Object> getUserUniqueIdentityAndPassword(String userName);

}
