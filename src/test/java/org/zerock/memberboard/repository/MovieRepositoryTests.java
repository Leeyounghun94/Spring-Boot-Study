package org.zerock.memberboard.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;
import org.zerock.memberboard.entity.Movie;
import org.zerock.memberboard.entity.MovieImage;

import java.util.Arrays;
import java.util.UUID;
import java.util.stream.IntStream;

@SpringBootTest
public class MovieRepositoryTests {

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private MovieImageRepository movieImageRepository;

    @Commit
    @Transactional
    @Test
    public void movieInsert() {

        IntStream.rangeClosed(1, 100).forEach(i -> {
            Movie movie = Movie.builder()
                    .title("영화 제목.." + i)
                    .build();

            System.out.println("=====================================================");

            movieRepository.save(movie);

            int count = (int)(Math.random() * 5) + i ; // 1 2 3  4..

           for (int j =0; j < count; j++) {
               MovieImage movieImage = MovieImage.builder()
                       .uuid(UUID.randomUUID().toString())
                       .movie(movie)
                       .imgName("테스트" + j + ".jpg")
                       .build();

               movieImageRepository.save(movieImage);
           }

            System.out.println("======================================================");
        });

        /*
        Hibernate:
    insert
    into
        movie_image
        (img_name, movie_mno, path, uuid)
    values
        (?, ?, ?, ?)
======================================================
=====================================================
Hibernate:
    insert
    into
        movie
        (moddate, regdate, title)
    values
        (?, ?, ?)
Hibernate:
    insert
    into
        movie_image
        (img_name, movie_mno, path, uuid)
    values
        (?, ?, ?, ?)
Hibernate:
    insert
    into
        movie_image
        (img_name, movie_mno, path, uuid)
    values
        (?, ?, ?, ?)
Hibernate:
    insert
    into
        movie_image
        (img_name, movie_mno, path, uuid)
    values
        (?, ?, ?, ?)

... 생략 ;;
         */
    }


    @Test
    public void testList() {

        PageRequest pageRequest = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "mno"));

        Page<Object[]> result = movieRepository.getListPage(pageRequest);

        for (Object[] objects : result.getContent())  {
            System.out.println(Arrays.toString(objects));
        }
    }
    /*
    Hibernate:
    select
        m1_0.mno,
        m1_0.moddate,
        m1_0.regdate,
        m1_0.title,
        avg(coalesce(r1_0.grade, 0)),
        count(distinct r1_0.review_num)
    from
        movie m1_0
    left join
        review r1_0
            on r1_0.movie_mno=m1_0.mno
    group by
        m1_0.mno
    order by
        m1_0.mno desc
    limit
        ?
Hibernate:
    select
        count(m1_0.mno)
    from
        movie m1_0
    left join
        review r1_0
            on r1_0.movie_mno=m1_0.mno
    group by
        m1_0.mno
[Movie(mno=200, title=영화 제목..100), 4.0, 1]
[Movie(mno=199, title=영화 제목..99), 3.0, 2]
[Movie(mno=198, title=영화 제목..98), 0.0, 0]
[Movie(mno=197, title=영화 제목..97), 2.0, 1]
[Movie(mno=196, title=영화 제목..96), 2.5, 2]
[Movie(mno=195, title=영화 제목..95), 0.0, 0]
[Movie(mno=194, title=영화 제목..94), 0.0, 0]
[Movie(mno=193, title=영화 제목..93), 0.0, 0]
[Movie(mno=192, title=영화 제목..92), 5.0, 1]
[Movie(mno=191, title=영화 제목..91), 0.0, 0]


@Query("select m, mi, avg(coalesce(r.grade,0)),  count(r) from Movie m " +
            "left outer join MovieImage mi on mi.movie = m " +
            "left outer join Review  r on r.movie = m group by m ")
    Page<Object[]> getListPage(Pageable pageable);

    수정 된 후 결과 ->

    Hibernate:
    select
        m1_0.mno,
        m1_0.moddate,
        m1_0.regdate,
        m1_0.title,
        mi1_0.inum,
        mi1_0.img_name,
        mi1_0.movie_mno,
        mi1_0.path,
        mi1_0.uuid,
        avg(coalesce(r1_0.grade, 0)),
        count(r1_0.review_num)
    from
        movie m1_0
    left join
        movie_image mi1_0
            on mi1_0.movie_mno=m1_0.mno
    left join
        review r1_0
            on r1_0.movie_mno=m1_0.mno
    group by
        m1_0.mno
    order by
        m1_0.mno desc
    limit
        ?
Hibernate:
    select
        count(m1_0.mno)
    from
        movie m1_0
    left join
        movie_image mi1_0
            on mi1_0.movie_mno=m1_0.mno
    left join
        review r1_0
            on r1_0.movie_mno=m1_0.mno
    group by
        m1_0.mno
[Movie(mno=200, title=영화 제목..100), MovieImage(inum=10411, uuid=9cb25cee-641c-457e-9b83-86464f8c9190, imgName=테스트0.jpg, path=null), 4.0, 100]
[Movie(mno=199, title=영화 제목..99), MovieImage(inum=10309, uuid=0d3d1462-dc93-4b83-b740-b6bf5a31cf82, imgName=테스트0.jpg, path=null), 3.0, 204]
[Movie(mno=198, title=영화 제목..98), MovieImage(inum=10207, uuid=ce9d6cb5-090e-4858-9254-e189c82080a5, imgName=테스트0.jpg, path=null), 0.0, 0]
[Movie(mno=197, title=영화 제목..97), MovieImage(inum=10106, uuid=da4d352d-b839-4ebe-93ae-9cdafcf73ff0, imgName=테스트0.jpg, path=null), 2.0, 101]
[Movie(mno=196, title=영화 제목..96), MovieImage(inum=10009, uuid=3052bbd0-e715-483f-8525-b582ed169ad2, imgName=테스트0.jpg, path=null), 2.5, 194]
[Movie(mno=195, title=영화 제목..95), MovieImage(inum=9911, uuid=d6162b87-c521-4586-8177-cced19892e40, imgName=테스트0.jpg, path=null), 0.0, 0]
[Movie(mno=194, title=영화 제목..94), MovieImage(inum=9815, uuid=cb08ff32-0b3b-40d0-9bff-427cc05f9d4e, imgName=테스트0.jpg, path=null), 0.0, 0]
[Movie(mno=193, title=영화 제목..93), MovieImage(inum=9720, uuid=a3eccad3-64b9-4c3f-8716-3427110b7468, imgName=테스트0.jpg, path=null), 0.0, 0]
[Movie(mno=192, title=영화 제목..92), MovieImage(inum=9627, uuid=e344f792-dd81-4e56-9600-5775d9270791, imgName=테스트0.jpg, path=null), 5.0, 93]
[Movie(mno=191, title=영화 제목..91), MovieImage(inum=9536, uuid=07f85c1b-3446-44ea-a458-7cd7bdcc1f85, imgName=테스트0.jpg, path=null), 0.0, 0]
     */
}
