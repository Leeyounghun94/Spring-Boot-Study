package org.zerock.controller.entity;


import jakarta.persistence.*;
import lombok.*;

@Entity // JPA가 엔티티를 담당한다.
@Table(name = "tbl_memo")  // Table을 담당한다. (name = "") -> 테이블명
@ToString   // 객체가 문자열 처리
@Getter // 게터 메서드
@Builder    /* 빌더 패턴(Setter 대신 활용하며 . 으로 값을 추가할 수 있다.
               Memo.builder().memoText("값").memoWriter("값").build(); */
@AllArgsConstructor // 모든 필드 값으로 생성자를 만든다.
@NoArgsConstructor  // 빈 메서드로 생성자 만든다.  Memo memo = New Memo();         //Builder 세트
public class Memo {

    //필드
    @Id // 기본키 역할을 한다.
    @GeneratedValue(strategy = GenerationType.IDENTITY) //  MariaDB용 자동 번호 생성
/*  @GeneratedValue(strategy = GenerationType.SEQUENCE) // 오라클 시퀀스 객체용
    @GeneratedValue(strategy = GenerationType.AUTO) // 알아서 자동으로 번호 생성
    @GeneratedValue(strategy = GenerationType.UUID) // 16진수화 번호 생성 */
    // 시퀀스 개념(자동으로 번호가 만들어짐)
    private Long mno ;

    @Column(length = 200, nullable = false) // 메모 글, 길이는 200자, null허용 안함
    private String memoText ;

    @Column(length = 20, nullable = false)// 작성자, 길이는 20글자, null허용 안함
    private String writer ;



    //생성자



    //메서드


    /* Java가 이렇게 결과를 만들어 준다.
    Hibernate:
    create table tbl_memo (
        mno bigint not null auto_increment,
        memo_text varchar(200) not null,
        writer varchar(20) not null,
        primary key (mno)
    ) engine=InnoDB
     */
}
