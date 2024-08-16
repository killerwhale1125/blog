package react.blog.entity.member.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class LoginRequestDto {
    @NotEmpty
    private String username;
    @NotEmpty
    private String password;
}
