package leets.weeth.domain.file.presentation;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import leets.weeth.domain.file.application.dto.response.UrlResponse;
import leets.weeth.domain.file.application.usecase.FileManageUseCase;
import leets.weeth.global.common.response.CommonResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "FileController")
@RestController
@RequiredArgsConstructor
@RequestMapping("/files")
public class FileController {

    private final FileManageUseCase fileManageUseCase;

    @GetMapping("/")
    @Operation(summary = "파일 업로드를 위한 presigned url을 요청하는 API 입니다.")
    public CommonResponse<List<UrlResponse>> getUrl(@RequestParam(required = false) List<String> fileName) {
        return CommonResponse.createSuccess(ResponseMessage.PRESIGNED_URL_GET_SUCCESS.getMessage(), fileManageUseCase.getUrl(fileName));
    }
}
