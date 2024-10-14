package org.zerock.guestbook.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.zerock.guestbook.dto.PageRequestDTO;
import org.zerock.guestbook.service.GuestbookService;

@Controller
@RequestMapping("/guestbook")   // http://localhost:80/questbook
@Log4j2
@RequiredArgsConstructor    // 생성자 자동 주입 final용
public class GuestbookController {

    private final GuestbookService service; // @RequiredArgsConstructor용 필드


    @GetMapping("/")
    public String index() {
        // http://localhost:80/ 로 올때 list로 보낸다.

        return "redirect:/guestbook/list";
    }

    @GetMapping({"/list"})
    public String list(PageRequestDTO pageRequestDTO, Model model) { //http://localhost:80/questbook/ or //http://localhost:80/questbook/list

    log.info("GuestbookController.list 메서드 실행. . . ..");

    model.addAttribute("result", service.getList(pageRequestDTO));
    // 페이징 처리 + DTO -> JPA -> Entity -> 모든 결과가 Model 담는다.

    return "/guestbook/list";   // resources/templates/guestbook/list.html

    }
}
