package org.zerock.memberboard.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;
import org.zerock.memberboard.entity.Board;
import org.zerock.memberboard.entity.Member;


import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

@SpringBootTest
public class BoardRepositoryTests {


    @Autowired
    private BoardRepository boardRepository;


    @Test
    public void insertBoard() {// 사용자 1명이 하나의 게시물을 등록

        IntStream.rangeClosed(1, 100).forEach(i -> {

            Member member = Member.builder().email("user" + i + "@aaa.com").build();

            Board board = Board.builder()
                    .title("Title..." + i)
                    .content("Content...." + i)
                    .writer(member)
                    .build();

            boardRepository.save(board);

        });

    }


    @Transactional  // 해당 메서드를 하나의 트랜젝션으로 처리하라는 의미.
    @Test
    public void testRead1() {

        Optional<Board> result = boardRepository.findById(100L); //데이터베이스에 존재하는 번호

        Board board = result.get();

        System.out.println(board);
        System.out.println(board.getWriter());

    }

    /*
            Hibernate:
            select
                b1_0.bno,
                b1_0.content,
                b1_0.moddate,
                b1_0.regdate,
                b1_0.title,
                w1_0.email,
                w1_0.moddate,
                w1_0.name,
                w1_0.password,
                w1_0.regdate
            from
                board b1_0
            left join
                member w1_0
                    on w1_0.email=b1_0.writer_email
            where
                b1_0.bno=?
        Board(bno=100, title=Title...100, content=Content....100)
        Member(email=user100@aaa.com, password=1111, name=USER100)


        (fetch = FetchType.LAZY) -> 지연 로딩 적용 한 후

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
            Board(bno=100, title=Title...100, content=Content....100)
            Hibernate:
                select
                    m1_0.email,
                    m1_0.moddate,
                    m1_0.name,
                    m1_0.password,
                    m1_0.regdate
                from
                    member m1_0
                where
                    m1_0.email=?
            Member(email=user100@aaa.com, password=1111, name=USER100)

            전과 달리 필요한것만 가져올수 있는걸 알 수 있다.

            보드테이블 Get.writer 하기 위해 멤버 테이블 로딩하는걸 볼 수 있다.
            즉, 지연로딩 하지 않으면 자동으로 보드테이블과 멤버 테이블이 조인으로 처리 되는 차이를 알 수 있다.
     */

