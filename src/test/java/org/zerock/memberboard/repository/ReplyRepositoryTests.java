package org.zerock.memberboard.repository;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.zerock.memberboard.entity.Board;
import org.zerock.memberboard.entity.Reply;

import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

@SpringBootTest
public class ReplyRepositoryTests {


    @Autowired
    private ReplyRepository replyRepository;



    @Test
    public void insertReply() {//    300개 댓글을 1~100사의 번호로 추가. DB에는 1~100번 게시물의 N개의 댓글 추가

        IntStream.rangeClosed(1, 300).forEach(i -> {
            //1부터 100까지의 임의의 번호
            long bno  = (long)(Math.random() * 100) + 1;

            Board board = Board.builder().bno(bno).build();

            Reply reply = Reply.builder()
                    .text("Reply......." +i)
                    .board(board)
                    .replyer("guest")
                    .build();

            replyRepository.save(reply);

        });

    }

    @Test
    public void readReply1() {

        Optional<Reply> result = replyRepository.findById(1L);

        Reply reply = result.get();

        System.out.println(reply);
        System.out.println(reply.getBoard());

    }
    /*
            Hibernate:
            select
                r1_0.rno,
                b1_0.bno,
                b1_0.content,
                b1_0.moddate,
                b1_0.regdate,
                b1_0.title,
                w1_0.email,
                w1_0.moddate,
                w1_0.name,
                w1_0.password,
                w1_0.regdate,
                r1_0.moddate,
                r1_0.regdate,
                r1_0.replyer,
                r1_0.text
            from
                reply r1_0
            left join
                board b1_0
                    on b1_0.bno=r1_0.board_bno
            left join
                member w1_0
                    on w1_0.email=b1_0.writer_email
            where
                r1_0.rno=?
        Reply(rno=1, text=Reply.......1, replyer=guest)
        Board(bno=52, title=Title...52, content=Content....52)

        댓글 테이블, 보드 테이블, 멤버 테이블 까지 모두 조인으로 처리된다. 즉, 댓글 가져올때 매번 보드와 멤버를 조인해서 가져올 필요가 많지는 않으므로
        효율적이지 않다.
     */

    @Test
    public void testListByBoard() {// 보드 객체 파라미터 받고 모든 댓글을 순번대로 가져오기


        List<Reply> replyList= replyRepository.getRepliesByBoardOrderByRno(Board.builder()
                        .bno(97L)
                .build());

        replyList.forEach(reply -> {
            System.out.println(reply);
        });

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
Reply(rno=34, text=Reply.......34, replyer=guest)
Reply(rno=57, text=Reply.......57, replyer=guest)
Reply(rno=120, text=Reply.......120, replyer=guest)
Reply(rno=134, text=Reply.......134, replyer=guest)
Reply(rno=159, text=Reply.......159, replyer=guest)
Reply(rno=520, text=Reply.......220, replyer=guest)
Reply(rno=575, text=Reply.......275, replyer=guest)
Reply(rno=588, text=Reply.......288, replyer=guest)

         */
    }
}
