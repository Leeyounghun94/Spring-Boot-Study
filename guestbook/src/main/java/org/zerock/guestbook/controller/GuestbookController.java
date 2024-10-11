package org.zerock.guestbook.controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/guestbook")   // http://localhost:80/questbook
@Log4j2
public class GuestbookController {

    @GetMapping({"/", "/list"})
    public String list() { //http://localhost:80/questbook/ or //http://localhost:80/questbook/list

    log.info("GuestbookController.list 메서드 실행. . . ..");
    return "/guestbook/list";   // resources/templates/guestbook/list.html

    }
}
