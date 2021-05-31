package com.devin.dev.utils;

public enum ResponseMessage {
    LOGIN_SUCCESS("로그인 성공"),
    LOGIN_FAIL("로그인 실패"),
    READ_USER("회원 정보 조회 성공"),
    NOT_FOUND_USER("회원을 찾을 수 없습니다."),
    CREATED_USER("회원 가입 성공"),
    UPDATE_USER("회원 정보 수정 성공"),
    DELETE_USER("회원 탈퇴 성공"),
    INTERNAL_SERVER_ERROR("서버 내부 에러"),
    EXIST_USER_MAIL("이미 존재하는 회원 메일"),
    DB_ERROR("데이터베이스 에러");

    ResponseMessage(String message) {
    }
}
