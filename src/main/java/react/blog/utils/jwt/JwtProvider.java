package react.blog.utils.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtProvider {

    @Value("${security.jwt.secret-key}")
    private String secretKey;

    @Value("${security.jwt.token.access-expiration-time}")
    private long accessExpirationTime;

    @Value("${security.jwt.token.refresh-expiration-time}")
    private long refreshExpirationTime;

    private final RedisTemplate<String, String> redisTemplate;

    public JwtToken create(Authentication authentication) {
        /**
         * Access와 Refresh 토큰을 생성.
         * Expire Time은 yml 설정 파일로 관리
         * Login 마다 토큰 발급
         */
        String accessToken = createAccessToken(authentication);
        String refreshToken = createRefreshToken(authentication);

        return JwtToken.builder()
                .grantType("Bearer")
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    /**
     * Access 토큰 생성
     */
    public String createAccessToken(Authentication authentication) {
        Claims claims = Jwts.claims().setSubject(authentication.getName());
        Date now = new Date();
        Date expireDate = new Date(now.getTime() + accessExpirationTime);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expireDate)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    /**
     * Refresh 토큰 생성
     */
    public String createRefreshToken(Authentication authentication) {
        Claims claims = Jwts.claims().setSubject(authentication.getName());
        Date now = new Date();
        Date expireDate = new Date(now.getTime() + refreshExpirationTime);

        /**
         * Refresh 토큰 생성
         */
        String refreshToken = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expireDate)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();

        /**
         * Redis Ram에 Refresh 토큰 저장
         */
        redisTemplate.opsForValue().set(
                authentication.getName(),
                refreshToken,
                refreshExpirationTime,
                TimeUnit.MILLISECONDS
        );

        return refreshToken;
    }

    /**
     * 검증
     */
    public String validate(String jwt) {
        Claims claims = null;

        try {
            claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(jwt).getBody();
        } catch(Exception exception) {
            exception.printStackTrace();
            return null;
        }

        return claims.getSubject();
    }
}
