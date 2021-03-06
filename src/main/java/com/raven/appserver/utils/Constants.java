package com.raven.appserver.utils;

public class Constants {
    public static final long GLOBAL_SESSION_TIMEOUT = 604800000L;

    /* user identity*/
    public static final String USER_SUPER_ADMIN = "superadmin";

    /**
     * shiro
     */
    public static final String DEFAULT_ROLES_KEY = "roles";
    public static final String DEFAULT_PERMS_KEY = "perms";
    public static final String DEFAULT_ROLE = "user";
    public static final String DEFAULT_PWD_KEY = "password";

    // 用户身份唯一标识在Map中的key
    public static final String DEFAULT_IDENTITY_KEY = "username";
    public static final String DEFAULT_SALT_KEY = "salt";

    // Group message template.
    public static final String CREATE_GROUP_FORMAT = "%s created the group.";
    public static final String JOIN_GROUP_FORMAT = "%s invited %s to the group.";
    public static final String KICK_GROUP_FORMAT = "%s kicked %s from the group.";
    public static final String QUIT_GROUP_FORMAT = "%s quited the group.";
    public static final String DISMISS_GROUP_FORMAT = "%s dismissed the group.";
}
