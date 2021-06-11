package com.devin.dev.utils;

public enum ResponseMessage {
    LOGIN_SUCCESS("로그인 성공"),
    INCORRECT_PASSWORD("패스워드 오류"),
    LOGIN_FAIL("로그인 실패"),
    READ_USER("회원 정보 조회 성공"),
    INACTIVE_USER("비활성 유저 입니다."),
    NOT_FOUND_USER("회원을 찾을 수 없습니다."),
    NOT_FOUND_EMAIL("이메일을 찾을 수 없습니다."),
    NOT_FOUND_POST("게시글을 찾을 수 없습니다."),
    NOT_FOUND_REPLY("답변을 찾을 수 없습니다."),
    NOT_FOUND_SUBJECT("주제를 찾을 수 없습니다."),
    NOT_SAME_USER("작성자가 아닙니다."),
    SAME_USER("작성자 입니다."),
    CANNOT_DELETE_SELECTED("채택된 답변은 삭제할 수 없습니다."),
    CREATED_USER("회원 가입 성공"),
    UPDATE_USER("회원 정보 수정 성공"),
    DELETE_USER("회원 탈퇴 성공"),
    CHANGE_USER_STATUS("회원 상태 변경 성공"),
    FOUND_POST("게시글 검색 성공"),
    POST_UPLOAD_SUCCESS("게시글 등록 성공"),
    POST_EDIT_SUCCESS("게시글 수정 성공"),
    DELETED_POST("게시글 삭제 성공"),
    REPLY_UPLOAD_SUCCESS("답변 등록 성공"),
    REPLY_EDIT_SUCCESS("답변 수정 성공"),
    REPLY_DELETE_SUCCESS("답변 삭제 성공"),
    REPLY_LIKE_CHANGE_SUCCESS("답변 좋아요 변경 성공"),
    CREATED_SUBJECT("주제 등록 성공"),
    INTERNAL_SERVER_ERROR("서버 내부 에러"),
    EXIST_USER_EMAIL("이미 존재하는 회원 메일"),
    EXIST_SUBJECT("이미 존재하는 주제"),
    DELETE_SUBJECT("관심 주제 삭제 성공"),
    DB_ERROR("데이터베이스 에러");

    ResponseMessage(String message) {
    }
}
