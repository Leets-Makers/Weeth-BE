package leets.weeth.domain.board.domain.service;

import java.util.List;
import leets.weeth.domain.board.application.exception.PostNotFoundException;
import leets.weeth.domain.board.domain.entity.Post;
import leets.weeth.domain.board.domain.entity.enums.Category;
import leets.weeth.domain.board.domain.entity.enums.Part;
import leets.weeth.domain.board.domain.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostFindService {

    private final PostRepository postRepository;

    public Post find(Long postId){
        return postRepository.findById(postId)
                .orElseThrow(PostNotFoundException::new);
    }

    public List<Post> find(){
        return postRepository.findAll();
    }

    public Slice<Post> findRecentPosts(Pageable pageable) {
        return postRepository.findPageBy(pageable);
    }

    public Slice<Post> findByPartAndOptionalFilters(Part part, Category category, Integer cardinalNumber, Integer week, Pageable pageable) {

        return postRepository.findByPartAndOptionalFilters(
                part, category, cardinalNumber, week, pageable
        );
    }

    public Slice<Post> search(String keyword, Pageable pageable) {
        if(keyword == null || keyword.isEmpty()){
            return findRecentPosts(pageable);
        }
        else{
            return postRepository.findByTitleContainingOrContentContainingIgnoreCase(keyword, keyword, pageable);
        }
    }

}
