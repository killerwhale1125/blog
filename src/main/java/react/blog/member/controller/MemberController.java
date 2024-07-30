package react.blog.member.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import react.blog.common.BaseResponse;
import react.blog.domain.Member;
import react.blog.member.dto.LoginRequestDto;
import react.blog.member.dto.RegistDto;
import react.blog.utils.jwt.JwtToken;
import react.blog.member.service.MemberService;

import static react.blog.common.BaseResponseStatus.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class MemberController {

    private final MemberService memberService;
    private final PasswordEncoder passwordEncoder;

    /**
     * 로그인
     * @param loginRequestDto
     * @return
     */
    @GetMapping("/login")
    public BaseResponse<JwtToken> login(@RequestBody @Valid LoginRequestDto loginRequestDto) {
        return new BaseResponse<>(memberService.signIn(loginRequestDto.getUsername(), loginRequestDto.getPassword()));
    }

    /**
     * 회원가입
     * @param registDto
     * @return
     * @Description
     * 회원가입 할 때 중복검사를 하는데 3번 쿼리가 나가는 것을 고려해봐야함
     * 응답 값으로 인하여 세번 조회를 하는 것이 현명한 방법인지?
     */
    @PostMapping
    public BaseResponse<Void> registration(@RequestBody @Valid RegistDto registDto) {
        if(memberService.isDuplicatedEmail(registDto.getEmail()))
            return new BaseResponse<>(DUPLICATE_EMAIL);

        if(memberService.isDuplicatedNickname(registDto.getNickname()))
            return new BaseResponse<>(DUPLICATE_NICKNAME);

        if(memberService.isDuplicatedPhoneNumber(registDto.getTelNumber()))
            return new BaseResponse<>(DUPLICATE_TEL_NUMBER);

        memberService.registrationMember(RegistDto.toEntity(registDto, passwordEncoder));
        return new BaseResponse<>();
    }
}
