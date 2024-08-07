package react.blog.board.repository;

import com.querydsl.core.QueryResults;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import react.blog.board.dto.response.BoardListResponseDto;
import react.blog.board.dto.response.QBoardListResponseDto;

import static react.blog.domain.QBoard.board;
import static react.blog.domain.QMember.member;

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
                        board.createdDate.stringValue(),
                        member.nickname))
                .from(board)
                .join(board.member, member)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();

        return new PageImpl<>(results.getResults(), pageable, results.getTotal());
    }
}