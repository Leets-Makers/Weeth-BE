package leets.weeth.domain.user.application.usecase;

import leets.weeth.domain.user.application.dto.response.UserResponseDto;
import leets.weeth.domain.user.domain.entity.enums.UsersOrderBy;

import java.util.List;

import static leets.weeth.domain.user.application.dto.request.UserRequestDto.*;

public interface UserManageUseCase {


    List<UserResponseDto.AdminResponse> findAllByAdmin(UsersOrderBy orderBy);

    void accept(UserId userIds);

    void update(List<UserRoleUpdate> request);

    void leave(Long userId);

    void ban(UserId userIds);

    void applyOB(List<UserApplyOB> request);

    void reset(Long userId);
}
