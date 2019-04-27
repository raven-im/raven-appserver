package com.raven.appserver.common;

public class BaseException extends Exception {

    private static final long serialVersionUID = -5161508214068953737L;

    public BaseException() {
    }

    public BaseException(String message) {
        super(message);
    }

}