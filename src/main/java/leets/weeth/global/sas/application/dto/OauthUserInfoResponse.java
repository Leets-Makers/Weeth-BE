package leets.weeth.global.sas.application.dto;

public record OauthUserInfoResponse(
        long userId,
        String name,
        String email,
        String tel,
        int cardinal
) {
}
