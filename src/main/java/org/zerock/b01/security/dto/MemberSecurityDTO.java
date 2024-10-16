package org.zerock.b01.security.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

@Setter
@Getter
@ToString
public class MemberSecurityDTO extends User {
    // User 를 상속 받는다. USER에는 UserDetail 인터페이스 구현한 클래스를 간단하게 생성하는 방법 제공

    // 딱히 다른 DTO랑은 차이가 없지만 시큐리티를 이용한 멤버DTO는 해당 API 맞게 작성되어야 한다.
    // 시큐리티 에서는 UserDetail라는 타입을 이용함

    //필드
    private String mid, mpw, email ;

    private boolean del, social;

    //생성자
    public MemberSecurityDTO(String username, String password, String email, boolean del, boolean social, Collection<? extends GrantedAuthority> authorities) {
        //                          이름              패스워드        이메일         탈퇴              소셜                      권한

        super(username, password, authorities);

        this.mid = username ;
        this.mpw = password ;
        this.email = email ;
        this.del = del ;
        this.social = social ;

    }




}
