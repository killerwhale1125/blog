package react.blog.utils.jwt;

import io.jsonwebtoken.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import react.blog.common.BaseException;

import java.security.SignatureException;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static react.blog.common.BaseResponseStatus.*;

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
    public String validate(String token){
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token).getBody().getSubject();
    }

    public String resolveToken(HttpServletRequest request) {
        String authorization = request.getHeader("Authorization");

        // null or 공백 or 길이 0 일 경우 false
        if(!StringUtils.hasText(authorization))
            return null;

        if(!authorization.startsWith("Bearer "))
            return null;

        // 토큰 반환
        return authorization.substring(7);
    }

    /**
     * 1. refresh 토큰 검증
     * 2. Redis 토큰과 동등성 비교
     * 3. 토큰에 담긴 정보로 유저 조회 후 새 토큰 발급
     */
    public JwtToken refreshTokens(String refreshToken) {
        /**
         * Refresh 토큰 검증
         * 토큰 값이 존재하는데 검증 실패 시 재 로그인 필요
         */
        validate(refreshToken);

        Claims claims = parseClaims(refreshToken);
        String username = claims.getSubject();

        /**
         * Redis에 저장된 Refresh 토큰 조회
         * Refresh 만료 시 Redis에 자동 삭제되기 때문에 서버 측 검증 X
         * null -> 만료
         */
        String redisRefreshToken = redisTemplate.opsForValue().get(username);

        /**
         * Refresh 토큰 null 체크 & 동등성 여부 판단
         */
        if(!StringUtils.hasText(redisRefreshToken) || !refreshToken.equals(redisRefreshToken)) {
            throw new BaseException(TOKEN_MISMATCHED);
        }

        /**
         * 토큰에 담긴 유저 정보로 Authentication 객체 조회
         * 보안을 고려하여 Refresh 토큰도 만료되지 않았더라도 재발급
         */
        Authentication authentication = getAuthenticationFromClaims(claims);
        String newAccessToken = createAccessToken(authentication);
        String newRefreshToken = createRefreshToken(authentication);

        return JwtToken.builder()
                .grantType("Bearer")
                .accessToken(newAccessToken)
                .refreshToken(newRefreshToken)
                .build();
    }

    /**
     * JWT 토큰 복호화
     */
    private Claims parseClaims(String accessToken) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(accessToken)
                    .getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }

    /**
     * Token 정보로 Authentication 객체 생성
     */
    private Authentication getAuthenticationFromClaims(Claims claims) {
        String username = claims.getSubject();

        // 사용자 인증만을 위해 authorities를 빈 리스트로 설정
        List<GrantedAuthority> authorities = Collections.emptyList();

        UserDetails principal = new User(username, "", authorities);
        return new UsernamePasswordAuthenticationToken(principal, "", authorities);
    }
}
