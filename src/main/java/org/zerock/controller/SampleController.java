package org.zerock.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController // 기본
public class SampleController {

    @GetMapping("/hello")
    public String[] hello() {
        return new String[]{"Hello", "World", "Java", "Cloud", "Security"};
        // RestController 사용 시 기본 출력은 JSON으로 나온다.
    }
}
