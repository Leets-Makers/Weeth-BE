package leets.weeth.domain.board.domain.service;

import jakarta.transaction.Transactional;
import leets.weeth.domain.board.application.dto.NoticeDTO;
import leets.weeth.domain.board.domain.entity.Notice;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NoticeUpdateService {

    public void update(Notice notice, NoticeDTO.Update dto){
        notice.update(dto);
    }

}
