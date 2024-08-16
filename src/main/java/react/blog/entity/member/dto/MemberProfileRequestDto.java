package react.blog.entity.member.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import react.blog.entity.Member;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.ALWAYS)
public class MemberProfileRequestDto {
    private String email;
    private String nickname;
    private String profileImage;

    public static MemberProfileRequestDto toEntity(Member member) {
        return MemberProfileRequestDto.builder()
                .email(member.getEmail())
                .nickname(member.getNickname())
                .profileImage(member.getProfileImage())
                .build();
    }
}
