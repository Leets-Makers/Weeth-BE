package leets.weeth.domain.board.domain.service;

import jakarta.transaction.Transactional;
import leets.weeth.domain.board.domain.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostDeleteService {

    private final PostRepository postRepository;

    @Transactional
    public void delete(Long postId) {
        postRepository.deleteById(postId);
    }

}
