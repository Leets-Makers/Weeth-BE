package leets.weeth.domain.account.presentation;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import leets.weeth.domain.account.application.dto.ReceiptDTO;
import leets.weeth.domain.account.application.usecase.ReceiptUseCase;
import leets.weeth.global.common.response.CommonResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import static leets.weeth.domain.account.presentation.ResponseMessage.*;

@Tag(name = "ReceiptAdminController", description = "회비 내역 관련 어드민 컨트롤러")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin/receipts")
public class ReceiptAdminController {

    private final ReceiptUseCase receiptUseCase;

    @PostMapping
    @Operation(summary="회비 사용 내역 기입")
    public CommonResponse<Void> save(@RequestBody @Valid ReceiptDTO.Save dto) {
        receiptUseCase.save(dto);
        return CommonResponse.createSuccess(RECEIPT_SAVE_SUCCESS.getMessage());
    }

    @DeleteMapping("/{receiptId}")
    @Operation(summary="회비 사용 내역 취소")
    public CommonResponse<Void> delete(@PathVariable Long receiptId) {
        receiptUseCase.delete(receiptId);
        return CommonResponse.createSuccess(RECEIPT_DELETE_SUCCESS.getMessage());
    }

    @PatchMapping("/{receiptId}")
    @Operation(summary="회비 사용 내역 수정")
    public CommonResponse<Void> update(@PathVariable Long receiptId, @RequestBody @Valid ReceiptDTO.Update dto) {
        receiptUseCase.update(receiptId, dto);
        return CommonResponse.createSuccess(RECEIPT_UPDATE_SUCCESS.getMessage());
    }
}
