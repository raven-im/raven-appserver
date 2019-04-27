package com.raven.appserver.user.enums;

public enum UserState {

    NORMAL(0),

    DISABLE(1),

    DELETED(2);

    private int state;

    UserState(int state) {
        this.state = state;
    }

    public int getState() {
        return this.state;
    }
}

