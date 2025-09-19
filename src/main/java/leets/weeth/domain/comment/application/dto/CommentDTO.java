package leets.weeth.domain.comment.application.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import leets.weeth.domain.file.application.dto.request.FileSaveRequest;
import leets.weeth.domain.file.application.dto.response.FileResponse;
import leets.weeth.domain.user.domain.entity.enums.Position;
import leets.weeth.domain.user.domain.entity.enums.Role;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

public class CommentDTO {

    @Builder
    public record Save(
            Long parentCommentId,
            @NotBlank String content,
            @Valid List<@NotNull FileSaveRequest> files
    ){}

    @Builder
    public record Update(
            @NotBlank String content,
            @Valid List<@NotNull FileSaveRequest> files
    ){}

    @Builder
    public record Response(
            Long id,
            String name,
            Position position,
            Role role,
            String content,
            LocalDateTime time, //modifiedAt
            List<FileResponse> fileUrls,
            List<CommentDTO.Response> children
    ){}

}
