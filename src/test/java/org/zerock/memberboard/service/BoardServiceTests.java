package org.zerock.memberboard.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.zerock.memberboard.dto.BoardDTO;
import org.zerock.memberboard.dto.PageRequestDTO;
import org.zerock.memberboard.dto.PageResultDTO;

@SpringBootTest
public class BoardServiceTests {


    @Autowired
    private BoardService boardService;



    @Test
    public void testRegister() {

        BoardDTO dto = BoardDTO.builder()
                .title("Test.")
                .content("Test...")
                .writerEmail("user55@aaa.com")  //현재 데이터베이스에 존재하는 회원 이메일
                .build();

        Long bno = boardService.register(dto);

        /*
                Hibernate:
            select
                null,
                m1_0.moddate,
                m1_0.name,
                m1_0.password,
                m1_0.regdate
            from
                member m1_0
            where
                m1_0.email=?
        Hibernate:
            insert
            into
                board
                (content, moddate, regdate, title, writer_email)
            values
                (?, ?, ?, ?, ?)
         */
    }


    @Test
    public void testList() {

        //1페이지 10개
        PageRequestDTO pageRequestDTO = new PageRequestDTO();

        PageResultDTO<BoardDTO, Object[]> result = boardService.getList(pageRequestDTO);

        for (BoardDTO boardDTO : result.getDtoList()) {
            System.out.println(boardDTO);
        }

        /*
        Hibernate:
    select
        b1_0.bno,
        b1_0.content,
        b1_0.moddate,
        b1_0.regdate,
        b1_0.title,
        b1_0.writer_email,
        w1_0.email,
        w1_0.moddate,
        w1_0.name,
        w1_0.password,
        w1_0.regdate,
        count(r1_0.rno)
    from
        board b1_0
    left join
        member w1_0
            on w1_0.email=b1_0.writer_email
    left join
        reply r1_0
            on r1_0.board_bno=b1_0.bno
    group by
        b1_0.bno
    order by
        b1_0.bno desc
    limit
        ?
Hibernate:
    select
        count(b1_0.bno)
    from
        board b1_0
BoardDTO(bno=201, title=Test., content=Test..., writerEmail=user55@aaa.com, writerName=USER55, regDate=2024-10-15T15:31:28.381031, modDate=2024-10-15T15:31:28.381031, replyCount=0)
BoardDTO(bno=200, title=Title...100, content=Content....100, writerEmail=user100@aaa.com, writerName=USER100, regDate=2024-10-15T15:06:49.848107, modDate=2024-10-15T15:06:49.848107, replyCount=0)
BoardDTO(bno=199, title=Title...99, content=Content....99, writerEmail=user99@aaa.com, writerName=USER99, regDate=2024-10-15T15:06:49.843029, modDate=2024-10-15T15:06:49.843029, replyCount=0)
BoardDTO(bno=198, title=Title...98, content=Content....98, writerEmail=user98@aaa.com, writerName=USER98, regDate=2024-10-15T15:06:49.839276, modDate=2024-10-15T15:06:49.839276, replyCount=0)
BoardDTO(bno=197, title=Title...97, content=Content....97, writerEmail=user97@aaa.com, writerName=USER97, regDate=2024-10-15T15:06:49.836115, modDate=2024-10-15T15:06:49.836115, replyCount=0)
BoardDTO(bno=196, title=Title...96, content=Content....96, writerEmail=user96@aaa.com, writerName=USER96, regDate=2024-10-15T15:06:49.831103, modDate=2024-10-15T15:06:49.831103, replyCount=0)
BoardDTO(bno=195, title=Title...95, content=Content....95, writerEmail=user95@aaa.com, writerName=USER95, regDate=2024-10-15T15:06:49.828057, modDate=2024-10-15T15:06:49.828057, replyCount=0)
BoardDTO(bno=194, title=Title...94, content=Content....94, writerEmail=user94@aaa.com, writerName=USER94, regDate=2024-10-15T15:06:49.823097, modDate=2024-10-15T15:06:49.823097, replyCount=0)
BoardDTO(bno=193, title=Title...93, content=Content....93, writerEmail=user93@aaa.com, writerName=USER93, regDate=2024-10-15T15:06:49.817140, modDate=2024-10-15T15:06:49.817140, replyCount=0)
BoardDTO(bno=192, title=Title...92, content=Content....92, writerEmail=user92@aaa.com, writerName=USER92, regDate=2024-10-15T15:06:49.813198, modDate=2024-10-15T15:06:49.813198, replyCount=0)

     -> 1페이지에 해당하는 10개의 게시글, 회원, 댓글 수를 처리한다.    */

    }

    @Test
    public void testGet() {// 게시물 조회 테스트

        Long bno = 100L;

        BoardDTO boardDTO = boardService.get(bno);

        System.out.println(boardDTO);

        /*
        Hibernate:
    select
        b1_0.bno,
        b1_0.content,
        b1_0.moddate,
        b1_0.regdate,
        b1_0.title,
        b1_0.writer_email,
        w1_0.email,
        w1_0.moddate,
        w1_0.name,
        w1_0.password,
        w1_0.regdate,
        count(r1_0.rno)
    from
        board b1_0
    left join
        member w1_0
            on w1_0.email=b1_0.writer_email
    left join
        reply r1_0
            on r1_0.board_bno=b1_0.bno
    where
        b1_0.bno=?
BoardDTO(bno=100, title=Title...100, content=Content....100, writerEmail=user100@aaa.com, writerName=USER100, regDate=2024-10-15T11:32:25.665633, modDate=2024-10-15T11:32:25.665633, replyCount=3)

         */
    }


    @Test
    public void testRemove() {

        Long bno = 1L;

        boardService.removeWithReplies(bno);

        /*
                Hibernate:
            delete
            from
                reply
            where
                board_bno=?
        Hibernate:
            select
                b1_0.bno,
                b1_0.content,
                b1_0.moddate,
                b1_0.regdate,
                b1_0.title,
                b1_0.writer_email
            from
                board b1_0
            where
                b1_0.bno=?
        Hibernate:
            delete
            from
                board
            where
                bno=?

        -> 메서드 실행되면 댓글 테이블이 먼저 삭제되고 보드 테이블 조회하고 나서 삭제된다.

         */

    }

    @Test
    public void testModify() {

        BoardDTO boardDTO = BoardDTO.builder()
                .bno(2L)
                .title("제목 변경합니다.2")
                .content("내용 변경합니다.2")
                .build();

        boardService.modify(boardDTO);

        /*
        Hibernate:
    select
        b1_0.bno,
        b1_0.content,
        b1_0.moddate,
        b1_0.regdate,
        b1_0.title,
        b1_0.writer_email
    from
        board b1_0
    where
        b1_0.bno=?
Hibernate:
    update
        board
    set
        content=?,
        moddate=?,
        title=?,
        writer_email=?
    where
        bno=?


        -> select 이용해서 원래의 보드를 먼저 듸져보고 그다음에 업데이트를 실행한다.

         */
    }

}
