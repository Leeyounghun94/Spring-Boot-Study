package org.zerock.memberboard.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString(exclude = "writer")   //(exclude = "writer") -> 문자열 처리 제외(Member는 테이블이니까)
public class Board extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)// MariaDB용 자동 번호 생성
    private Long bno ;

    private String title ;

    private String content ;

    // 보드도 알아서 나머지 넣어라~

    @ManyToOne(fetch = FetchType.LAZY)// FK생성(자식이 부모를 선택한다)      //@ManyTOMany는 아직 쓰지는 말아라
    // (fetch = FetchType.LAZY) -> 지연 로딩을 적용한다.
    private Member writer ; // 멤버 테이블의 정보를 가져온다!

    /*
    Hibernate:
    create table board (
        bno bigint not null auto_increment,
        moddate datetime(6),
        regdate datetime(6),
        content varchar(255),
        title varchar(255),
        writer_email varchar(255),
        primary key (bno)
    ) engine=InnoDB


    Hibernate:
    alter table if exists board
       add constraint FK1iu8rhoim4thb0y12cpt01oiu
       foreign key (writer_email)
       references member (email)
     */

    public void changeTitle(String title){
        this.title = title;
    }

    public void changeContent(String content){
        this.content = content;
    }
}
