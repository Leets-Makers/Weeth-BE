package leets.weeth.domain.board.domain.service;

import java.util.List;
import leets.weeth.domain.board.application.exception.PostNotFoundException;
import leets.weeth.domain.board.domain.entity.Post;
import leets.weeth.domain.board.domain.entity.enums.Category;
import leets.weeth.domain.board.domain.entity.enums.Part;
import leets.weeth.domain.board.domain.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
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

    public List<String> findByPart(Part part) {
        return postRepository.findDistinctStudyNamesByPart(part);
    }

    public Slice<Post> findRecentPosts(Pageable pageable) {
        return postRepository.findPageBy(pageable);
    }

    public Slice<Post> findByPartAndOptionalFilters(Part part, Category category, Integer cardinalNumber, String  studyName, Integer week, Pageable pageable) {

        return postRepository.findByPartAndOptionalFilters(
                part, category, cardinalNumber, studyName, week, pageable
        );
    }

    public Slice<Post> findEducationByCardinal(int cardinalNumber, Pageable pageable) {

        return postRepository.findByCategoryAndCardinalNumber(
                Category.Education, cardinalNumber, pageable
        );
    }

    public Slice<Post> findByCategory(Category category, int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(Sort.Direction.DESC, "id"));

        return postRepository.findByCategory(category, pageable);
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
