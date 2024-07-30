package react.blog.member.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import react.blog.common.BaseResponse;
import react.blog.domain.Member;
import react.blog.member.dto.RegistDto;
import react.blog.utils.jwt.JwtProvider;
import react.blog.utils.jwt.JwtToken;
import react.blog.member.repository.MemberRepository;

import static react.blog.common.BaseResponseStatus.DUPLICATE_EMAIL;
import static react.blog.common.BaseResponseStatus.DUPLICATE_NICKNAME;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final MemberRepository memberRepository;
    private final JwtProvider jwtProvider;

    @Transactional
    public JwtToken signIn(String username, String password) {
            /**
             * 입력 정보를 토대로 인증 객체 생성 ( 아직은 인증되지 않은 정보 )
             */
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);

        AuthenticationManager authenticationManager = authenticationManagerBuilder.getObject();
        /**
         * Manager에게 인증되지 않은 authenticationToken 객체를 넘겨줘서 판별
         * authenticate 메서드가 실행될 때 MemberDetailsService 에서 만든 loadUserByUsername 메서드 실행
         * Manager는 Authentication 객체를 인증할 적절한 Provider를 찾아야 함
         * 따라서 loadUserByUsername을 호출하는 것은 AuthenticationProvider에 의해 호출됨
         */
        Authentication authenticate = authenticationManager.authenticate(authenticationToken);

        /**
         * 인증된 정보를 기반으로 JWT 객체 생성
         */
        return jwtProvider.create(authenticate);
    }

    @Transactional
    public void registrationMember(Member member) {
        memberRepository.save(member);
    }

    public boolean isDuplicatedEmail(String email) {
        return memberRepository.existsByEmail(email);
    }

    public boolean isDuplicatedNickname(String nickname) {
        return memberRepository.existsByNickname(nickname);
    }

    public boolean isDuplicatedPhoneNumber(String phoneNumber) {
        return memberRepository.existsByPhoneNumber(phoneNumber);
    }
}
