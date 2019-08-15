package com.raven.appserver.common;

public enum MessageType {
    MESSAGE(0),

    NOTIFY(1),

    COMMAND(2);


    private int type;

    MessageType(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }
}
