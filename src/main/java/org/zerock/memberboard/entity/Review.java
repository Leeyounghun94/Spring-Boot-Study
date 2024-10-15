package org.zerock.memberboard.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@ToString(exclude = {"movie", "member"})
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class Review extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reviewNum ;

    @ManyToOne(fetch = FetchType.LAZY)
    private Movie movie;

    @ManyToOne(fetch = FetchType.LAZY)
    private M_member member ;

    private int grade ;

    private String text ;

    /*

    Hibernate:
    create table m_member (
        mid bigint not null auto_increment,
        moddate datetime(6),
        regdate datetime(6),
        email varchar(255),
        nick_name varchar(255),
        pw varchar(255),
        primary key (mid)
    ) engine=InnoDB
Hibernate:
    create table review (
        review_num bigint not null auto_increment,
        moddate datetime(6),
        regdate datetime(6),
        grade integer not null,
        text varchar(255),
        member_mid bigint,
        movie_mno bigint,
        primary key (review_num)
    ) engine=InnoDB
Hibernate:
    alter table if exists review
       add constraint FK9gnx8llky658xey9qg00djyg5
       foreign key (member_mid)
       references m_member (mid)
Hibernate:
    alter table if exists review
       add constraint FKdg4bkv5wfpxx015elj4h915gw
       foreign key (movie_mno)
       references movie (mno)

     */
}
