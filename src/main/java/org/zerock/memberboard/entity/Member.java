package org.zerock.memberboard.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

@Entity // 테이블 담당한다.
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
public class Member extends BaseEntity {


    @Id // PK 지정
    private String email ;  // 이메일로 로그인

    private String password ;   // 비밀번호

    private String name ;   // 사용자 명


    // 나머지는 알아서 넣어라


    /*
    Hibernate:
    create table member (
        email varchar(255) not null,
        moddate datetime(6),
        regdate datetime(6),
        name varchar(255),
        password varchar(255),
        primary key (email)
    ) engine=InnoDB

     */
}
