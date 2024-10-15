package org.zerock.memberboard.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.zerock.memberboard.dto.ReplyDTO;
import org.zerock.memberboard.service.ReplyService;

import java.util.List;

@RestController
@RequestMapping("/replies/")
@Log4j2
@RequiredArgsConstructor
public class ReplyController {

    private final ReplyService replyService;

    @GetMapping(value = "/board/{bno}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ReplyDTO>> getListByBoard(@PathVariable("bno") Long bno ){

        log.info("bno: " + bno);

        return new ResponseEntity<>( replyService.getList(bno), HttpStatus.OK);

        // RestController의 모든 메서드의 리턴 타입은 Json을 사용하며 반환 타입을 ResponseEntity 사용하는데 이걸 사용하면 http 상태 코드 등 같이 전달할 수 있다.
    }   // : BoardDTO(bno=200, title=Title...100, content=Content....100, writerEmail=user100@aaa.com, writerName=USER100, regDate=2024-10-15T15:06:49.848107, modDate=2024-10-15T15:06:49.848107, replyCount=0)


    @PostMapping("")
    public ResponseEntity<Long> replyInsert(@RequestBody ReplyDTO replyDTO) {
        //@RequestBody는 Json으로 들어오는 데이터를 자동으로 해당 타입의 객체로 매핑해주는 역할을 한다.

        log.info(replyDTO);

        Long rno = replyService.register(replyDTO);

        return  new ResponseEntity<>(rno, HttpStatus.OK);
    }


    @DeleteMapping("/{rno}")
    public ResponseEntity<String> replyDelete(@PathVariable("rno") Long rno) {

        log.info(" 번호 : " + rno);

        replyService.remove(rno);

        return new ResponseEntity<>("success", HttpStatus.OK);
    }


    @PutMapping("/{rno}")
    public ResponseEntity<String> replyModify(@RequestBody ReplyDTO replyDTO) {

        log.info(replyDTO);

        replyService.modify(replyDTO);

        return new ResponseEntity<>("success", HttpStatus.OK);
    }
}
