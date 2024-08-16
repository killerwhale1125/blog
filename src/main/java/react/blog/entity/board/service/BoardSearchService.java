package react.blog.entity.board.service;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.stereotype.Service;
import react.blog.document.BoardDocument;
import react.blog.entity.board.dto.response.BoardListResponseDto;
import react.blog.entity.board.repository.BoardElasticSearchRepository;
import react.blog.entity.board.repository.BoardQueryRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BoardSearchService {

    private final BoardQueryRepository boardQueryRepository;
    private final BoardElasticSearchRepository boardElasticSearchRepository;

    /**
     * 최근 게시물 조회
     * @param pageable
     * @return
     */
    public List<BoardListResponseDto> findLatestBoard(Pageable pageable) {
        return boardQueryRepository.findLatestBoard(pageable).getContent();
    }

    /**
     * 주간 탑 3 게시물  ( 캐싱 적용 )
     * @param startOfWeek
     * @param endOfWeek
     * @return
     */
    @Cacheable(value = "topBoardsCache", key = "#startOfWeek.toString() + '-' + #endOfWeek.toString()", cacheManager = "redisCacheManager")
    public List<BoardListResponseDto> findTop3WeeklyPosts(LocalDateTime startOfWeek, LocalDateTime endOfWeek) {
        return boardQueryRepository.findAllByCreatedDateBetween(startOfWeek, endOfWeek);
    }

    /**
     * 검색어로 Elasticsearch 인덱스 조회
     * @param searchWord
     * @param pageable
     * @return
     */
    public List<BoardListResponseDto> findSearchBoardList(String searchWord, Pageable pageable) {
        SearchHits<BoardDocument> searchHits = boardElasticSearchRepository.findByTitle(searchWord);
        return searchHits.stream()
                .map(hit -> BoardListResponseDto.of(hit.getContent()))
                .collect(Collectors.toList());
    }

    public List<BoardListResponseDto> findBoardListByEmail(String email, Pageable pageable) {
        return boardQueryRepository.findBoardListByEmail(email, pageable);
    }

//    public List<WordsDocument> getRelatedSearches(String query) {
//        List<WordsDocument> searches = searchWordsRepository.findByQuery(query);
//        // 검색 결과가 없으면 빈 리스트 반환
//        if (searches.isEmpty()) {
//            return List.of();
//        }
//        // 첫 번째 검색 결과에서 관련 검색어 리스트 추출
////        return searches.stream()
////                .flatMap(result -> result.getSuggestions().stream())
////                .collect(Collectors.toList());
//        return null;
//    }
}
