package react.blog.favorite.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import react.blog.domain.Board;
import react.blog.domain.Favorite;
import react.blog.domain.Member;
import react.blog.favorite.dto.request.FavoriteListResponseDto;

import java.util.List;
import java.util.Optional;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {

    Optional<Favorite> findFavoriteByMemberAndBoard(Member member, Board board);

    @Query("select new react.blog.favorite.dto.request.FavoriteListResponseDto(m.email, m.nickname)" +
            " from Favorite f" +
            " join f.member m" +
            " join f.board b" +
            " where b.id = :boardId")
    List<FavoriteListResponseDto> findFavoriteMemberListByBoardId(@Param("boardId") Long boardId);
}
