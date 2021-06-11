package com.devin.dev.utils;

public enum StatusCode {
    SUCCESS(0),
    FAIL_AUTH(40001),
    NOT_ENOUGH_PARM(40002),
    NOT_EXIST(40003), // 찾으려는 정보가 없는 경우
    CONDITION_FAIL(40004),
    ;

    private final int code;

    StatusCode(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
