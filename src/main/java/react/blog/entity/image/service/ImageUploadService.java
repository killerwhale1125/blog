package react.blog.entity.image.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import react.blog.entity.board.service.BoardService;
import react.blog.entity.Board;
import react.blog.entity.Image;
import react.blog.entity.image.repository.ImageJdbcRepository;
import react.blog.entity.image.utils.FileUtils;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ImageUploadService {

    private final AwsS3Service awsS3Service;
    private final BoardService postService;
    private final ImageJdbcRepository imageJdbcRepository;

    @Transactional
    public void upload(Long postId, List<MultipartFile> files) throws IOException {
        Board board = postService.findBoardById(postId);
        upload(files, board);
    }

    /**
     * 게시물에 이미지 파일 업로드
     */
    private void upload(List<MultipartFile> files, Board board) throws IOException {
        List<Image> images = uploadImageToStorageServer(files, board);
        imageJdbcRepository.saveAll(images);
    }

    /**
     * AWS S3 이미지 업로드
     */
    private List<Image> uploadImageToStorageServer(List<MultipartFile> files, Board board){
        return files.stream()
                .map(file -> {
                    try {
                        // file 이름은 랜덤으로 생성
                        String filename = FileUtils.getRandomFilename();
                        /**
                         * File path를 가져오기 위한 과정
                         * 1. File 확장자명 유효성을 검사 하여 Filepath fullname 생성
                         * 2. 생성한 fullname으로 AWS S3에 파일 업로드 
                         * 3. S3 bucket에 업로드한 해당 AWS client의 파일 fath를 가져옴
                         */
                        String filepath = awsS3Service.upload(file, filename);
                        return Image.builder()
                                .name(filename)
                                .url(filepath)
                                .board(board)
                                .build();
                    } catch (IOException e) {
                        throw new RuntimeException("Failed to upload file", e);
                    }
                })
                .collect(Collectors.toList());
    }
}
