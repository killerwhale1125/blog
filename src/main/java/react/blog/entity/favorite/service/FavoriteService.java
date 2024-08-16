package react.blog.entity.favorite.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import react.blog.entity.board.repository.BoardRepository;
import react.blog.common.BaseException;
import react.blog.entity.Board;
import react.blog.entity.Favorite;
import react.blog.entity.Member;
import react.blog.entity.favorite.dto.request.FavoriteListResponseDto;
import react.blog.entity.favorite.repository.FavoriteRepository;
import react.blog.entity.member.repository.MemberRepository;

import java.util.List;

import static react.blog.common.BaseResponseStatus.NOT_EXISTED_BOARD;
import static react.blog.common.BaseResponseStatus.NOT_EXISTED_USER;

@Service
@RequiredArgsConstructor
public class FavoriteService {

    private final MemberRepository memberRepository;
    private final BoardRepository boardRepository;
    private final FavoriteRepository favoriteRepository;

    @Transactional
    public void addFavorite(String email, Long boardId) {
        Member member = memberRepository.findMemberByEmail(email).orElseThrow(() -> new BaseException(NOT_EXISTED_USER));
        Board board = boardRepository.findById(boardId).orElseThrow(() -> new BaseException(NOT_EXISTED_BOARD));
        favoriteRepository.findFavoriteByMemberAndBoard(member, board).ifPresentOrElse(
                favorite -> {
                    board.removeFavoriteCount();
                    favoriteRepository.delete(favorite);
                },
                () -> {
                    board.addFavoriteCount();
                    favoriteRepository.save(Favorite.builder().board(board).member(member).build());
                }
        );
    }

    public List<FavoriteListResponseDto> findFavoriteList(Long boardId) {
        return favoriteRepository.findFavoriteMemberListByBoardId(boardId);
    }
}
