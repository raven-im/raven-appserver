package com.raven.appserver.group.validator;

import com.raven.appserver.utils.RestResultCode;
import java.util.List;

public interface Validator {

    default boolean isValid(String key) {
        return false;
    }

    default boolean isValid(String key1, List<String> uids) {
        return false;
    }

    default RestResultCode errorCode() {
        return RestResultCode.COMMON_SERVER_ERROR;
    }
}
