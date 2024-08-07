package react.blog.board.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import react.blog.board.dto.response.BoardListResponseDto;
import react.blog.board.service.BoardSearchService;
import react.blog.common.BaseResponse;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class BoardSearchController {

    private final BoardSearchService boardSearchService;

    @GetMapping("/current-list")
    public BaseResponse<List<BoardListResponseDto>> getLatestPosts(Pageable pageable) {
        return new BaseResponse<>(boardSearchService.findLatestBoard(pageable));
    }

}
