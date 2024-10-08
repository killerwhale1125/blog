package react.blog.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import react.blog.utils.BaseTimeEntity;

@Entity
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SearchLog extends BaseTimeEntity {
    @Id @GeneratedValue
    @Column(name = "search_log_id")
    private Long id;
    
    // 검색어
    @NotNull
    private String searchWord;

    // 관련검색어
    private String relationWord;

    // 관련 검색어 여부
    @NotNull
    private boolean relation;
}
