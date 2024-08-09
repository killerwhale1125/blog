package react.blog.board.service;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import react.blog.board.dto.response.BoardListResponseDto;
import react.blog.board.repository.BoardQueryRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BoardSearchService {

    private final BoardQueryRepository boardQueryRepository;

    public List<BoardListResponseDto> findLatestBoard(Pageable pageable) {
        return boardQueryRepository.findLatestBoard(pageable).getContent();
    }

    @Cacheable(value = "topBoardsCache", key = "#startOfWeek.toString() + '-' + #endOfWeek.toString()", cacheManager = "redisCacheManager")
    public List<BoardListResponseDto> findTop3WeeklyPosts(LocalDateTime startOfWeek, LocalDateTime endOfWeek) {
        return boardQueryRepository.findAllByCreatedDateBetween(startOfWeek, endOfWeek);
    }

    public List<BoardListResponseDto> findSearchBoardList(String searchWord, Pageable pageable) {
        return boardQueryRepository.findSearchBoardList(searchWord, pageable);
    }
}
