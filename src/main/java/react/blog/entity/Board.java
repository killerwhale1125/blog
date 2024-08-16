package react.blog.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import react.blog.utils.BaseTimeEntity;

@Entity
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Board extends BaseTimeEntity {

    @Id @GeneratedValue
    @Column(name = "board_id")
    private Long id;

    @NotNull
    private String title;
    @NotNull
    private String content;
    @NotNull
    private int viewCount;
    @NotNull
    private int commentCount;
    @NotNull
    private int favoriteCount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Builder
    public Board(String title, String content, Member member) {
        this.title = title;
        this.content = content;
        this.member = member;
        this.viewCount = 0;
        this.commentCount = 0;
        this.favoriteCount = 0;
    }

    public void updateBoard(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public void addFavoriteCount() {
        this.favoriteCount++;
    }

    public void removeFavoriteCount() {
        this.favoriteCount--;
    }
}
