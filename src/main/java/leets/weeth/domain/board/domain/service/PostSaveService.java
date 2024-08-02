package leets.weeth.domain.board.domain.service;

import leets.weeth.domain.board.domain.entity.Post;
import leets.weeth.domain.board.domain.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostSaveService {

    private final PostRepository postRepository;

    public void save(Post post) {
        postRepository.save(post);
    }
}
