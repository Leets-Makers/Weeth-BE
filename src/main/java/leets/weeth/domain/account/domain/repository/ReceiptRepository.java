package leets.weeth.domain.account.domain.repository;

import leets.weeth.domain.account.domain.entity.Receipt;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReceiptRepository extends JpaRepository<Receipt, Long> {
}
