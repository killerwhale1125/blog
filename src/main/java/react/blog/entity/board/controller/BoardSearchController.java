package react.blog.entity.board.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import react.blog.entity.board.dto.response.BoardListResponseDto;
import react.blog.entity.board.service.BoardSearchService;
import react.blog.common.BaseResponse;

import java.util.List;

import static java.time.LocalDateTime.now;
import static react.blog.utils.BaseTimeEntity.endOfWeek;
import static react.blog.utils.BaseTimeEntity.startOfWeek;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/search")
public class BoardSearchController {

    private final BoardSearchService boardSearchService;

    /**
     * 최근 게시물 리스트
     * @param pageable
     * @return
     */
    @GetMapping("/current-list")
    public BaseResponse<List<BoardListResponseDto>> getLatestPosts(Pageable pageable) {
        return new BaseResponse<>(boardSearchService.findLatestBoard(pageable));
    }

    /**
     * 캐싱 적용
     * @return
     */
    @GetMapping("/top-3")
    public BaseResponse<List<BoardListResponseDto>> getTop3WeeklyPosts() {
        List<BoardListResponseDto> top3WeeklyPosts = boardSearchService.findTop3WeeklyPosts(startOfWeek(now()), endOfWeek(now()));
        return new BaseResponse<>(top3WeeklyPosts);
    }

    /**
     * indexing 적용
     * @param searchWord
     * @param pageable
     * @return
     */
    @GetMapping("/search-list/{searchWord}")
    public BaseResponse<List<BoardListResponseDto>> getSearchBoardList(@PathVariable String searchWord, Pageable pageable) {
        return new BaseResponse<>(boardSearchService.findSearchBoardList(searchWord, pageable));
    }

    /**
     * Indexing 적용
     * 단순 Email 조회라서 RDBMS에서 조회 방식은 데이터가 많지 않을 경우 적합
     * 데이터가 많을 경우 Elasticsearch가 효율적
     * @param email
     * @param pageable
     * @return
     */
    @GetMapping("/user-board/{email}")
    public BaseResponse<List<BoardListResponseDto>> getUserBoard(@PathVariable String email, Pageable pageable) {
        return new BaseResponse<>(boardSearchService.findBoardListByEmail(email, pageable));
    }

}
