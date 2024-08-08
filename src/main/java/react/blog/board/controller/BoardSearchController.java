package react.blog.board.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import react.blog.board.dto.response.BoardListResponseDto;
import react.blog.board.service.BoardSearchService;
import react.blog.common.BaseResponse;

import java.util.List;

import static java.time.LocalDateTime.now;
import static react.blog.utils.BaseTimeEntity.endOfWeek;
import static react.blog.utils.BaseTimeEntity.startOfWeek;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/board")
public class BoardSearchController {

    private final BoardSearchService boardSearchService;

    @GetMapping("/current-list")
    public BaseResponse<List<BoardListResponseDto>> getLatestPosts(Pageable pageable) {
        return new BaseResponse<>(boardSearchService.findLatestBoard(pageable));
    }

    /**
     * 캐싱 적용 대상
     */
    @GetMapping("/top-3")
    public BaseResponse<List<BoardListResponseDto>> getTop3WeeklyPosts() {
        List<BoardListResponseDto> top3WeeklyPosts = boardSearchService.findTop3WeeklyPosts(startOfWeek(now()), endOfWeek(now()));
        return new BaseResponse<>(top3WeeklyPosts);
    }
}
