package leets.weeth.domain.board.presentation;


import static leets.weeth.domain.board.presentation.ResponseMessage.NOTICE_FIND_ALL_SUCCESS;
import static leets.weeth.domain.board.presentation.ResponseMessage.NOTICE_FIND_BY_ID_SUCCESS;
import static leets.weeth.domain.board.presentation.ResponseMessage.NOTICE_SEARCH_SUCCESS;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import leets.weeth.domain.board.application.dto.NoticeDTO;
import leets.weeth.domain.board.application.usecase.NoticeUsecase;
import leets.weeth.global.common.response.CommonResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@Tag(name = "NOTICE", description = "공지사항 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/notices")
public class NoticeController {

    private final NoticeUsecase noticeUsecase;

    @GetMapping
    @Operation(summary="공지사항 목록 조회 [무한스크롤]")
    public CommonResponse<Slice<NoticeDTO.ResponseAll>> findNotices(@RequestParam("pageNumber") int pageNumber, @RequestParam("pageSize") int pageSize) {
        return CommonResponse.createSuccess(NOTICE_FIND_ALL_SUCCESS.getMessage(), noticeUsecase.findNotices(pageNumber, pageSize));
    }

    @GetMapping("/{noticeId}")
    @Operation(summary="특정 공지사항 조회")
    public CommonResponse<NoticeDTO.Response> findNoticeById(@PathVariable Long noticeId) {
        return CommonResponse.createSuccess(NOTICE_FIND_BY_ID_SUCCESS.getMessage(), noticeUsecase.findNotice(noticeId));
    }

    @GetMapping("/search")
    @Operation(summary="공지사항 검색 [무한스크롤]")
    public CommonResponse<Slice<NoticeDTO.ResponseAll>> findNotice(@RequestParam String keyword, @RequestParam("pageNumber") int pageNumber, @RequestParam("pageSize") int pageSize) {
        return CommonResponse.createSuccess(NOTICE_SEARCH_SUCCESS.getMessage(), noticeUsecase.searchNotice(keyword, pageNumber, pageSize));
    }
}
