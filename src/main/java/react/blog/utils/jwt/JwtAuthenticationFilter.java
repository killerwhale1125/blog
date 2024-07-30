package react.blog.utils.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

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

        } catch(Exception exception) {
            exception.printStackTrace();
        }

        filterChain.doFilter(request, response);
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
