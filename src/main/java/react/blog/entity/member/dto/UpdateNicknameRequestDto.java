package react.blog.entity.member.dto;

import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class UpdateNicknameRequestDto {
    private String nickname;
}