    @Test
    public void testReadWithWriter() {
        Object result = boardRepository.getBoardWithWriter(100L);
        Object[] arr = (Object[]) result;
        System.out.println("-------------------------------");
        System.out.println(Arrays.toString(arr));

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
                w1_0.regdate
            from
                board b1_0
            left join
                member w1_0
                    on w1_0.email=b1_0.writer_email
            where
                b1_0.bno=?
        -------------------------------
        [Board(bno=100, title=Title...100, content=Content....100), Member(email=user100@aaa.com, password=1111, name=USER100)]


        지연 로딩으로 처리되었으나 조인 처리가 한꺼번에 되어서 보드와 멤버테이블을 이용하는 걸 확인.
     */


    @Test
    public void testGetBoardWithReply() {

        List<Object[]> result = boardRepository.getBoardWithReply(100L);

        for (Object[] arr : result) {
            System.out.println(Arrays.toString(arr));
        }
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
        r1_0.rno,
        r1_0.board_bno,
        r1_0.moddate,
        r1_0.regdate,
        r1_0.replyer,
        r1_0.text
    from
        board b1_0
    left join
        reply r1_0
            on r1_0.board_bno=b1_0.bno
    where
        b1_0.bno=?
[Board(bno=100, title=Title...100, content=Content....100), Reply(rno=40, text=Reply.......40, replyer=guest)]
[Board(bno=100, title=Title...100, content=Content....100), Reply(rno=62, text=Reply.......62, replyer=guest)]
[Board(bno=100, title=Title...100, content=Content....100), Reply(rno=74, text=Reply.......74, replyer=guest)]


       */

    @Test
    public void testWithReplyCount(){

        Pageable pageable = PageRequest.of(0,10, Sort.by("bno").descending());

        Page<Object[]> result = boardRepository.getBoardWithReplyCount(pageable);

        result.get().forEach(row -> {

            Object[] arr = (Object[])row;

            System.out.println(Arrays.toString(arr));
        });

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
            [Board(bno=200, title=Title...100, content=Content....100), Member(email=user100@aaa.com, password=1111, name=USER100), 0]
            [Board(bno=199, title=Title...99, content=Content....99), Member(email=user99@aaa.com, password=1111, name=USER99), 0]
            [Board(bno=198, title=Title...98, content=Content....98), Member(email=user98@aaa.com, password=1111, name=USER98), 0]
            [Board(bno=197, title=Title...97, content=Content....97), Member(email=user97@aaa.com, password=1111, name=USER97), 0]
            [Board(bno=196, title=Title...96, content=Content....96), Member(email=user96@aaa.com, password=1111, name=USER96), 0]
            [Board(bno=195, title=Title...95, content=Content....95), Member(email=user95@aaa.com, password=1111, name=USER95), 0]
            [Board(bno=194, title=Title...94, content=Content....94), Member(email=user94@aaa.com, password=1111, name=USER94), 0]
            [Board(bno=193, title=Title...93, content=Content....93), Member(email=user93@aaa.com, password=1111, name=USER93), 0]
            [Board(bno=192, title=Title...92, content=Content....92), Member(email=user92@aaa.com, password=1111, name=USER92), 0]
            [Board(bno=191, title=Title...91, content=Content....91), Member(email=user91@aaa.com, password=1111, name=USER91), 0]
         */
    }

    @Test
    public void testRead3() {
        Object result = boardRepository.getBoardByBno(100L);
        Object[] arr = (Object[]) result;

        System.out.println(Arrays.toString(arr));
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
    where
        b1_0.bno=?
[Board(bno=100, title=Title...100, content=Content....100), Member(email=user100@aaa.com, password=1111, name=USER100), 3]
     */

    @Test
    public void testSearch1() {
        boardRepository.search1();

        /*
        from Board board
  left join Member member1 with board.writer = member1
  left join Reply reply with reply.board = board
group by board
2024-10-15T16:37:01.001+09:00  INFO 10220 --- [memberboard] [    Test worker] o.z.m.r.s.SearchBoardRepositoryImpl      : ---------------------------
Hibernate:
    select
        b1_0.bno,
        b1_0.content,
        b1_0.moddate,
        b1_0.regdate,
        b1_0.title,
        b1_0.writer_email,
        m1_0.email,
        count(r1_0.rno)
    from
        board b1_0
    left join
        member m1_0
            on b1_0.writer_email=m1_0.email
    left join
        reply r1_0
            on r1_0.board_bno=b1_0.bno
    group by
        b1_0.bno
2024-10-15T16:37:01.477+09:00  INFO 10220 --- [memberboard] [    Test worker] o.z.m.r.s.SearchBoardRepositoryImpl      : [[Board(bno=2, title=제목 변경합니다.2, content=내용 변경합니다.2), user2@aaa.com, 4], [Board(bno=3, title=Title...3, content=Content....3), user3@aaa.com, 5], [Board(bno=4, title=Title...4, content=Content....4), user4@aaa.com, 6], [Board(bno=5, title=Title...5, content=Content....5), user5@aaa.com, 10], [Board(bno=6, title=Title...6, content=Content....6), user6@aaa.com, 1], [Board(bno=7, title=Title...7, content=Content....7), user7@aaa.com, 12], [Board(bno=8, title=Title...8, content=Content....8), user8@aaa.com, 10], [Board(bno=9, title=Title...9, content=Content....9), user9@aaa.com, 4], [Board(bno=10, title=Title...10, content=Content....10), user10@aaa.com, 3], [Board(bno=11, title=Title...11, content=Content....11), user11@aaa.com, 8], [Board(bno=12, title=Title...12, content=Content....12), user12@aaa.com, 3], [Board(bno=13, title=Title...13, content=Content....13), user13@aaa.com, 2], [Board(bno=14, title=Title...14, content=Content....14), user14@aaa.com, 6], [Board(bno=15, title=Title...15, content=Content....15), user15@aaa.com, 5], [Board(bno=16, title=Title...16, content=Content....16), user16@aaa.com, 9], [Board(bno=17, title=Title...17, content=Content....17), user17@aaa.com, 7], [Board(bno=18, title=Title...18, content=Content....18), user18@aaa.com, 2], [Board(bno=19, title=Title...19, content=Content....19), user19@aaa.com, 2], [Board(bno=20, title=Title...20, content=Content....20), user20@aaa.com, 3], [Board(bno=21, title=Title...21, content=Content....21), user21@aaa.com, 5], [Board(bno=22, title=Title...22, content=Content....22), user22@aaa.com, 9], [Board(bno=23, title=Title...23, content=Content....23), user23@aaa.com, 7], [Board(bno=24, title=Title...24, content=Content....24), user24@aaa.com, 8], [Board(bno=25, title=Title...25, content=Content....25), user25@aaa.com, 6], [Board(bno=26, title=Title...26, content=Content....26), user26@aaa.com, 11], [Board(bno=27, title=Title...27, content=Content....27), user27@aaa.com, 6], [Board(bno=28, title=Title...28, content=Content....28), user28@aaa.com, 7], [Board(bno=29, title=Title...29, content=Content....29), user29@aaa.com, 9], [Board(bno=30, title=Title...30, content=Content....30), user30@aaa.com, 12], [Board(bno=31, title=Title...31, content=Content....31), user31@aaa.com, 8], [Board(bno=32, title=Title...32, content=Content....32), user32@aaa.com, 3], [Board(bno=33, title=Title...33, content=Content....33), user33@aaa.com, 8], [Board(bno=34, title=Title...34, content=Content....34), user34@aaa.com, 3], [Board(bno=35, title=Title...35, content=Content....35), user35@aaa.com, 4], [Board(bno=36, title=Title...36, content=Content....36), user36@aaa.com, 8], [Board(bno=37, title=Title...37, content=Content....37), user37@aaa.com, 6], [Board(bno=38, title=Title...38, content=Content....38), user38@aaa.com, 4], [Board(bno=39, title=Title...39, content=Content....39), user39@aaa.com, 9], [Board(bno=40, title=Title...40, content=Content....40), user40@aaa.com, 4], [Board(bno=41, title=Title...41, content=Content....41), user41@aaa.com, 4], [Board(bno=42, title=Title...42, content=Content....42), user42@aaa.com, 6], [Board(bno=43, title=Title...43, content=Content....43), user43@aaa.com, 4], [Board(bno=44, title=Title...44, content=Content....44), user44@aaa.com, 6], [Board(bno=45, title=Title...45, content=Content....45), user45@aaa.com, 4], [Board(bno=46, title=Title...46, content=Content....46), user46@aaa.com, 3], [Board(bno=47, title=Title...47, content=Content....47), user47@aaa.com, 4], [Board(bno=48, title=Title...48, content=Content....48), user48@aaa.com, 4], [Board(bno=49, title=Title...49, content=Content....49), user49@aaa.com, 6], [Board(bno=50, title=Title...50, content=Content....50), user50@aaa.com, 1], [Board(bno=51, title=Title...51, content=Content....51), user51@aaa.com, 11], [Board(bno=52, title=Title...52, content=Content....52), user52@aaa.com, 2], [Board(bno=53, title=Title...53, content=Content....53), user53@aaa.com, 8], [Board(bno=54, title=Title...54, content=Content....54), user54@aaa.com, 6], [Board(bno=55, title=Title...55, content=Content....55), user55@aaa.com, 5], [Board(bno=56, title=Title...56, content=Content....56), user56@aaa.com, 11], [Board(bno=57, title=Title...57, content=Content....57), user57@aaa.com, 2], [Board(bno=58, title=Title...58, content=Content....58), user58@aaa.com, 4], [Board(bno=59, title=Title...59, content=Content....59), user59@aaa.com, 2], [Board(bno=60, title=Title...60, content=Content....60), user60@aaa.com, 8], [Board(bno=61, title=Title...61, content=Content....61), user61@aaa.com, 6], [Board(bno=62, title=Title...62, content=Content....62), user62@aaa.com, 6], [Board(bno=63, title=Title...63, content=Content....63), user63@aaa.com, 7], [Board(bno=64, title=Title...64, content=Content....64), user64@aaa.com, 7], [Board(bno=65, title=Title...65, content=Content....65), user65@aaa.com, 3], [Board(bno=66, title=Title...66, content=Content....66), user66@aaa.com, 4], [Board(bno=67, title=Title...67, content=Content....67), user67@aaa.com, 9], [Board(bno=68, title=Title...68, content=Content....68), user68@aaa.com, 6], [Board(bno=69, title=Title...69, content=Content....69), user69@aaa.com, 6], [Board(bno=70, title=Title...70, content=Content....70), user70@aaa.com, 5], [Board(bno=71, title=Title...71, content=Content....71), user71@aaa.com, 5], [Board(bno=72, title=Title...72, content=Content....72), user72@aaa.com, 7], [Board(bno=73, title=Title...73, content=Content....73), user73@aaa.com, 6], [Board(bno=74, title=Title...74, content=Content....74), user74@aaa.com, 4], [Board(bno=75, title=Title...75, content=Content....75), user75@aaa.com, 6], [Board(bno=76, title=Title...76, content=Content....76), user76@aaa.com, 3], [Board(bno=77, title=Title...77, content=Content....77), user77@aaa.com, 9], [Board(bno=78, title=Title...78, content=Content....78), user78@aaa.com, 9], [Board(bno=79, title=Title...79, content=Content....79), user79@aaa.com, 8], [Board(bno=80, title=Title...80, content=Content....80), user80@aaa.com, 9], [Board(bno=81, title=Title...81, content=Content....81), user81@aaa.com, 7], [Board(bno=82, title=Title...82, content=Content....82), user82@aaa.com, 6], [Board(bno=83, title=Title...83, content=Content....83), user83@aaa.com, 5], [Board(bno=84, title=Title...84, content=Content....84), user84@aaa.com, 5], [Board(bno=85, title=Title...85, content=Content....85), user85@aaa.com, 8], [Board(bno=86, title=Title...86, content=Content....86), user86@aaa.com, 4], [Board(bno=87, title=Title...87, content=Content....87), user87@aaa.com, 2], [Board(bno=88, title=Title...88, content=Content....88), user88@aaa.com, 6], [Board(bno=89, title=Title...89, content=Content....89), user89@aaa.com, 10], [Board(bno=90, title=Title...90, content=Content....90), user90@aaa.com, 8], [Board(bno=91, title=Title...91, content=Content....91), user91@aaa.com, 4], [Board(bno=92, title=Title...92, content=Content....92), user92@aaa.com, 8], [Board(bno=93, title=Title...93, content=Content....93), user93@aaa.com, 6], [Board(bno=94, title=Title...94, content=Content....94), user94@aaa.com, 6], [Board(bno=95, title=Title...95, content=Content....95), user95@aaa.com, 7], [Board(bno=96, title=Title...96, content=Content....96), user96@aaa.com, 8], [Board(bno=97, title=Title...97, content=Content....97), user97@aaa.com, 8], [Board(bno=98, title=Title...98, content=Content....98), user98@aaa.com, 9], [Board(bno=99, title=Title...99, content=Content....99), user99@aaa.com, 7], [Board(bno=100, title=Title...100, content=Content....100), user100@aaa.com, 3], [Board(bno=101, title=Title...1, content=Content....1), user1@aaa.com, 0], [Board(bno=102, title=Title...2, content=Content....2), user2@aaa.com, 0], [Board(bno=103, title=Title...3, content=Content....3), user3@aaa.com, 0], [Board(bno=104, title=Title...4, content=Content....4), user4@aaa.com, 0], [Board(bno=105, title=Title...5, content=Content....5), user5@aaa.com, 0], [Board(bno=106, title=Title...6, content=Content....6), user6@aaa.com, 0], [Board(bno=107, title=Title...7, content=Content....7), user7@aaa.com, 0], [Board(bno=108, title=Title...8, content=Content....8), user8@aaa.com, 0], [Board(bno=109, title=Title...9, content=Content....9), user9@aaa.com, 0], [Board(bno=110, title=Title...10, content=Content....10), user10@aaa.com, 0], [Board(bno=111, title=Title...11, content=Content....11), user11@aaa.com, 0], [Board(bno=112, title=Title...12, content=Content....12), user12@aaa.com, 0], [Board(bno=113, title=Title...13, content=Content....13), user13@aaa.com, 0], [Board(bno=114, title=Title...14, content=Content....14), user14@aaa.com, 0], [Board(bno=115, title=Title...15, content=Content....15), user15@aaa.com, 0], [Board(bno=116, title=Title...16, content=Content....16), user16@aaa.com, 0], [Board(bno=117, title=Title...17, content=Content....17), user17@aaa.com, 0], [Board(bno=118, title=Title...18, content=Content....18), user18@aaa.com, 0], [Board(bno=119, title=Title...19, content=Content....19), user19@aaa.com, 0], [Board(bno=120, title=Title...20, content=Content....20), user20@aaa.com, 0], [Board(bno=121, title=Title...21, content=Content....21), user21@aaa.com, 0], [Board(bno=122, title=Title...22, content=Content....22), user22@aaa.com, 0], [Board(bno=123, title=Title...23, content=Content....23), user23@aaa.com, 0], [Board(bno=124, title=Title...24, content=Content....24), user24@aaa.com, 0], [Board(bno=125, title=Title...25, content=Content....25), user25@aaa.com, 0], [Board(bno=126, title=Title...26, content=Content....26), user26@aaa.com, 0], [Board(bno=127, title=Title...27, content=Content....27), user27@aaa.com, 0], [Board(bno=128, title=Title...28, content=Content....28), user28@aaa.com, 0], [Board(bno=129, title=Title...29, content=Content....29), user29@aaa.com, 0], [Board(bno=130, title=Title...30, content=Content....30), user30@aaa.com, 0], [Board(bno=131, title=Title...31, content=Content....31), user31@aaa.com, 0], [Board(bno=132, title=Title...32, content=Content....32), user32@aaa.com, 0], [Board(bno=133, title=Title...33, content=Content....33), user33@aaa.com, 0], [Board(bno=134, title=Title...34, content=Content....34), user34@aaa.com, 0], [Board(bno=135, title=Title...35, content=Content....35), user35@aaa.com, 0], [Board(bno=136, title=Title...36, content=Content....36), user36@aaa.com, 0], [Board(bno=137, title=Title...37, content=Content....37), user37@aaa.com, 0], [Board(bno=138, title=Title...38, content=Content....38), user38@aaa.com, 0], [Board(bno=139, title=Title...39, content=Content....39), user39@aaa.com, 0], [Board(bno=140, title=Title...40, content=Content....40), user40@aaa.com, 0], [Board(bno=141, title=Title...41, content=Content....41), user41@aaa.com, 0], [Board(bno=142, title=Title...42, content=Content....42), user42@aaa.com, 0], [Board(bno=143, title=Title...43, content=Content....43), user43@aaa.com, 0], [Board(bno=144, title=Title...44, content=Content....44), user44@aaa.com, 0], [Board(bno=145, title=Title...45, content=Content....45), user45@aaa.com, 0], [Board(bno=146, title=Title...46, content=Content....46), user46@aaa.com, 0], [Board(bno=147, title=Title...47, content=Content....47), user47@aaa.com, 0], [Board(bno=148, title=Title...48, content=Content....48), user48@aaa.com, 0], [Board(bno=149, title=Title...49, content=Content....49), user49@aaa.com, 0], [Board(bno=150, title=Title...50, content=Content....50), user50@aaa.com, 0], [Board(bno=151, title=Title...51, content=Content....51), user51@aaa.com, 0], [Board(bno=152, title=Title...52, content=Content....52), user52@aaa.com, 0], [Board(bno=153, title=Title...53, content=Content....53), user53@aaa.com, 0], [Board(bno=154, title=Title...54, content=Content....54), user54@aaa.com, 0], [Board(bno=155, title=Title...55, content=Content....55), user55@aaa.com, 0], [Board(bno=156, title=Title...56, content=Content....56), user56@aaa.com, 0], [Board(bno=157, title=Title...57, content=Content....57), user57@aaa.com, 0], [Board(bno=158, title=Title...58, content=Content....58), user58@aaa.com, 0], [Board(bno=159, title=Title...59, content=Content....59), user59@aaa.com, 0], [Board(bno=160, title=Title...60, content=Content....60), user60@aaa.com, 0], [Board(bno=161, title=Title...61, content=Content....61), user61@aaa.com, 0], [Board(bno=162, title=Title...62, content=Content....62), user62@aaa.com, 0], [Board(bno=163, title=Title...63, content=Content....63), user63@aaa.com, 0], [Board(bno=164, title=Title...64, content=Content....64), user64@aaa.com, 0], [Board(bno=165, title=Title...65, content=Content....65), user65@aaa.com, 0], [Board(bno=166, title=Title...66, content=Content....66), user66@aaa.com, 0], [Board(bno=167, title=Title...67, content=Content....67), user67@aaa.com, 0], [Board(bno=168, title=Title...68, content=Content....68), user68@aaa.com, 0], [Board(bno=169, title=Title...69, content=Content....69), user69@aaa.com, 0], [Board(bno=170, title=Title...70, content=Content....70), user70@aaa.com, 0], [Board(bno=171, title=Title...71, content=Content....71), user71@aaa.com, 0], [Board(bno=172, title=Title...72, content=Content....72), user72@aaa.com, 0], [Board(bno=173, title=Title...73, content=Content....73), user73@aaa.com, 0], [Board(bno=174, title=Title...74, content=Content....74), user74@aaa.com, 0], [Board(bno=175, title=Title...75, content=Content....75), user75@aaa.com, 0], [Board(bno=176, title=Title...76, content=Content....76), user76@aaa.com, 0], [Board(bno=177, title=Title...77, content=Content....77), user77@aaa.com, 0], [Board(bno=178, title=Title...78, content=Content....78), user78@aaa.com, 0], [Board(bno=179, title=Title...79, content=Content....79), user79@aaa.com, 0], [Board(bno=180, title=Title...80, content=Content....80), user80@aaa.com, 0], [Board(bno=181, title=Title...81, content=Content....81), user81@aaa.com, 0], [Board(bno=182, title=Title...82, content=Content....82), user82@aaa.com, 0], [Board(bno=183, title=Title...83, content=Content....83), user83@aaa.com, 0], [Board(bno=184, title=Title...84, content=Content....84), user84@aaa.com, 0], [Board(bno=185, title=Title...85, content=Content....85), user85@aaa.com, 0], [Board(bno=186, title=Title...86, content=Content....86), user86@aaa.com, 0], [Board(bno=187, title=Title...87, content=Content....87), user87@aaa.com, 0], [Board(bno=188, title=Title...88, content=Content....88), user88@aaa.com, 0], [Board(bno=189, title=Title...89, content=Content....89), user89@aaa.com, 0], [Board(bno=190, title=Title...90, content=Content....90), user90@aaa.com, 0], [Board(bno=191, title=Title...91, content=Content....91), user91@aaa.com, 0], [Board(bno=192, title=Title...92, content=Content....92), user92@aaa.com, 0], [Board(bno=193, title=Title...93, content=Content....93), user93@aaa.com, 0], [Board(bno=194, title=Title...94, content=Content....94), user94@aaa.com, 0], [Board(bno=195, title=Title...95, content=Content....95), user95@aaa.com, 0], [Board(bno=196, title=Title...96, content=Content....96), user96@aaa.com, 0], [Board(bno=197, title=Title...97, content=Content....97), user97@aaa.com, 0], [Board(bno=198, title=Title...98, content=Content....98), user98@aaa.com, 0], [Board(bno=199, title=Title...99, content=Content....99), user99@aaa.com, 0], [Board(bno=200, title=Title...100, content=Content....100), user100@aaa.com, 0], [Board(bno=201, title=Test., content=Test...), user55@aaa.com, 0]]

         */
    }

    @Test
    public void testSearchPage() {

        Pageable pageable =
                PageRequest.of(0,10,
                        Sort.by("bno").descending()
                                .and(Sort.by("title").ascending()));

        Page<Object[]> result = boardRepository.searchPage("t", "1", pageable);

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
        m1_0.email,
        m1_0.moddate,
        m1_0.name,
        m1_0.password,
        m1_0.regdate,
        count(r1_0.rno)
    from
        board b1_0
    left join
        member m1_0
            on b1_0.writer_email=m1_0.email
    left join
        reply r1_0
            on r1_0.board_bno=b1_0.bno
    where
        b1_0.bno>?
        and b1_0.title like ? escape '!'
    group by
        b1_0.bno
2024-10-15T16:48:20.236+09:00  INFO 4788 --- [memberboard] [    Test worker] o.z.m.r.s.SearchBoardRepositoryImpl      : [[Board(bno=10, title=Title...10, content=Content....10), Member(email=user10@aaa.com, password=1111, name=USER10), 3], [Board(bno=11, title=Title...11, content=Content....11), Member(email=user11@aaa.com, password=1111, name=USER11), 8], [Board(bno=12, title=Title...12, content=Content....12), Member(email=user12@aaa.com, password=1111, name=USER12), 3], [Board(bno=13, title=Title...13, content=Content....13), Member(email=user13@aaa.com, password=1111, name=USER13), 2], [Board(bno=14, title=Title...14, content=Content....14), Member(email=user14@aaa.com, password=1111, name=USER14), 6], [Board(bno=15, title=Title...15, content=Content....15), Member(email=user15@aaa.com, password=1111, name=USER15), 5], [Board(bno=16, title=Title...16, content=Content....16), Member(email=user16@aaa.com, password=1111, name=USER16), 9], [Board(bno=17, title=Title...17, content=Content....17), Member(email=user17@aaa.com, password=1111, name=USER17), 7], [Board(bno=18, title=Title...18, content=Content....18), Member(email=user18@aaa.com, password=1111, name=USER18), 2], [Board(bno=19, title=Title...19, content=Content....19), Member(email=user19@aaa.com, password=1111, name=USER19), 2], [Board(bno=21, title=Title...21, content=Content....21), Member(email=user21@aaa.com, password=1111, name=USER21), 5], [Board(bno=31, title=Title...31, content=Content....31), Member(email=user31@aaa.com, password=1111, name=USER31), 8], [Board(bno=41, title=Title...41, content=Content....41), Member(email=user41@aaa.com, password=1111, name=USER41), 4], [Board(bno=51, title=Title...51, content=Content....51), Member(email=user51@aaa.com, password=1111, name=USER51), 11], [Board(bno=61, title=Title...61, content=Content....61), Member(email=user61@aaa.com, password=1111, name=USER61), 6], [Board(bno=71, title=Title...71, content=Content....71), Member(email=user71@aaa.com, password=1111, name=USER71), 5], [Board(bno=81, title=Title...81, content=Content....81), Member(email=user81@aaa.com, password=1111, name=USER81), 7], [Board(bno=91, title=Title...91, content=Content....91), Member(email=user91@aaa.com, password=1111, name=USER91), 4], [Board(bno=100, title=Title...100, content=Content....100), Member(email=user100@aaa.com, password=1111, name=USER100), 3], [Board(bno=101, title=Title...1, content=Content....1), Member(email=user1@aaa.com, password=1111, name=USER1), 0], [Board(bno=110, title=Title...10, content=Content....10), Member(email=user10@aaa.com, password=1111, name=USER10), 0], [Board(bno=111, title=Title...11, content=Content....11), Member(email=user11@aaa.com, password=1111, name=USER11), 0], [Board(bno=112, title=Title...12, content=Content....12), Member(email=user12@aaa.com, password=1111, name=USER12), 0], [Board(bno=113, title=Title...13, content=Content....13), Member(email=user13@aaa.com, password=1111, name=USER13), 0], [Board(bno=114, title=Title...14, content=Content....14), Member(email=user14@aaa.com, password=1111, name=USER14), 0], [Board(bno=115, title=Title...15, content=Content....15), Member(email=user15@aaa.com, password=1111, name=USER15), 0], [Board(bno=116, title=Title...16, content=Content....16), Member(email=user16@aaa.com, password=1111, name=USER16), 0], [Board(bno=117, title=Title...17, content=Content....17), Member(email=user17@aaa.com, password=1111, name=USER17), 0], [Board(bno=118, title=Title...18, content=Content....18), Member(email=user18@aaa.com, password=1111, name=USER18), 0], [Board(bno=119, title=Title...19, content=Content....19), Member(email=user19@aaa.com, password=1111, name=USER19), 0], [Board(bno=121, title=Title...21, content=Content....21), Member(email=user21@aaa.com, password=1111, name=USER21), 0], [Board(bno=131, title=Title...31, content=Content....31), Member(email=user31@aaa.com, password=1111, name=USER31), 0], [Board(bno=141, title=Title...41, content=Content....41), Member(email=user41@aaa.com, password=1111, name=USER41), 0], [Board(bno=151, title=Title...51, content=Content....51), Member(email=user51@aaa.com, password=1111, name=USER51), 0], [Board(bno=161, title=Title...61, content=Content....61), Member(email=user61@aaa.com, password=1111, name=USER61), 0], [Board(bno=171, title=Title...71, content=Content....71), Member(email=user71@aaa.com, password=1111, name=USER71), 0], [Board(bno=181, title=Title...81, content=Content....81), Member(email=user81@aaa.com, password=1111, name=USER81), 0], [Board(bno=191, title=Title...91, content=Content....91), Member(email=user91@aaa.com, password=1111, name=USER91), 0], [Board(bno=200, title=Title...100, content=Content....100), Member(email=user100@aaa.com, password=1111, name=USER100), 0]]
     */

}