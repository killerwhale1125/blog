package react.blog.image.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import react.blog.domain.Image;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ImageJdbcRepository {
    private static final String BULK_INSERT_SQL = "INSERT INTO " +
            "`image`(`image_name` , `image_url`, `created_time`, `modified_time`, `post_id`, `is_removed`) " +
            "VALUES(?, ?, ?, ?, ?, ?)";

    private final JdbcTemplate jdbcTemplate;

    /**
     * JdbcTemplate가 JPA보다 성능상 이점이 많아서 JdbcTemplate 사용
     */
    public void saveAll(List<Image> images) {
        jdbcTemplate.batchUpdate(BULK_INSERT_SQL,
                images,
                images.size(),
                (ps, image) -> {
                    LocalDateTime now = LocalDateTime.now();
                    ps.setString(1, image.getName());
                    ps.setString(2, image.getUrl());
                    ps.setTimestamp(3, Timestamp.valueOf(now));
                    ps.setTimestamp(4, Timestamp.valueOf(now));
                    ps.setLong(5, image.getBoard().getId());
                    ps.setBoolean(6, image.isRemoved());
                });
    }
}
