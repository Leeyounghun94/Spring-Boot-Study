package org.zerock.guestbook.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.zerock.guestbook.entity.Guestbook;
import org.zerock.guestbook.entity.QGuestbook;

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

    @Test
    public void testQuery1() {
        // querydsl를 이용해서 단일 검색용    -> 0페이지 , 10개, 정렬(gno, 내림차순), 제목이 1이 들어있는 조건

        Pageable pageable = PageRequest.of(0, 10, Sort.by("gno").descending());
        //  0페이지 , 10개, 정렬(gno, 내림차순)

        QGuestbook qGuestbook = QGuestbook.guestbook;   // 쿼리dsl로 객체 생성

        String keyword = "1";

        BooleanBuilder builder = new BooleanBuilder();  // 조건이 where 이 있냐? 없냐?

        BooleanExpression expression = qGuestbook.title.contains(keyword);  // 제목의 1을 넣는다
        // 주의: import com.querydsl.core.types.dsl.BooleanExpression;

        builder.and(expression);    // 검색 where문 붙인다.

        Page<Guestbook> result = guestbookRepository.findAll(builder, pageable); // 찾아온 값을 페이징 처리
        // builder -> where, pageble -> 페이징 처리 + 정렬

        result.stream().forEach(guestbook -> {
            System.out.println(guestbook);

        });// 결과를 콘솔로 출력

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
                    g1_0.title like ? escape '!'
                order by
                    g1_0.gno desc
                limit
                    ?, ?
            Hibernate:
                select
                    count(g1_0.gno)
                from
                    guestbook g1_0
                where
                    g1_0.title like ? escape '!'
            Guestbook(gno=291, title=제목. . . . 291, content=내용 . . . 291, writer=user . . .1)
            Guestbook(gno=281, title=제목. . . . 281, content=내용 . . . 281, writer=user . . .1)
            Guestbook(gno=271, title=제목. . . . 271, content=내용 . . . 271, writer=user . . .1)
            Guestbook(gno=261, title=제목. . . . 261, content=내용 . . . 261, writer=user . . .1)
            Guestbook(gno=251, title=제목. . . . 251, content=내용 . . . 251, writer=user . . .1)
            Guestbook(gno=241, title=제목. . . . 241, content=내용 . . . 241, writer=user . . .1)
            Guestbook(gno=231, title=제목. . . . 231, content=내용 . . . 231, writer=user . . .1)
            Guestbook(gno=221, title=제목. . . . 221, content=내용 . . . 221, writer=user . . .1)
            Guestbook(gno=219, title=제목. . . . 219, content=내용 . . . 219, writer=user . . .9)
            Guestbook(gno=218, title=제목. . . . 218, content=내용 . . . 218, writer=user . . .8)

            title에 1이 나온걸 확인할 수 있다.
         */
    }

    @Test
    public void testQueryMulti() {
        // 제목과 내용을 where문으로 다중검색 테스트

        Pageable pageable = PageRequest.of(0, 7, Sort.by("gno").descending());
        //  0페이지 , 10개, 정렬(gno, 내림차순)

        QGuestbook qGuestbook = QGuestbook.guestbook;   // 쿼리dsl로 객체 생성

        String keyword = "2";

        BooleanBuilder builder = new BooleanBuilder();  // 조건이 where 이 있냐? 없냐?

        BooleanExpression exTitle = qGuestbook.title.contains(keyword);// 조건 1

        BooleanExpression exContent = qGuestbook.content.contains(keyword);// 조건 2

        // 조건들을 합체

        BooleanExpression exAll = exTitle.or(exTitle);  // where title or content -> (조건 : 제목이거나 내용)

        builder.and(exAll); // 조건문 합체

        builder.and(qGuestbook.gno.gt(0L)); // PK에 index를 활용하여 빠른 추출한다

        Page<Guestbook> result = guestbookRepository.findAll(builder, pageable);

        result.stream().forEach(guestbook -> {
            System.out.println(guestbook);
        });// 결과 콘솔 출력


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
                where
                    (
                        g1_0.title like ? escape '!'
                        or g1_0.title like ? escape '!'
                    )
                    and g1_0.gno>?
                order by
                    g1_0.gno desc
                limit
                    ?, ?
            Hibernate:
                select
                    count(g1_0.gno)
                from
                    guestbook g1_0
                where
                    (
                        g1_0.title like ? escape '!'
                        or g1_0.title like ? escape '!'
                    )
                    and g1_0.gno>?
            Guestbook(gno=299, title=제목. . . . 299, content=내용 . . . 299, writer=user . . .9)
            Guestbook(gno=298, title=제목. . . . 298, content=내용 . . . 298, writer=user . . .8)
            Guestbook(gno=297, title=제목. . . . 297, content=내용 . . . 297, writer=user . . .7)
            Guestbook(gno=296, title=제목. . . . 296, content=내용 . . . 296, writer=user . . .6)
            Guestbook(gno=295, title=제목. . . . 295, content=내용 . . . 295, writer=user . . .5)
            Guestbook(gno=294, title=제목. . . . 294, content=내용 . . . 294, writer=user . . .4)
            Guestbook(gno=293, title=제목. . . . 293, content=내용 . . . 293, writer=user . . .3)
         */
    }
}