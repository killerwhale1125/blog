package react.blog.entity.searchlog.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import react.blog.common.BaseResponse;
import react.blog.entity.searchlog.service.SearchLogService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/searchLog")
public class SearchLogController {

    private final SearchLogService searchLogService;

    /**
     * 검색어 등록
     * @param searchWord
     * @return
     */
    @PostMapping("/{searchWord}")
    public BaseResponse<Void> handleSearch(@PathVariable String searchWord,
                                           @RequestBody(required = false) List<String> suggestions) {
        searchLogService.saveOrUpdateSearchWord(searchWord, suggestions);
        return new BaseResponse<>();
    }

    /**
     * 관련 검색어 조회
     * @param searchWord
     * @return
     */
    @GetMapping("/{searchWord}/suggestions")
    public BaseResponse<List<String>> getSuggestions(@PathVariable String searchWord) {
        List<String> suggestions = searchLogService.getSuggestions(searchWord);
        return new BaseResponse<>(suggestions);
    }

    @GetMapping("/popular-list")
    public BaseResponse<List<String>> popularWordList() {
        return new BaseResponse<>(searchLogService.getTopSearchWords());
    }
}
