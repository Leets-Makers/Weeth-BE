package leets.weeth.domain.comment.application.mapper;

import leets.weeth.domain.board.domain.entity.Notice;
import leets.weeth.domain.board.domain.entity.Post;
import leets.weeth.domain.comment.application.dto.CommentDTO;
import leets.weeth.domain.comment.domain.entity.Comment;
import leets.weeth.domain.user.domain.entity.User;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CommentMapper {

    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "createdAt", ignore = true),
            @Mapping(target = "modifiedAt", ignore = true),
            @Mapping(target = "isDeleted", ignore = true),
            @Mapping(target = "notice", ignore = true),
            @Mapping(target = "user", source = "user"),
            @Mapping(target = "parent", source = "parent"),
            @Mapping(target = "content", source = "dto.content"),
            @Mapping(target = "post", source = "post")
    })
    Comment fromCommentDto(CommentDTO.Save dto, Post post, User user, Comment parent);

    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "createdAt", ignore = true),
            @Mapping(target = "modifiedAt", ignore = true),
            @Mapping(target = "isDeleted", ignore = true),
            @Mapping(target = "post", ignore = true),
            @Mapping(target = "user", source = "user"),
            @Mapping(target = "parent", source = "parent"),
            @Mapping(target = "content", source = "dto.content"),
            @Mapping(target = "notice", source = "notice")
    })
    Comment fromCommentDto(CommentDTO.Save dto, Notice notice, User user, Comment parent);


    @Mapping(target = "name", source = "comment.user.name")
    @Mapping(target = "position", source = "comment.user.position")
    @Mapping(target = "role", source = "comment.user.role")
    @Mapping(target = "time", source = "comment.modifiedAt")
    @Mapping(target = "children", source = "children")
    CommentDTO.Response toCommentDtoWithChildren(Comment comment, List<CommentDTO.Response> children);
}
