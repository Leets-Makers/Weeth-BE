package leets.weeth.global.sas.application.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorMessage {

    USER_INACTIVE("WAE-001", "가입 승인이 허가되지 않은 계정입니다."),
    USER_NOT_FOUND("WAE-002", "존재하지 않는 유저입니다."),
    KAKAO_AUTH_ERROR("WAE-003", "카카오 로그인 예외입니다.");

    private final String code;
    private final String description;
}
