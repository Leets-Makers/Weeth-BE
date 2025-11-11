package leets.weeth.domain.board.domain.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import leets.weeth.domain.board.domain.entity.Post;

public interface PostRepositoryCustom {

	Slice<Post> findRecentPart(Pageable pageable);

	Slice<Post> searchPart(String kw,Pageable pageable);

}
