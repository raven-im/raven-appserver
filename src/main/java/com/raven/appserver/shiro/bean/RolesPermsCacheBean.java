package com.raven.appserver.shiro.bean;

import com.raven.appserver.utils.JacksonUtils;
import java.util.List;

/**
 * @Author zxx
 * @Description
 * @Date Created on 2017/11/10
 */
public class RolesPermsCacheBean {

    private List<String> roles;

    private List<String> perms;

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    public List<String> getPerms() {
        return perms;
    }

    public void setPerms(List<String> perms) {
        this.perms = perms;
    }

    @Override
    public String toString() {
        return JacksonUtils.toJSon(this);
    }
}
