package react.blog.entity.board.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import react.blog.entity.Board;

public interface BoardRepository extends JpaRepository<Board, Long> {

}
