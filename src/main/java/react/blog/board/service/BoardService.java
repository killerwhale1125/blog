package react.blog.board.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import react.blog.board.dto.request.CreateBoardRequestDto;
import react.blog.board.dto.request.UpdateBoardRequestDto;
import react.blog.board.dto.response.BoardListResponseDto;
import react.blog.board.repository.BoardQueryRepository;
import react.blog.board.repository.BoardRepository;
import react.blog.common.BaseException;
import react.blog.domain.Board;
import react.blog.domain.Member;
import react.blog.member.repository.MemberRepository;

import java.util.List;

import static react.blog.common.BaseResponseStatus.*;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public void createBoard(String email, CreateBoardRequestDto boardRequestDto) {
        Board board = boardRequestDto.toEntity(memberRepository.findMemberByEmail(email).orElseThrow(() -> new BaseException(NOT_EXISTED_USER)));
        boardRepository.save(board);
    }

    @Transactional
    public void updateBoard(String email, UpdateBoardRequestDto boardRequestDto, Long boardId) {
        Member member = memberRepository.findMemberByEmail(email).orElseThrow(() -> new BaseException(NOT_EXISTED_USER));
        Board board = boardRepository.findById(boardId).orElseThrow(() -> new BaseException(NOT_EXISTED_BOARD));

        checkAuth(member, board);

        board.updateBoard(boardRequestDto.getTitle(), boardRequestDto.getContent());
        boardRepository.save(board);
    }

    @Transactional
    public void removeBoard(String email, Long boardId) {
        Member member = memberRepository.findMemberByEmail(email).orElseThrow(() -> new BaseException(NOT_EXISTED_USER));
        Board board = boardRepository.findById(boardId).orElseThrow(() -> new BaseException(NOT_EXISTED_BOARD));

        checkAuth(member, board);

        boardRepository.delete(board);
    }

    public Board findBoardById(Long boardId) {
        return boardRepository.findById(boardId).orElseThrow(() -> new BaseException(NOT_EXISTED_BOARD));
    }

    public void checkAuth(Member member, Board board) {
        if(board.getMember().getId() != member.getId()) throw new BaseException(AUTHORIZATION_FAIL);
    }
}
