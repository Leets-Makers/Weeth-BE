package leets.weeth.global.auth.jwt.service;

import leets.weeth.domain.user.application.exception.RoleNotFoundException;
import leets.weeth.domain.user.domain.entity.enums.Role;
import leets.weeth.global.auth.jwt.exception.InvalidTokenException;
import leets.weeth.global.auth.jwt.exception.RedisTokenNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class JwtRedisService {

    @Value("${weeth.jwt.refresh.expiration}")
    private Long expirationTime;

    private static final String PREFIX = "refreshToken:";

    private final RedisTemplate<String, String> redisTemplate;

    public void set(long userId, String refreshToken, Role role, String email) {
        String key = getKey(userId);
        redisTemplate.opsForHash().put(key, "token", refreshToken);
        redisTemplate.opsForHash().put(key, "role", role.toString());
        redisTemplate.opsForHash().put(key, "email", email);
        redisTemplate.expire(key, expirationTime, TimeUnit.MINUTES);
        log.info("Refresh Token 저장/업데이트: {}", key);
    }

    public void delete(Long userId) {
        String key = getKey(userId);
        redisTemplate.delete(key);
    }

    public void validateRefreshToken(long userId, String requestToken) {
        if (!find(userId).equals(requestToken)) {
            throw new InvalidTokenException();
        }
    }

    public String getEmail(long userId) {
        String key = getKey(userId);
        String roleValue = (String) redisTemplate.opsForHash().get(key, "email");

        return Optional.ofNullable(roleValue)
                .orElseThrow(RoleNotFoundException::new);
    }

    public Role getRole(long userId) {
        String key = getKey(userId);
        String roleValue = (String) redisTemplate.opsForHash().get(key, "role");

        return Optional.ofNullable(roleValue)
                .map(Role::valueOf)
                .orElseThrow(RoleNotFoundException::new);
    }

    public void updateRole(long userId, String role) {
        String key = getKey(userId);

        if (Boolean.TRUE.equals(redisTemplate.hasKey(key))) {
            redisTemplate.opsForHash().put(key, "role", role);
        }
    }

    private String find(long userId) {
        String key = getKey(userId);
        return Optional.ofNullable((String) redisTemplate.opsForHash().get(key, "token"))
                .orElseThrow(RedisTokenNotFoundException::new);
    }

    private String getKey(long userId) {
        return PREFIX + userId;
    }
}
