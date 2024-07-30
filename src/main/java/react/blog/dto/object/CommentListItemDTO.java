package react.blog.dto.object;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;

@Data
public class CommentListItemDTO {
    private String nickname;
    private String profileImage;
    private String writeDatetime;
    private String content;

    @QueryProjection
    public CommentListItemDTO(String nickname, String profileImage, String writeDatetime, String content) {
        this.nickname = nickname;
        this.profileImage = profileImage;
        this.writeDatetime = writeDatetime;
        this.content = content;
    }
}
