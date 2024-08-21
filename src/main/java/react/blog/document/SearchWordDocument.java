package react.blog.document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@Document(indexName = "words")
@AllArgsConstructor
public class SearchWordDocument {
    @Id
    @Field(type = FieldType.Keyword, analyzer = "standard")
    private String searchWord;

    @Field(type = FieldType.Integer)
    private int count;

    @Field(type = FieldType.Text, analyzer = "standard") // Text 타입으로 관련 검색어를 저장
    private List<String> suggestions; // 관련 검색어 리스트

    public static SearchWordDocument of(String searchWord) {
        return SearchWordDocument.builder()
                .searchWord(searchWord)
                .count(1)
                .suggestions(new ArrayList<>())
                .build();
    }

    public void updateCount() {
        this.count++;
    }

    public void registSuggestions(List<String> suggestions) {
        this.suggestions.addAll(suggestions);
    }
}
