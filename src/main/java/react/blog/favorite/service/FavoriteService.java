package react.blog.favorite.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import react.blog.board.repository.BoardRepository;
import react.blog.common.BaseException;
import react.blog.domain.Board;
import react.blog.domain.Favorite;
import react.blog.domain.Member;
import react.blog.favorite.dto.request.FavoriteListResponseDto;
import react.blog.favorite.repository.FavoriteRepository;
import react.blog.member.repository.MemberRepository;

import java.util.List;
import java.util.stream.Collectors;

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
