package com.sieum.user.exception;

public class UnAuthorizedException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public UnAuthorizedException() {
        super("계정 권한이 유효하지 않습니다.\n다시 로그인 하세요.");
    }
}
