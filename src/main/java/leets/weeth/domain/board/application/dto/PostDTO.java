package leets.weeth.domain.board.application.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;
import leets.weeth.domain.board.domain.entity.enums.Category;
import leets.weeth.domain.board.domain.entity.enums.Part;
import leets.weeth.domain.comment.application.dto.CommentDTO;
import leets.weeth.domain.file.application.dto.request.FileSaveRequest;
import leets.weeth.domain.file.application.dto.response.FileResponse;
import leets.weeth.domain.user.domain.entity.enums.Position;
import leets.weeth.domain.user.domain.entity.enums.Role;
import lombok.Builder;

public class PostDTO {

    @Builder
    public record Save(
            @NotNull String title,
            @NotNull String content,
            @NotNull Category category,
            String studyName,
            int week,
            @NotNull Part part,
            @NotNull Integer cardinalNumber,
            @Valid List<@NotNull FileSaveRequest> files
    ){}

    @Builder
    public record SaveEducation(
            @NotNull String title,
            @NotNull String content,
            @NotNull List<Part> parts,
            @NotNull Integer cardinalNumber,
            @Valid List<@NotNull FileSaveRequest> files
    ){}

    @Builder
    public record Update(
            String title,
            String content,
            String studyName,
            Integer week,
            Part part,
            Integer cardinalNumber,
            @Valid List<FileSaveRequest> files
    ){}

    @Builder
    public record UpdateEducation(
            String title,
            String content,
            List<Part> parts,
            Integer cardinalNumber,
            @Valid List<FileSaveRequest> files
    ){}

    @Builder
    public record Response(
            Long id,
            String name,
            Position position,
            Role role,
            String title,
            String content,
            String studyName,
            Integer week,
            Integer cardinalNumber,
            Part part,
            List<Part> parts,
            LocalDateTime time,
            Integer commentCount,
            List<CommentDTO.Response> comments,
            List<FileResponse> fileUrls
    ){}

    @Builder
    public record ResponseAll(
            Long id,
            String name,
            Position position,
            Role role,
            String title,
            String content,
            String studyName,
            int week,
            LocalDateTime time,
            Integer commentCount,
            boolean hasFile,
            boolean isNew
    ){}

    @Builder
    public record ResponseEducationAll(
            Long id,
            String name,
            List<Part> parts,
            int week,
            LocalDateTime time,
            Integer commentCount,
            boolean hasFile,
            boolean isNew
    ){}

}
