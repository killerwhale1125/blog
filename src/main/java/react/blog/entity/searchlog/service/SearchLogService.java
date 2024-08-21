package react.blog.entity.searchlog.service;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import react.blog.document.SearchWordDocument;
import react.blog.entity.searchlog.repository.SearchLogRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SearchLogService {

    private final SearchLogRepository searchLogRepository;

    // 검색어 저장 또는 업데이트 및 제안 검색어 추가
    public void saveOrUpdateSearchWord(String searchWord, List<String> newSuggestions) {
        // 1. 검색어에 해당하는 문서 찾기 (없으면 새로 생성)
        SearchWordDocument targetDoc = searchLogRepository.findById(searchWord).orElseGet(() -> {
            SearchWordDocument newDoc = SearchWordDocument.of(searchWord);
            searchLogRepository.save(newDoc);
            return newDoc;
        });

        // 2. 카운트 업데이트
        targetDoc.updateCount();

        // 3. 제안 검색어 업데이트
        if (!newSuggestions.isEmpty()) {
            List<String> existingSuggestions = targetDoc.getSuggestions();
            existingSuggestions.addAll(newSuggestions);
            targetDoc.registSuggestions(existingSuggestions.stream()
                    .distinct() // 중복 제거
                    .sorted(String::compareToIgnoreCase) // 정렬
                    .collect(Collectors.toList()));
        }

        // 4. 문서 저장
        searchLogRepository.save(targetDoc);
    }

    // 상위 10개의 검색어 조회 (캐시 사용)
    @Cacheable(value = "topSearchWords", key = "'topSearchWords'")
    public List<String> getTopSearchWords() {
        Sort sort = Sort.by(Sort.Order.desc("count"));
        return searchLogRepository.findTop10By(sort).stream()
                .map(SearchWordDocument::getSearchWord)
                .collect(Collectors.toList());
    }

    // 검색어에 해당하는 제안 검색어 조회 및 관련 검색어 추가
    public List<String> getSuggestions(String searchWord) {
        // 1. 최대 10개의 유사한 검색어 조회
        Pageable pageable = PageRequest.of(0, 10);
        List<SearchWordDocument> relatedWordsDocs = searchLogRepository.findBySearchWordContaining(searchWord, pageable);
        List<String> relatedWords = relatedWordsDocs.stream()
                .map(SearchWordDocument::getSearchWord)
                .collect(Collectors.toList());

        // 2. 현재 검색어 문서에 제안 검색어 추가 및 업데이트
        saveOrUpdateSearchWord(searchWord, relatedWords);

        // 3. 제안 검색어 리스트 반환
        return searchLogRepository.findById(searchWord)
                .map(SearchWordDocument::getSuggestions)
                .orElseGet(() -> relatedWords.stream().distinct().collect(Collectors.toList()));
    }
}
