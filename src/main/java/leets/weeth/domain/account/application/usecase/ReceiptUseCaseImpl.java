package leets.weeth.domain.account.application.usecase;

import jakarta.transaction.Transactional;
import leets.weeth.domain.account.application.dto.ReceiptDTO;
import leets.weeth.domain.account.application.mapper.ReceiptMapper;
import leets.weeth.domain.account.domain.entity.Account;
import leets.weeth.domain.account.domain.entity.Receipt;
import leets.weeth.domain.account.domain.service.*;
import leets.weeth.domain.file.application.mapper.FileMapper;
import leets.weeth.domain.file.domain.entity.File;
import leets.weeth.domain.file.domain.service.FileDeleteService;
import leets.weeth.domain.file.domain.service.FileGetService;
import leets.weeth.domain.file.domain.service.FileSaveService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReceiptUseCaseImpl implements ReceiptUseCase {

    private final ReceiptGetService receiptGetService;
    private final ReceiptDeleteService receiptDeleteService;
    private final ReceiptSaveService receiptSaveService;
    private final ReceiptUpdateService receiptUpdateService;
    private final AccountGetService accountGetService;

    private final FileGetService fileGetService;
    private final FileSaveService fileSaveService;
    private final FileDeleteService fileDeleteService;

    private final ReceiptMapper mapper;
    private final FileMapper fileMapper;


    @Override
    @Transactional
    public void save(ReceiptDTO.Save dto) {
        Account account = accountGetService.find(dto.cardinal());
        Receipt receipt = receiptSaveService.save(mapper.from(dto, account));
        account.spend(receipt);

        List<File> files = fileMapper.toFileList(dto.files(), receipt);
        fileSaveService.save(files);
    }

    @Override
    @Transactional
    public void update(Long reciptId, ReceiptDTO.Update dto){
        Account account = accountGetService.find(dto.cardinal());
        Receipt receipt = receiptGetService.find(reciptId);
        account.cancel(receipt);

        if(!dto.files().isEmpty()){ // 업데이트하려는 파일이 있다면 파일을 전체 삭제한 뒤 저장
            List<File> fileList = getFiles(reciptId);
            fileDeleteService.delete(fileList);

            List<File> files = fileMapper.toFileList(dto.files(), receipt);
            fileSaveService.save(files);
        }
        receiptUpdateService.update(receipt, dto);
        account.spend(receipt);
    }

    private List<File> getFiles(Long reciptId) {
        return fileGetService.findAllByReceipt(reciptId);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Receipt receipt = receiptGetService.find(id);
        List<File> fileList = fileGetService.findAllByReceipt(id);

        receipt.getAccount().cancel(receipt);

        fileDeleteService.delete(fileList);
        receiptDeleteService.delete(receipt);
    }
}
