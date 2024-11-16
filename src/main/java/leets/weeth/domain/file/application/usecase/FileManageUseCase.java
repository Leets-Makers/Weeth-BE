package leets.weeth.domain.file.application.usecase;

import jakarta.transaction.Transactional;
import leets.weeth.domain.file.application.dto.response.UrlResponse;
import leets.weeth.domain.file.domain.service.PreSignedService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FileManageUseCase {

    private final PreSignedService preSignedService;

    @Transactional
    public List<UrlResponse> getUrl(List<String> fileNames) {
        return fileNames.stream()
                .map(preSignedService::generateUrl)
                .toList();
    }
}
