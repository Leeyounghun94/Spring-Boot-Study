package org.zerock.memberboard.entity;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@ToString
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "m_member")
public class M_member extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long mid ;

    private String email ;

    private String pw ;

    private String nickName ;
}
