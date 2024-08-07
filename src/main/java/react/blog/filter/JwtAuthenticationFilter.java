package react.blog.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import react.blog.common.BaseResponse;
import react.blog.common.BaseResponseStatus;
import react.blog.utils.jwt.JwtProvider;

import java.io.IOException;
import java.security.SignatureException;

import static jakarta.servlet.http.HttpServletResponse.SC_BAD_REQUEST;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static react.blog.common.BaseResponseStatus.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        try {

            String token = parseBearerToken(request);

            if(!StringUtils.hasText(token)) {
                // 토큰 없을 시 다음 필터로 넘김
                filterChain.doFilter(request, response);
                return;
            }

            String email = jwtProvider.validate(token);

            if(!StringUtils.hasText(email)) {
                // 이메일 없을 시 다음 필터로 넘김
                filterChain.doFilter(request, response);
                return;
            }

            // Authentication 인증 객체 생성
            AbstractAuthenticationToken authenticationToken
                    = new UsernamePasswordAuthenticationToken(email, null, AuthorityUtils.NO_AUTHORITIES);

            // 웹인증 세부정부 소스 -> Client IP, Session ID 를 WebAuthenticationDetails 에 생성하여 세부정보를 인증객체에 저장
            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            /**
             * JWT 토큰 자체에 인증 정보가 포함되어있기 때문에 AuthenticationManager에게 UsernamePasswordAuthenticationToken을 넘기지 않는다
             * 따라서 생략이 가능하다
             */
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        } catch (Exception e) {
            handleJwtException(e, response);
            return;
        }

        filterChain.doFilter(request, response);
    }

    public void setResponse(HttpServletResponse response, BaseResponseStatus baseResponseStatus) throws IOException {
        response.setStatus(SC_BAD_REQUEST);
        response.setContentType(APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("utf-8");
        BaseResponse<Void> baseResponse = new BaseResponse<>(baseResponseStatus);
        new ObjectMapper().writeValue(response.getWriter(), baseResponse);
    }

    private void handleJwtException(Exception e, HttpServletResponse response) throws IOException {
        BaseResponseStatus status;

        if (e instanceof SecurityException || e instanceof MalformedJwtException) {
            status = INVALID_TOKEN;
        } else if (e instanceof ExpiredJwtException) {
            status = EXPIRED_TOKEN;
        } else if (e instanceof UnsupportedJwtException) {
            status = UNSUPPORTED_TOKEN;
        } else if (e instanceof IllegalArgumentException) {
            status = TOKEN_ISEMPTY;
        } else if (e instanceof SignatureException) {
            status = INVALID_TOKEN;
        } else {
            status = INVALID_TOKEN;
        }

        logException(e, status);
        setResponse(response, status);
    }

    private void logException(Exception e, BaseResponseStatus status) {
        log.debug("JWT Exception: {}", status, e);
        log.error("JWT Exception: {}", status, e);
    }

    private String parseBearerToken(HttpServletRequest request) {
        String authorization = request.getHeader("Authorization");

        // null or 공백 or 길이 0 일 경우 false
        if(!StringUtils.hasText(authorization))
            return null;

        if(!authorization.startsWith("Bearer "))
            return null;

        // 토큰 반환
        return authorization.substring(7);
    }
}
