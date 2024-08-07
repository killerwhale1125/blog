package react.blog.board.dto.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import react.blog.domain.Board;
import react.blog.domain.Image;
import react.blog.domain.Member;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CreateBoardRequestDto {
    @NotEmpty
    private String title;
    @NotEmpty
    private String content;

    public Board toEntity(Member member) {
        return Board.builder()
                .title(this.title)
                .content(this.content)
                .member(member)
                .build();
    }
}
