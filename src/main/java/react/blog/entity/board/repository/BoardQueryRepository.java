package react.blog.entity.board.repository;

import com.querydsl.core.QueryResults;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import react.blog.entity.board.dto.response.BoardListResponseDto;
import react.blog.entity.board.dto.response.QBoardListResponseDto;

import java.time.LocalDateTime;
import java.util.List;

import static react.blog.entity.QBoard.board;
import static react.blog.entity.QMember.member;

@Repository
public class BoardQueryRepository {
    private final EntityManager em;
    private final JPAQueryFactory queryFactory;

    public BoardQueryRepository(EntityManager em) {
        this.em = em;
        this.queryFactory = new JPAQueryFactory(em);
    }

    public Page<BoardListResponseDto> findLatestBoard(Pageable pageable) {
        QueryResults<BoardListResponseDto> results = queryFactory.select(
                new QBoardListResponseDto(
                        board.id,
                        board.title,
                        board.content,
                        board.favoriteCount,
                        board.commentCount,
                        board.viewCount,
                        board.createdDate,
                        member.nickname,
                        member.email))
                .from(board)
                .join(board.member, member)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();

        return new PageImpl<>(results.getResults(), pageable, results.getTotal());
    }

    public List<BoardListResponseDto> findAllByCreatedDateBetween(LocalDateTime startOfWeek, LocalDateTime endOfWeek) {
        return queryFactory.select(
                new QBoardListResponseDto(
                    board.id,
                    board.title,
                    board.content,
                    board.favoriteCount,
                    board.commentCount,
                    board.viewCount,
                    board.createdDate,
                    member.nickname,
                    member.email))
                .from(board)
                .join(board.member, member)
                .where(board.createdDate.between(startOfWeek, endOfWeek))
                .orderBy(board.favoriteCount.desc())
                .limit(3)
                .fetch();
    }

    public List<BoardListResponseDto> findBoardListByEmail(String email, Pageable pageable) {
        return queryFactory.select(new QBoardListResponseDto(
                board.id,
                board.title,
                board.content,
                board.favoriteCount,
                board.commentCount,
                board.viewCount,
                board.createdDate,
                member.nickname,
                member.email))
                .from(board)
                .leftJoin(board.member, member)
                .where(member.email.eq(email))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
    }
}
