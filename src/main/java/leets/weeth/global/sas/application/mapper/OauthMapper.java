package leets.weeth.global.sas.application.mapper;

import leets.weeth.domain.user.domain.entity.User;
import leets.weeth.global.sas.application.dto.OauthUserInfoResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface OauthMapper {

    @Mapping(target = "userId", source = "user.id")
    OauthUserInfoResponse toResponse(User user, int cardinal);
}

