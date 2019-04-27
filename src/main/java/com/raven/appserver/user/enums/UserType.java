package com.raven.appserver.user.enums;

public enum UserType {
    ADMIN(0),

    USER(1);


    private int type;

    UserType(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }
}
