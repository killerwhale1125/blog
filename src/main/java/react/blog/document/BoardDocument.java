package react.blog.document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import react.blog.entity.Board;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@Document(indexName = "boards")
@AllArgsConstructor
public class BoardDocument {

    @Id
    private Long id;
    // 자체 소문자 적용
    @Field(type = FieldType.Text, analyzer = "standard")
    private String title;

    @Field(type = FieldType.Text, analyzer = "standard")
    private String content;

    @Field(type = FieldType.Text, analyzer = "standard")
    private String writeNickname;

    @Field(type = FieldType.Text, analyzer = "standard")
    private String email;

    @Field(type = FieldType.Date, format = DateFormat.date_hour_minute_second)
    private LocalDateTime writeDateTime;

    @Field(type = FieldType.Integer)
    private int viewCount;

    @Field(type = FieldType.Integer)
    private int commentCount;

    @Field(type = FieldType.Integer)
    private int favoriteCount;

    public static BoardDocument of(Board board) {
        return BoardDocument.builder()
                .id(board.getId())
                .title(board.getTitle())
                .content(board.getContent())
                .writeNickname(board.getMember().getNickname())
                .writeDateTime(board.getCreatedDate())
                .viewCount(board.getViewCount())
                .commentCount(board.getCommentCount())
                .favoriteCount(board.getFavoriteCount())
                .email(board.getMember().getEmail())
                .build();
    }
}