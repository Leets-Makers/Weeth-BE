package leets.weeth.domain.file.application.dto.request;

import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.URL;

public record FileSaveRequest(
        @NotBlank String fileName,
        @URL String fileUrl
) {
}
