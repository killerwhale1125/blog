package react.blog.favorite.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import react.blog.common.BaseResponse;
import react.blog.favorite.dto.request.FavoriteListResponseDto;
import react.blog.favorite.service.FavoriteService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/favorite")
public class FavoriteController {
    private final FavoriteService favoriteService;

    @PutMapping("/{boardNumber}")
    public BaseResponse<Void> addFavorite(Authentication authentication, @PathVariable Long boardNumber) {
        favoriteService.addFavorite(authentication.getName(), boardNumber);
        return new BaseResponse<>();
    }

    @GetMapping("/{boardNumber}")
    public BaseResponse<List<FavoriteListResponseDto>> findFavoriteList(@PathVariable Long boardNumber) {
        return new BaseResponse<>(favoriteService.findFavoriteList(boardNumber));
    }
}
