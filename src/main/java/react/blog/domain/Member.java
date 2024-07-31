package react.blog.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import react.blog.utils.BaseTimeEntity;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseTimeEntity {

    @Id @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    @Column(unique = true)
    private String email;

    private String password;

    @Column(unique = true)
    private String nickname;

    @NotNull
    private String phoneNumber;

    @Embedded
    private Address address;

    private String profileImage;

    @Builder
    public Member(String email, String password, String nickname, String phoneNumber, Address address) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.phoneNumber = phoneNumber;
        this.address = address;
    }

    /**
     * 닉네임 변경
     * @param newNickname
     */
    public void changeNickname(String newNickname) {
        this.nickname = newNickname;
    }
}
