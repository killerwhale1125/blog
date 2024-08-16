package react.blog.entity.member.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import react.blog.common.BaseException;
import react.blog.entity.Member;
import react.blog.entity.member.dto.MemberProfileRequestDto;
import react.blog.entity.member.repository.MemberRepository;
import react.blog.utils.jwt.JwtProvider;
import react.blog.utils.jwt.JwtToken;

import static react.blog.common.BaseResponseStatus.*;

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

    public void isDuplicatedEmail(String email) {
        if(memberRepository.existsByEmail(email)) throw new BaseException(DUPLICATE_EMAIL);

    }

    public void isDuplicatedNickname(String nickname) {
        if(memberRepository.existsByNickname(nickname)) throw new BaseException(DUPLICATE_NICKNAME);
    }

    public void isDuplicatedPhoneNumber(String phoneNumber) {
        if(memberRepository.existsByPhoneNumber(phoneNumber)) throw new BaseException(DUPLICATE_TEL_NUMBER);
    }

    public MemberProfileRequestDto findMemberByEmail(String email) {
        return MemberProfileRequestDto.toEntity(memberRepository.findMemberByEmail(email).orElseThrow(() -> new BaseException(NOT_EXISTED_USER)));
    }

    @Transactional
    public void updateNickname(String email, String newNickname) {
        isDuplicatedNickname(newNickname);

        Member member = memberRepository.findMemberByEmail(email).orElseThrow(() -> new BaseException(NOT_EXISTED_USER));
        member.changeNickname(newNickname);
    }
}
