package leets.weeth.domain.board.application.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import leets.weeth.domain.comment.application.dto.CommentDTO;
import leets.weeth.domain.file.application.dto.request.FileSaveRequest;
import leets.weeth.domain.file.application.dto.request.FileUpdateRequest;
import leets.weeth.domain.file.application.dto.response.FileResponse;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

public class NoticeDTO {

    @Builder
    public record Save(
            @NotNull String title,
            @NotNull String content,
            @Valid List<@NotNull FileSaveRequest> files
    ) {
    }

    @Builder
    public record Update(
            @NotNull String title,
            @NotNull String content,
            @Valid List<@NotNull FileSaveRequest> files
    ) {
    }

    @Builder
    public record Response(
            Long id,
            String name,
            String title,
            String content,
            LocalDateTime time,//modifiedAt
            Integer commentCount,
            List<CommentDTO.Response> comments,
            List<FileResponse> fileUrls
    ) {
    }

    @Builder
    public record ResponseAll(
            Long id,
            String name,
            String title,
            String content,
            LocalDateTime time,//modifiedAt
            Integer commentCount
    ) {
    }

}
