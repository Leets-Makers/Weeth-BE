package leets.weeth.global.sas.application.dto;

import leets.weeth.domain.user.domain.entity.enums.Position;

public record OauthUserInfoResponse(
        long userId,
        String name,
        String email,
        String tel,
        Position position,
        int cardinal
) {
}
