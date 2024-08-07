package react.blog.board.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import react.blog.domain.Board;

public interface BoardRepository extends JpaRepository<Board, Long> {

}
