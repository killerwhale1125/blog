package react.blog.domain;

import jakarta.persistence.*;
import lombok.*;
import react.blog.utils.BaseTimeEntity;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Image extends BaseTimeEntity {

    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "IMAGE_NAME")
    private String name;

    @Column(name = "IMAGE_URL")
    private String url;

    @Column(name = "IS_REMOVED")
    private boolean removed;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private Board board;

    @Builder
    public Image(String name, String url, Board board) {
        this.name = name;
        this.url = url;
        this.board = board;
    }
}
