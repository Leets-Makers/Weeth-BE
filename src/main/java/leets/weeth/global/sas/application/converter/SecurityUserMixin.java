package leets.weeth.global.sas.application.converter;

import com.fasterxml.jackson.annotation.*;

@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS)               // ← FQCN 자체를 type id 로 사용
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY,
        getterVisibility = JsonAutoDetect.Visibility.NONE,
        isGetterVisibility = JsonAutoDetect.Visibility.NONE,
        creatorVisibility = JsonAutoDetect.Visibility.NONE)
@JsonIgnoreProperties(ignoreUnknown = true)
public abstract class SecurityUserMixin {

    @JsonCreator
    SecurityUserMixin(
            @JsonProperty("id") Long id,
            @JsonProperty("email") String email,
            @JsonProperty("name") String name,
            @JsonProperty("role") String role,
            @JsonProperty("active") boolean active) {
    }
}
