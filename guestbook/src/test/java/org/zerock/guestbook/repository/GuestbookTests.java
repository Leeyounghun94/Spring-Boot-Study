package org.zerock.guestbook.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.zerock.guestbook.entity.Guestbook;

import java.util.Optional;
import java.util.stream.IntStream;

@SpringBootTest
public class GuestbookTests {

    @Autowired
    private GuestbookRepository guestbookRepository;

    @Test
    public void insertDummies() {

        IntStream.rangeClosed(1, 300).forEach(i -> {
            Guestbook guestbook = Guestbook.builder()
                    .title("제목. . . . " + i)
                    .content("내용 . . . " + i)
                    .writer("user . . ." + (i % 10))    // user0...user1. . . user2....user3
                    .build();
            System.out.println(guestbookRepository.save(guestbook));
            // 리포지토리에 JPA 내장된 메서드인 .save 로 insert 처리함.
        });
    }


    @Test
    public void updateTest() {
        Optional<Guestbook> result = guestbookRepository.findById(300L);
        //  findById -> select * from guestBook where gno = 300

        if (result.isPresent()) {   // Optional 값이 null 이 아니면 ~

            Guestbook guestbook = result.get(); // 검색한 300 객체를 가져와서 guestBook에 넣는다.

            guestbook.chageTitle("수정된 제목~");
            guestbook.changeContent("수정된 내용~");
            guestbookRepository.save(guestbook);
            // save -> update set . . . .
        }

                    /* Hibernate:
                select
                    g1_0.gno,
                    g1_0.content,
                    g1_0.moddate,
                    g1_0.reg_date,
                    g1_0.title,
                    g1_0.writer
                from
                    guestbook g1_0
                where
                    g1_0.gno=?
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
                where
                    g1_0.gno=?
            Hibernate:
                update
                    guestbook
                set
                    content=?,
                    moddate=?,
                    title=?,
                    writer=?
                where
                    gno=?
                }

                     */
    }
}