package org.zerock.b01.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.zerock.b01.dto.MemberJoinDTO;

@Controller
@RequestMapping("/member")
@Log4j2
@RequiredArgsConstructor
public class MemberController {

    // 스프링 시큐리티 적용된 멤버 컨트롤러 역할



    @GetMapping("/login")
    public void loginGet(String ERROR, String logout) {// String logout-> http://localhost:8080/member/login?logout
        // String ERROR -> 아이디 존재하지 않아요 에러 처리용(로그인 과정에서 문제 시 처리)

        log.info("MemberController.loginGet 메서드 실행  - - - - - - -");
        log.info("logout : " + logout);

        if (logout != null) {
            log.info("사용자가 로그아웃 하였습니다 - - - - -");
        }

    }


    @GetMapping("/join")
    public void joinGET() {

        log.info("MemberController.joinGET 메서드 실행 중  - - - - - -");
    }

    @PostMapping("/join")
    public String joinPOST(MemberJoinDTO memberJoinDTO) {

        log.info("MemberController.joinPOST 메서드 실행 중  - - - - - -");
        log.info(memberJoinDTO);

       return "redirect:/board/list";
    }
}
