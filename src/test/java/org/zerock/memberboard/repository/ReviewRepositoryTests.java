package org.zerock.memberboard.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.zerock.memberboard.entity.M_member;
import org.zerock.memberboard.entity.Movie;
import org.zerock.memberboard.entity.Review;

import java.util.stream.IntStream;

@SpringBootTest
public class ReviewRepositoryTests {


    @Autowired
    private ReviewRepository reviewRepository ;

    @Test
    public void reviewInsert() {

        // 200개 리뷰 등록
        IntStream.rangeClosed(1, 200).forEach(i -> {

            // 영화 번호
            Long mno = (long)(Math.random()*100) + 1 ;

            // 리뷰어? 번호
            Long mid = ((long)(Math.random()*100) + 1);
            M_member member = M_member.builder()
                    .mid(mid)
                    .build();

            Review movieReview = Review.builder()
                    .member(member)
                    .movie(Movie.builder()
                            .mno(mno)
                            .build())
                    .grade((int)(Math.random()*5) +1)
                    .text("영화의 느낌적인 느낌.. " + i)
                    .build();

            reviewRepository.save(movieReview);
        });

        /*

        Hibernate:
    insert
    into
        review
        (grade, member_mid, moddate, movie_mno, regdate, text)
    values
        (?, ?, ?, ?, ?, ?)
Hibernate:
    insert
    into
        review
        (grade, member_mid, moddate, movie_mno, regdate, text)
    values
        (?, ?, ?, ?, ?, ?)
Hibernate:
    insert
    into
        review
        (grade, member_mid, moddate, movie_mno, regdate, text)
    values
        (?, ?, ?, ?, ?, ?)
Hibernate:
    insert
    into
        review
        (grade, member_mid, moddate, movie_mno, regdate, text)
    values
        (?, ?, ?, ?, ?, ?)
Hibernate:
    insert
    into
        review
        (grade, member_mid, moddate, movie_mno, regdate, text)
    values
        (?, ?, ?, ?, ?, ?)


        ... 생략 ;
         */
    }
}
