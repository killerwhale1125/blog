package react.blog.member.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import react.blog.common.BaseResponse;
import react.blog.member.dto.LoginRequestDto;
import react.blog.member.dto.MemberProfileRequestDto;
import react.blog.member.dto.RegistRequestDto;
import react.blog.utils.jwt.JwtProvider;
import react.blog.utils.jwt.JwtToken;
import react.blog.member.service.MemberService;

import static react.blog.common.BaseResponseStatus.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final MemberService memberService;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;

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
     * @param registRequestDto
     * @return
     * @Description
     * 회원가입 할 때 중복검사를 하는데 3번 쿼리가 나가는 것을 고려해봐야함
     * 응답 값으로 인하여 세번 조회를 하는 것이 현명한 방법인지?
     */
    @PostMapping
    public BaseResponse<Void> registration(@RequestBody @Valid RegistRequestDto registRequestDto) {
        // 중복 검사
        memberService.isDuplicatedEmail(registRequestDto.getEmail());
        memberService.isDuplicatedNickname(registRequestDto.getNickname());
        memberService.isDuplicatedPhoneNumber(registRequestDto.getTelNumber());

        memberService.registrationMember(RegistRequestDto.toEntity(registRequestDto, passwordEncoder));
        return new BaseResponse<>();
    }

    @PostMapping("/refresh")
    public BaseResponse<JwtToken> refresh(HttpServletRequest request) {
        return new BaseResponse<>(jwtProvider.refreshTokens(jwtProvider.resolveToken(request)));
    }

}
