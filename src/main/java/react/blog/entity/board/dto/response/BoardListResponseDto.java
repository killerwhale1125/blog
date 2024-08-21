package react.blog.entity.board.dto.response;

import com.querydsl.core.annotations.QueryProjection;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import react.blog.document.BoardDocument;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BoardListResponseDto implements Serializable {
    private long boardNumber;
    private String title;
    private String content;
    private int favoriteCount;
    private int commentCount;
    private int viewCount;
    private LocalDateTime writeDatetime;
    private String writerNickname;
    private String email;

    public static BoardListResponseDto of(BoardDocument document) {
        return BoardListResponseDto.builder()
                .boardNumber(document.getId())
                .content(document.getContent())
                .title(document.getTitle())
                .viewCount(document.getViewCount())
                .favoriteCount(document.getFavoriteCount())
                .commentCount(document.getCommentCount())
                .writeDatetime(document.getWriteDateTime())
                .writerNickname(document.getWriteNickname())
                .email(document.getEmail())
                .build();
    }

    @QueryProjection
    public BoardListResponseDto(long boardNumber, String title, String content, int favoriteCount, int commentCount, int viewCount, LocalDateTime writeDatetime, String writerNickname, String email) {
        this.boardNumber = boardNumber;
        this.title = title;
        this.content = content;
        this.favoriteCount = favoriteCount;
        this.commentCount = commentCount;
        this.viewCount = viewCount;
        this.writeDatetime = writeDatetime;
        this.writerNickname = writerNickname;
        this.email = email;
    }
}
