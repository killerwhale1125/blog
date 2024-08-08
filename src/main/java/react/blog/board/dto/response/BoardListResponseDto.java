package react.blog.board.dto.response;

import com.querydsl.core.annotations.QueryProjection;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
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

    @QueryProjection
    public BoardListResponseDto(long boardNumber, String title, String content, int favoriteCount, int commentCount, int viewCount, LocalDateTime writeDatetime, String writerNickname) {
        this.boardNumber = boardNumber;
        this.title = title;
        this.content = content;
        this.favoriteCount = favoriteCount;
        this.commentCount = commentCount;
        this.viewCount = viewCount;
        this.writeDatetime = writeDatetime;
        this.writerNickname = writerNickname;
    }
}
