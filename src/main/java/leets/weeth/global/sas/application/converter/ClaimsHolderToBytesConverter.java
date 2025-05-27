package leets.weeth.global.sas.application.converter;

import com.fasterxml.jackson.databind.ObjectMapper;
import leets.weeth.domain.user.domain.entity.SecurityUser;
import leets.weeth.global.sas.domain.entity.ClaimsHolder;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.WritingConverter;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.security.jackson2.SecurityJackson2Modules;
import org.springframework.security.oauth2.server.authorization.jackson2.OAuth2AuthorizationServerJackson2Module;

@WritingConverter
public class ClaimsHolderToBytesConverter implements Converter<ClaimsHolder, byte[]> {

    private final Jackson2JsonRedisSerializer<ClaimsHolder> serializer;

    public ClaimsHolderToBytesConverter() {
        ObjectMapper objectMapper = new ObjectMapper();

        objectMapper.registerModules(SecurityJackson2Modules.getModules(ClaimsHolderToBytesConverter.class.getClassLoader()));
        objectMapper.registerModules(new OAuth2AuthorizationServerJackson2Module());

        objectMapper.addMixIn(ClaimsHolder.class, ClaimsHolderMixin.class);
        objectMapper.addMixIn(Long.class, SecurityUserMixin.class);
        objectMapper.addMixIn(SecurityUser.class, SecurityUserMixin.class);

        this.serializer = new Jackson2JsonRedisSerializer<>(objectMapper, ClaimsHolder.class);
    }

    @Override
    public byte[] convert(ClaimsHolder value) {
        return this.serializer.serialize(value);
    }

}
