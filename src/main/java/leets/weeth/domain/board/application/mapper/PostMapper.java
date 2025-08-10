package leets.weeth.domain.board.application.mapper;

import leets.weeth.domain.board.application.dto.PostDTO;
import leets.weeth.domain.board.domain.entity.Post;
import leets.weeth.domain.comment.application.dto.CommentDTO;
import leets.weeth.domain.comment.application.mapper.CommentMapper;
import leets.weeth.domain.file.application.dto.response.FileResponse;
import leets.weeth.domain.user.domain.entity.Cardinal;
import leets.weeth.domain.user.domain.entity.User;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses = CommentMapper.class, unmappedTargetPolicy = ReportingPolicy.IGNORE, imports = { java.time.LocalDateTime.class })
public interface PostMapper {

    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "createdAt", ignore = true),
            @Mapping(target = "modifiedAt", ignore = true),
            @Mapping(target = "user", source = "user"),
            @Mapping(target = "part", expression = "java(user.getUserPart())"),
            @Mapping(target = "parts", expression = "java(List.of(user.getUserPart()))"),
            @Mapping(target = "cardinalNumber", expression = "java(latest.getCardinalNumber())")
    })
    Post fromPostDto(PostDTO.Save dto, User user, Cardinal latest);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "modifiedAt", ignore = true)
    @Mapping(target = "user", source = "user")
    @Mapping(target = "part", ignore = true)
    @Mapping(target = "parts", source = "dto.parts")
    @Mapping(target = "cardinalNumber", expression = "java(latest.getCardinalNumber())")
    @Mapping(target = "category", constant = "Education")
    Post fromEducationDto(PostDTO.SaveEducation dto, User user, Cardinal latest);

    @Mappings({
            @Mapping(target = "name", source = "post.user.name"),
            @Mapping(target = "position", source = "post.user.position"),
            @Mapping(target = "role", source = "post.user.role"),
            @Mapping(target = "time", source = "post.createdAt"),
            @Mapping(target = "hasFile", expression = "java(fileExists)"),
            @Mapping(target = "isNew", expression = "java(post.getCreatedAt().isAfter(LocalDateTime.now().minusHours(24)))")
    })
    PostDTO.ResponseAll toAll(Post post, boolean fileExists);

    @Mappings({
            @Mapping(target = "id",            source = "post.id"),
            @Mapping(target = "name",          source = "post.user.name"),
            @Mapping(target = "parts",         source = "post.parts"),
            @Mapping(target = "week",          source = "post.week"),
            @Mapping(target = "commentCount",  source = "post.commentCount"),
            @Mapping(target = "time",          source = "post.createdAt"),
            @Mapping(target = "hasFile",       expression = "java(fileExists)"),
            @Mapping(target = "isNew",         expression = "java(post.getCreatedAt().isAfter(LocalDateTime.now().minusHours(24)))")
    })
    PostDTO.ResponseEducationAll toEducationAll(Post post, boolean fileExists);

    @Mappings({
            @Mapping(target = "name", source = "post.user.name"),
            @Mapping(target = "position", source = "post.user.position"),
            @Mapping(target = "role", source = "post.user.role"),
            @Mapping(target = "time", source = "post.createdAt"),
            @Mapping(target = "comments", source = "comments")
    })
    PostDTO.Response toPostDto(Post post, List<FileResponse> fileUrls, List<CommentDTO.Response> comments);

}
