package react.blog.entity.searchlog.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import react.blog.document.SearchWordDocument;

import java.util.List;

public interface SearchLogRepository extends ElasticsearchRepository<SearchWordDocument, String> {

    /**
     * Elasticsearch에서 인기 검색어 10개를 조회하는 쿼리 메서드
     * ?0: 메서드의 첫 번째 파라미터를 참조하여 0번째 파라미터를 사용
     */
    List<SearchWordDocument> findTop10By(Sort sort);

    // 검색어를 기반으로 관련 문서 조회
    List<SearchWordDocument> findBySearchWordContaining(String searchWord, Pageable pageable);

    SearchWordDocument findBySearchWord(String searchWord);
}
