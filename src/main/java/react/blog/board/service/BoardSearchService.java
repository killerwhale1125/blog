package react.blog.board.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import react.blog.board.dto.response.BoardListResponseDto;
import react.blog.board.repository.BoardQueryRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BoardSearchService {

    private final BoardQueryRepository boardQueryRepository;

    public List<BoardListResponseDto> findLatestBoard(Pageable pageable) {
        return boardQueryRepository.findLatestBoard(pageable).getContent();
    }
}
