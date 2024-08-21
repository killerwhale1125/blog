package react.blog.entity.board.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import react.blog.entity.board.dto.request.CreateBoardRequestDto;
import react.blog.entity.board.dto.request.UpdateBoardRequestDto;
import react.blog.entity.board.service.BoardService;
import react.blog.common.BaseResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/board")
public class BoardController {

    private final BoardService boardService;

    @PostMapping
    public BaseResponse<Void> createBoard(Authentication authentication, @RequestBody @Valid CreateBoardRequestDto boardRequestDto) {
        boardService.createBoard(authentication.getName(), boardRequestDto);
        return new BaseResponse<>();
    }

    @PatchMapping("/{boardId}")
    public BaseResponse<Void> updateBoard(Authentication authentication, @RequestBody @Valid UpdateBoardRequestDto boardRequestDto, @PathVariable Long boardId) {
        boardService.updateBoard(authentication.getName(), boardRequestDto, boardId);
        return new BaseResponse<>();
    }

    @DeleteMapping("/{boardId}")
    public BaseResponse<Void> removeBoard(Authentication authentication, @PathVariable Long boardId) {
        boardService.removeBoard(authentication.getName(), boardId);
        return new BaseResponse<>();
    }
}
