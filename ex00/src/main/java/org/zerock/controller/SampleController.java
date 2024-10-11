package org.zerock.controller;


import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.zerock.controller.dto.SampleDTO;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


@Controller // 기본
@RequestMapping("/sample")  //  http://localhost:80/sample
@Log4j2
public class SampleController {

    @GetMapping("/hello")
    public String[] hello() {//     http://localhost:80/sample/hello
        return new String[]{"Hello", "World", "Java", "Cloud", "Security"};
        // RestController 사용 시 기본 출력은 JSON으로 나온다.
    }

    @GetMapping("/ex1")
    public void ex1() {  //  http://localhost:80/sample/ex1
        // 리턴이 void이면은 ex1.html을 찾는다  -> resources/templates/sample/ex1.html을 찾는다.

        // 리턴이 String 이면 리턴으로 지정해준 경로로 리턴해준다.
        log.info("ex1 메서드 실행 . . . . .");   // 콘솔에 로그 찍힌다.
    }

    @GetMapping({"/ex2","/exLink"}) // 이렇게 하면 ex2, exLink 둘다 처리한다.
    public void exModel(Model model) {
        // 스프링의 모델 영역을 이용하여 데이터 전달
        // 더미데이터 20개 만들어서 프론트로 출력하기

        List<SampleDTO> list = IntStream.rangeClosed(1, 20).asLongStream().mapToObj(i -> {
           SampleDTO dto = SampleDTO.builder()
                   .sno(i)
                   .first("first.." + i)
                   .last("Last.." + i)
                   .regTime(LocalDateTime.now())
                   .build();
           return dto;
        }).collect(Collectors.toList());

        model.addAttribute("list", list);

    }

    @GetMapping("/exInLine")
    public String exInLine(RedirectAttributes redirectAttributes) {
        // RedirectAttributes addflashAttribute를 이용해서 1회용 객체를 처리한다.
        // http://localhost:80/exInLine 을 호출하면 1회용 값 2개를 ex3.html에다가 전달한다.
        log.info("exInLine 메서드 실행중 . . . . ");

        SampleDTO dto = SampleDTO.builder()
                .sno(100L)
                .first("first..100")
                .last("last..100")
                .regTime(LocalDateTime.now())
                .build();

        redirectAttributes.addFlashAttribute("result", "success");
        redirectAttributes.addFlashAttribute("dto", dto);
        return "redirect:/sample/ex3";
    }

    @GetMapping("/ex3")
    public void ex3() {// void -> resources/temples/sample/ex3.html 으로 리턴
        log.info("ex3메서드 실행 . . . .");
    }


    @GetMapping({"/exLayout1", "/exLayout2", "/exTemplate", "/exSidebar"})
    public void exLayout1() {

        log.info("exLayout1 메서드 실행 . . . . .");
    }


}
