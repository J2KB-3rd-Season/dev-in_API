package com.devin.dev.utils;

public enum StatusCode {
    OK(0), // 요청에 대한 처리 성공
    CREATED(201), // POST 요청에 데이터가 잘 생성 되었을 때 응답코드
    NO_CONTENT(204), // 요청에 대한 처리는 성공했지만, 클라이언트가 현재 페이지에서 이동할 필요가 없을 때
    BAD_REQUEST(400), // 요청에 대한 값이 잘못되었을 때
    UNAUTHORIZED(401), // 이 요청을 하려면 로그인이 필요할 때
    FORBIDDEN(403), // 유저가 접근 불가능한 정보에 접근했을 때
    NOT_FOUND(404), // URI 가 존재하지 않음
    INTERNAL_SERVER_ERROR(500), // 서버에서 에러가 났을 때 전부. 500 이후 에러를 모두 포함한다
    SERVICE_UNAVAILABLE(503); // 서버에서 요청에 대한 처리가 불가능할 때

    private final int code;

    StatusCode(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
