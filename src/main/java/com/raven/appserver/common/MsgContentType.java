package com.raven.appserver.common;

public enum MsgContentType {
    TEXT(0),

    IMAGE(1),

    VOICE(2),

    VIDEO(3);


    private int type;

    MsgContentType(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }
}
