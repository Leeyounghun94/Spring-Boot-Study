package org.zerock.guestbook.service;


import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.zerock.guestbook.dto.GuestbookDTO;
import org.zerock.guestbook.dto.PageRequestDTO;
import org.zerock.guestbook.dto.PageResultDTO;
import org.zerock.guestbook.entity.Guestbook;

@SpringBootTest
@Log4j2
public class GuestbookServiceTests {


    @Autowired
    private GuestbookService service;


    @Test
    public void testRegister() {

        GuestbookDTO guestbookDTO = GuestbookDTO.builder()
                .title("서비스테스트용 제목")
                .content("서비스테스트 내용")
                .writer("서비스 사용자")
                .build();

        log.info("서비스 테스트 중 : 엔티티 출력 -> " + service.register(guestbookDTO));
        // 서비스 테스트 중 : 엔티티 출력 -> null -> null 처리로 하면 db로 안간다.

        /* JPA 주입 후 ->
        Hibernate:
            insert
            into
                guestbook
                (content, moddate, reg_date, title, writer)
            values
                (?, ?, ?, ?, ?)
        2024-10-14T10:57:03.236+09:00  INFO 2140 --- [guestbook] [    Test worker] o.z.g.service.GuestbookServiceTests      : 서비스 테스트 중 : 엔티티 출력 -> 301
         */
    }

