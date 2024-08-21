package react.blog.entity.board.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import react.blog.document.BoardDocument;

public interface BoardElasticSearchRepository extends ElasticsearchRepository<BoardDocument, Long> {
    Page<BoardDocument> findByTitle(String title, Pageable pageable);

    Page<BoardDocument> findByWriteNickname(String email, Pageable pageable);

    Page<BoardDocument> findByEmail(String email, Pageable pageable);
}
