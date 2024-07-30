package react.blog.dto.object;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;

@Data
public class FavoriteListItemDTO {
    private String email;
    private String nickname;
    private String profileImage;

    @QueryProjection
    public FavoriteListItemDTO(String email, String nickname, String profileImage) {
        this.email = email;
        this.nickname = nickname;
        this.profileImage = profileImage;
    }
}
