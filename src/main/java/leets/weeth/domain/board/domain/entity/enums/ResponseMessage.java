package leets.weeth.domain.board.domain.entity.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ResponseMessage {

    NOTICE_CREATED_SUCCESS("공지사항 생성 성공."),
    NOTICE_UPDATED_SUCCESS("공지사항 수정 성공."),
    NOTICE_DELETED_SUCCESS("공지사항 삭제 성공."),

    POST_CREATED_SUCCESS("게시글 생성 성공."),
    POST_UPDATED_SUCCESS("게시글 수정 성공."),
    POST_DELETED_SUCCESS("게시글 삭제 성공.");

    private final String message;
}
