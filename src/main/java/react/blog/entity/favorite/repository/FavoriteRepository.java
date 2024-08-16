package react.blog.entity.favorite.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import react.blog.entity.Board;
import react.blog.entity.Favorite;
import react.blog.entity.Member;
import react.blog.entity.favorite.dto.request.FavoriteListResponseDto;

import java.util.List;
import java.util.Optional;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {

    Optional<Favorite> findFavoriteByMemberAndBoard(Member member, Board board);

    @Query("select react.blog.entity.favorite.dto.request.FavoriteListResponseDto(m.email, m.nickname)" +
            " from Favorite f" +
            " join f.member m" +
            " join f.board b" +
            " where b.id = :boardId")
    List<FavoriteListResponseDto> findFavoriteMemberListByBoardId(@Param("boardId") Long boardId);
}
