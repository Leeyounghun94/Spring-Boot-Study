package org.zerock.memberboard.entity;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString(exclude = "movie")// 연관 관계 항상 주의
@Embeddable
public class MovieImage {


    // 여기에는 나중에 사용할 이미지에 대한 정보를 기록한다.
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long inum ;

    private String uuid ;   // 고유 번호 생성

    private  String imgName ;

    private String path ;   // 이미지 경로

    @ManyToOne(fetch = FetchType.LAZY)
    private Movie movie ;

    /*

    Hibernate:
    create table movie (
        mno bigint not null auto_increment,
        moddate datetime(6),
        regdate datetime(6),
        title varchar(255),
        primary key (mno)
    ) engine=InnoDB
Hibernate:
    create table movie_image (
        inum bigint not null auto_increment,
        img_name varchar(255),
        path varchar(255),
        uuid varchar(255),
        movie_mno bigint,
        primary key (inum)
    ) engine=InnoDB
Hibernate:
    alter table if exists movie_image
       add constraint FKitwj3761d8j8ku189u4qrseih
       foreign key (movie_mno)
       references movie (mno)


     */
}
