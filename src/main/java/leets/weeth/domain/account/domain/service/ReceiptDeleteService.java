package leets.weeth.domain.account.domain.service;

import leets.weeth.domain.account.domain.repository.ReceiptRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReceiptDeleteService {

    private final ReceiptRepository receiptRepository;

    public void delete(Long id) {
        receiptRepository.deleteById(id);
    }
}
