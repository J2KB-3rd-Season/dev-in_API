package com.devin.dev.utils;

public enum StatusCode {
    OK(200),
    CREATED(201),
    NO_CONTENT(204),
    BAD_REQUEST(400),
    UNAUTHORIZED(401),
    FORBIDDEN(403),
    NOT_FOUND(404), // 페이지가 없는 경우
    INTERNAL_SERVER_ERROR(500),
    SERVICE_UNAVAILABLE(503),
    DB_ERROR(600),
    SUCCESS(0),
    FAIL_AUTH(400001),
    NOT_ENOUGH_PARM(40002),
    NOT_EXIST(400003), // 찾으려는 정보가 없는 경우
    CONDITION_FAIL(40004),
    ;

    StatusCode(int code) {
    }
}
