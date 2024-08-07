package react.blog.member.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import react.blog.common.BaseResponse;
import react.blog.member.dto.MemberProfileRequestDto;
import react.blog.member.dto.UpdateNicknameRequestDto;
import react.blog.member.service.MemberService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/{email}")
    public BaseResponse<MemberProfileRequestDto> memberProfile(@PathVariable String email) {
        return new BaseResponse<>(memberService.findMemberByEmail(email));
    }

    @GetMapping
    public BaseResponse<MemberProfileRequestDto> loginMemberProfile(Authentication authentication) {
        return new BaseResponse<>(memberService.findMemberByEmail(authentication.getName()));
    }

    @PatchMapping("/nickname")
    public BaseResponse<Void> updateNickname(@RequestBody UpdateNicknameRequestDto nicknameRequestDto, Authentication authentication) {
        memberService.updateNickname(authentication.getName(), nicknameRequestDto.getNickname());
        return new BaseResponse<>();
    }

//    @PatchMapping("/profileImage")

}
