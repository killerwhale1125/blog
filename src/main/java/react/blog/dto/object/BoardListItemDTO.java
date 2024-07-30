package react.blog.dto.object;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;

@Data
public class BoardListItemDTO {
    private int boardNumber;
    private String title;
    private String content;
    private String boardTitleImage;
    private String favoriteCount;
    private int commentCount;
    private int viewCount;
    private String writeDatetime;
    private String writerNickname;
    private String writerProfileImage;

    @QueryProjection
    public BoardListItemDTO(int boardNumber, String title, String content, String boardTitleImage, String favoriteCount, int commentCount, int viewCount, String writeDatetime, String writerNickname, String writerProfileImage) {
        this.boardNumber = boardNumber;
        this.title = title;
        this.content = content;
        this.boardTitleImage = boardTitleImage;
        this.favoriteCount = favoriteCount;
        this.commentCount = commentCount;
        this.viewCount = viewCount;
        this.writeDatetime = writeDatetime;
        this.writerNickname = writerNickname;
        this.writerProfileImage = writerProfileImage;
    }
}
