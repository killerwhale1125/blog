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
        searchLogRepository.findById(searchWord).ifPresentOrElse(
                doc -> {
                    doc.updateCount();
                    searchLogRepository.save(doc);
                },
                () -> {
                    SearchWordDocument newDoc = SearchWordDocument.of(searchWord);
                    searchLogRepository.save(newDoc);
                }
        );
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
        Pageable pageable = PageRequest.of(0, 10);
        List<String> relatedWords = searchLogRepository.findBySearchWordContaining(searchWord, pageable).stream()
                .map(SearchWordDocument::getSearchWord)
                .distinct()
                .sorted(String::compareToIgnoreCase)
                .collect(Collectors.toList());

        SearchWordDocument searchWordDocument = updateSuggestions(searchWord, relatedWords);
        return searchWordDocument.getSuggestions();
    }

    private SearchWordDocument updateSuggestions(String searchWord, List<String> newSuggestions) {
        SearchWordDocument document = searchLogRepository.findById(searchWord).get();
        document.registSuggestions(newSuggestions);
        searchLogRepository.save(document);
        return document;
    }
}
