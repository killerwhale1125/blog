package react.blog.entity.board.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import react.blog.document.BoardDocument;
import react.blog.entity.board.dto.request.CreateBoardRequestDto;
import react.blog.entity.board.dto.request.UpdateBoardRequestDto;
import react.blog.entity.board.repository.BoardElasticSearchRepository;
import react.blog.entity.board.repository.BoardRepository;
import react.blog.common.BaseException;
import react.blog.entity.Board;
import react.blog.entity.Member;
import react.blog.entity.member.repository.MemberRepository;

import static react.blog.common.BaseResponseStatus.*;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final MemberRepository memberRepository;
    private final BoardElasticSearchRepository boardElasticSearchRepository;

    /**
     * 게시물 생성 시 Elasticsearch 인덱싱
     * @param email
     * @param boardRequestDto
     */
    @Transactional
    public void createBoard(String email, CreateBoardRequestDto boardRequestDto) {
        Member member = memberRepository.findMemberByEmail(email).orElseThrow(() -> new BaseException(NOT_EXISTED_USER));
        Board board = boardRequestDto.toEntity(member);
        boardRepository.save(board);
        boardElasticSearchRepository.save(BoardDocument.of(board));
    }

    /**
     * 게시물 수정 시 Elasticsearch 인덱싱
     * @param email
     * @param boardRequestDto
     * @param boardId
     */
    @Transactional
    public void updateBoard(String email, UpdateBoardRequestDto boardRequestDto, Long boardId) {
        Member member = memberRepository.findMemberByEmail(email).orElseThrow(() -> new BaseException(NOT_EXISTED_USER));
        Board board = boardRepository.findById(boardId).orElseThrow(() -> new BaseException(NOT_EXISTED_BOARD));

        checkAuth(member, board);

        board.updateBoard(boardRequestDto.getTitle(), boardRequestDto.getContent());
        boardRepository.save(board);
        boardElasticSearchRepository.save(BoardDocument.of(board));
    }

    @Transactional
    public void removeBoard(String email, Long boardId) {
        Member member = memberRepository.findMemberByEmail(email).orElseThrow(() -> new BaseException(NOT_EXISTED_USER));
        Board board = boardRepository.findById(boardId).orElseThrow(() -> new BaseException(NOT_EXISTED_BOARD));

        checkAuth(member, board);

        boardRepository.delete(board);
        boardElasticSearchRepository.deleteById(board.getId().toString());
    }

    public Board findBoardById(Long boardId) {
        return boardRepository.findById(boardId).orElseThrow(() -> new BaseException(NOT_EXISTED_BOARD));
    }

    public void checkAuth(Member member, Board board) {
        if(board.getMember().getId() != member.getId()) throw new BaseException(AUTHORIZATION_FAIL);
    }

}
