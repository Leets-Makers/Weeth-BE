package leets.weeth.global.sas.application.converter;

import com.fasterxml.jackson.annotation.*;

import java.util.Map;

@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY,
        getterVisibility = JsonAutoDetect.Visibility.NONE,
        isGetterVisibility = JsonAutoDetect.Visibility.NONE,
        creatorVisibility = JsonAutoDetect.Visibility.NONE)
public abstract class ClaimsHolderMixin {

    @JsonCreator
    ClaimsHolderMixin(@JsonProperty("claims") Map<String, Object> claims) {
    }

}