    @Test
    public void testList() {

        PageRequestDTO pageRequestDTO = PageRequestDTO.builder()
                .page(31)
                .size(10)
                .build();

        PageResultDTO<GuestbookDTO, Guestbook> resultDTO = service.getList(pageRequestDTO);

        for (GuestbookDTO guestbookDTO : resultDTO.getDtoList()) {

            System.out.println("방명록 리스트" + guestbookDTO);

        }

        System.out.println("페이징 처리" + pageRequestDTO);
        System.out.println("이전 : " + resultDTO.isPrev());
        System.out.println("다음 : " + resultDTO.isNext());
        System.out.println("총 페이지 : " + resultDTO.getTotalPage());
        System.out.println("=======================================================================");
        resultDTO.getPageList().forEach(i -> System.out.println(i));

        /*
                Hibernate:
            select
                g1_0.gno,
                g1_0.content,
                g1_0.moddate,
                g1_0.reg_date,
                g1_0.title,
                g1_0.writer
            from
                guestbook g1_0
            order by
                g1_0.gno desc
            limit
                ?, ?
        Hibernate:
            select
                count(g1_0.gno)
            from
                guestbook g1_0
        방명록 리스트GuestbookDTO(gno=301, title=서비스테스트용 제목, content=서비스테스트 내용, writer=서비스 사용자, regDate=2024-10-14T10:57:03.168803, modDate=2024-10-14T10:57:03.168803)
        방명록 리스트GuestbookDTO(gno=300, title=수정된 제목~, content=수정된 내용~, writer=user . . .0, regDate=2024-10-11T15:48:08.395234, modDate=2024-10-11T15:57:59.607375)
        방명록 리스트GuestbookDTO(gno=299, title=제목. . . . 299, content=내용 . . . 299, writer=user . . .9, regDate=2024-10-11T15:48:08.392341, modDate=2024-10-11T15:48:08.392341)
        방명록 리스트GuestbookDTO(gno=298, title=제목. . . . 298, content=내용 . . . 298, writer=user . . .8, regDate=2024-10-11T15:48:08.388196, modDate=2024-10-11T15:48:08.388196)
        방명록 리스트GuestbookDTO(gno=297, title=제목. . . . 297, content=내용 . . . 297, writer=user . . .7, regDate=2024-10-11T15:48:08.384835, modDate=2024-10-11T15:48:08.384835)
        방명록 리스트GuestbookDTO(gno=296, title=제목. . . . 296, content=내용 . . . 296, writer=user . . .6, regDate=2024-10-11T15:48:08.376081, modDate=2024-10-11T15:48:08.376081)
        방명록 리스트GuestbookDTO(gno=295, title=제목. . . . 295, content=내용 . . . 295, writer=user . . .5, regDate=2024-10-11T15:48:08.371069, modDate=2024-10-11T15:48:08.371069)
        방명록 리스트GuestbookDTO(gno=294, title=제목. . . . 294, content=내용 . . . 294, writer=user . . .4, regDate=2024-10-11T15:48:08.365095, modDate=2024-10-11T15:48:08.365095)
        방명록 리스트GuestbookDTO(gno=293, title=제목. . . . 293, content=내용 . . . 293, writer=user . . .3, regDate=2024-10-11T15:48:08.359127, modDate=2024-10-11T15:48:08.359127)
        방명록 리스트GuestbookDTO(gno=292, title=제목. . . . 292, content=내용 . . . 292, writer=user . . .2, regDate=2024-10-11T15:48:08.355115, modDate=2024-10-11T15:48:08.355115)
        페이징 처리PageRequestDTO(page=1, size=10)



                             Hibernate:
                        select
                            g1_0.gno,
                            g1_0.content,
                            g1_0.moddate,
                            g1_0.reg_date,
                            g1_0.title,
                            g1_0.writer
                        from
                            guestbook g1_0
                        order by
                            g1_0.gno desc
                        limit
                            ?, ?
                    Hibernate:
                        select
                            count(g1_0.gno)
                        from
                            guestbook g1_0
                    방명록 리스트GuestbookDTO(gno=301, title=서비스테스트용 제목, content=서비스테스트 내용, writer=서비스 사용자, regDate=2024-10-14T10:57:03.168803, modDate=2024-10-14T10:57:03.168803)
                    방명록 리스트GuestbookDTO(gno=300, title=수정된 제목~, content=수정된 내용~, writer=user . . .0, regDate=2024-10-11T15:48:08.395234, modDate=2024-10-11T15:57:59.607375)
                    방명록 리스트GuestbookDTO(gno=299, title=제목. . . . 299, content=내용 . . . 299, writer=user . . .9, regDate=2024-10-11T15:48:08.392341, modDate=2024-10-11T15:48:08.392341)
                    방명록 리스트GuestbookDTO(gno=298, title=제목. . . . 298, content=내용 . . . 298, writer=user . . .8, regDate=2024-10-11T15:48:08.388196, modDate=2024-10-11T15:48:08.388196)
                    방명록 리스트GuestbookDTO(gno=297, title=제목. . . . 297, content=내용 . . . 297, writer=user . . .7, regDate=2024-10-11T15:48:08.384835, modDate=2024-10-11T15:48:08.384835)
                    방명록 리스트GuestbookDTO(gno=296, title=제목. . . . 296, content=내용 . . . 296, writer=user . . .6, regDate=2024-10-11T15:48:08.376081, modDate=2024-10-11T15:48:08.376081)
                    방명록 리스트GuestbookDTO(gno=295, title=제목. . . . 295, content=내용 . . . 295, writer=user . . .5, regDate=2024-10-11T15:48:08.371069, modDate=2024-10-11T15:48:08.371069)
                    방명록 리스트GuestbookDTO(gno=294, title=제목. . . . 294, content=내용 . . . 294, writer=user . . .4, regDate=2024-10-11T15:48:08.365095, modDate=2024-10-11T15:48:08.365095)
                    방명록 리스트GuestbookDTO(gno=293, title=제목. . . . 293, content=내용 . . . 293, writer=user . . .3, regDate=2024-10-11T15:48:08.359127, modDate=2024-10-11T15:48:08.359127)
                    방명록 리스트GuestbookDTO(gno=292, title=제목. . . . 292, content=내용 . . . 292, writer=user . . .2, regDate=2024-10-11T15:48:08.355115, modDate=2024-10-11T15:48:08.355115)
                    페이징 처리PageRequestDTO(page=1, size=10)
                    이전 : false
                    다음 : true
                    총 페이지 : 31
                    =======================================================================
                    1
                    2
                    3
                    4
                    5
                    6
                    7
                    8
                    9
                    10

        /*
        이전 : true
        다음 : false
        총 페이지 : 31
        =======================================================================
        31
         */

    }
}
