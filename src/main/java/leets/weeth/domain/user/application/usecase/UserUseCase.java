package leets.weeth.domain.user.application.usecase;

import leets.weeth.domain.user.application.dto.request.UserRequestDto;
import leets.weeth.domain.user.application.dto.response.UserResponseDto;
import leets.weeth.global.auth.jwt.application.dto.JwtDto;
import org.springframework.data.domain.Slice;

import java.util.List;
import java.util.Map;

import static leets.weeth.domain.user.application.dto.request.UserRequestDto.Register;
import static leets.weeth.domain.user.application.dto.request.UserRequestDto.SignUp;
import static leets.weeth.domain.user.application.dto.response.UserResponseDto.SocialLoginResponse;
import static leets.weeth.domain.user.application.dto.request.UserRequestDto.*;
import static leets.weeth.domain.user.application.dto.response.UserResponseDto.*;


public interface UserUseCase {

    SocialLoginResponse login(Login dto);

    SocialAuthResponse authenticate(Login dto);

    SocialLoginResponse integrate(NormalLogin dto);

    UserResponseDto.Response find(Long userId);

    Slice<SummaryResponse> findAllUser(int pageNumber, int pageSize, Integer cardinal);

    UserResponseDto.UserResponse findUserDetails(Long userId);

    void update(UserRequestDto.Update dto, Long userId);

    void apply(SignUp dto);

    void socialRegister(Register dto);

    JwtDto refresh(String refreshToken);

    UserResponseDto.UserInfo findUserInfo(Long userId);

}
