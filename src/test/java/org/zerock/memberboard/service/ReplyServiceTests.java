package org.zerock.memberboard.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.zerock.memberboard.dto.ReplyDTO;

import java.util.List;

@SpringBootTest
public class ReplyServiceTests {

    @Autowired
    private ReplyService service;

    @Test
    public void testGetList() {

        Long bno = 100L;//데이터베이스에 존재하는 번호

        List<ReplyDTO> replyDTOList = service.getList(bno);

        replyDTOList.forEach(replyDTO -> System.out.println(replyDTO));

    }
    /*

    Hibernate:
    select
        r1_0.rno,
        r1_0.board_bno,
        r1_0.moddate,
        r1_0.regdate,
        r1_0.replyer,
        r1_0.text
    from
        reply r1_0
    where
        r1_0.board_bno=?
    order by
        r1_0.rno
ReplyDTO(rno=40, text=Reply.......40, replyer=guest, bno=null, regDate=2024-10-15T11:35:04.431848, modDate=2024-10-15T11:35:04.431848)
ReplyDTO(rno=62, text=Reply.......62, replyer=guest, bno=null, regDate=2024-10-15T11:35:04.557858, modDate=2024-10-15T11:35:04.557858)
ReplyDTO(rno=74, text=Reply.......74, replyer=guest, bno=null, regDate=2024-10-15T11:35:04.602635, modDate=2024-10-15T11:35:04.602635)
     */

}
