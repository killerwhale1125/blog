package react.blog.entity.board.repository;

import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import react.blog.document.BoardDocument;

public interface BoardElasticSearchRepository extends ElasticsearchRepository<BoardDocument, String> {
    SearchHits<BoardDocument> findByTitle(String title);
}
