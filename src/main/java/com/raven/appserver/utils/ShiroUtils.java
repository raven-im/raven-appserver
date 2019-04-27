package com.raven.appserver.utils;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.InvalidSessionException;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;

/**
 * @Author zxx
 * @Description
 * @Date Created on 2017/11/10
 */
public class ShiroUtils {

    public static String USER_ID = "user_id";
    public static String USER_UID = "user_uid";

    public static Session getSession() {
        Subject subject = getSubject();
        if (subject != null) {
            return subject.getSession(false);
        }
        return null;
    }

    public static Subject getSubject() {
        return SecurityUtils.getSubject();
    }

    public static Object getAttribute(Object key) throws InvalidSessionException {
        Session session = getSession();
        if (session != null) {
            return session.getAttribute(key);
        }
        return null;
    }

    public static void setAttribute(Object key, Object value) throws InvalidSessionException {
        Session session = getSession();
        if (session != null) {
            session.setAttribute(key, value);
        }
    }
}
