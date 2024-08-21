package react.blog.entity.board.service;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.stereotype.Service;
import react.blog.document.BoardDocument;
import react.blog.entity.board.dto.response.BoardListResponseDto;
import react.blog.entity.board.repository.BoardElasticSearchRepository;
import react.blog.entity.board.repository.BoardQueryRepository;
import react.blog.entity.searchlog.service.SearchLogService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BoardSearchService {

    private final BoardQueryRepository boardQueryRepository;
    private final BoardElasticSearchRepository boardElasticSearchRepository;
    private final SearchLogService searchLogService;

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
     * 검색어 저장 및 count 증가
     * 검색어로 Elasticsearch 인덱스 조회
     * @param searchWord
     * @param pageable
     * @return
     * 현재 local에는 1개의 클러스터와 1개의 노드로 구성되어있음
     * 따라서 검색 시 1개의 노드로 모든 데이터 검색을 처리하는 중
     */
    public List<BoardListResponseDto> findSearchBoardList(String searchWord, Pageable pageable) {
        Page<BoardDocument> searchHits = boardElasticSearchRepository.findByTitle(searchWord, pageable);
        return searchHits.stream()
                .map(BoardListResponseDto::of)
                .collect(Collectors.toList());
    }

    public List<BoardListResponseDto> findBoardListByEmail(String email, Pageable pageable) {
        return boardElasticSearchRepository.findByEmail(email, pageable)
                .stream()
                .map(BoardListResponseDto::of)
                .collect(Collectors.toList());
    }

}
