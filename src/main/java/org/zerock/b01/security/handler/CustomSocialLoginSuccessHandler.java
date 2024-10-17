package org.zerock.b01.security.handler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.zerock.b01.security.dto.MemberSecurityDTO;

import java.io.IOException;

@Log4j2
@RequiredArgsConstructor
public class CustomSocialLoginSuccessHandler implements AuthenticationSuccessHandler {

    /* 로그인 성공과 실패를 커스터마이징 할 수 있는 CustomSocialLoginSuccessHandler + AuthenticationFaileHandler 제공 해준다 시큐리티에서
        소셜 로그인 성공 후 이동방법을 여기에서 처리하도록 한다!
     */

    private final PasswordEncoder passwordEncoder;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        log.info(" = = = = = = = = = = = = = = = = = = = == ");
        log.info("CustomSocialLoginSuccessHandler.onAuthenticationSuccess 메서드 실행  . . . . .  .");
        log.info(authentication.getPrincipal());

        MemberSecurityDTO memberSecurityDTO = (MemberSecurityDTO) authentication.getPrincipal();

        String encodedPw = memberSecurityDTO.getMpw();

        // 소셜 로그인이고 회원 패스워드 1111

        if (memberSecurityDTO.isSocial() && (memberSecurityDTO.getMpw().equals("1111") || passwordEncoder.matches("1111", memberSecurityDTO.getMpw()))) {

            log.info("비밀번호 변경 슈드~");

            log.info("멤버 수정으로 보내버려");
            response.sendRedirect("/member/modify");

            return;
        }else {

            response.sendRedirect("/board/list");
        }
    }

}
