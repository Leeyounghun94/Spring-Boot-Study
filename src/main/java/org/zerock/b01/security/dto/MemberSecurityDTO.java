package org.zerock.b01.security.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Map;

@Setter
@Getter
@ToString
public class MemberSecurityDTO extends User implements OAuth2User {
    // User 를 상속 받는다. USER에는 UserDetail 인터페이스 구현한 클래스를 간단하게 생성하는 방법 제공

    // 딱히 다른 DTO랑은 차이가 없지만 시큐리티를 이용한 멤버DTO는 해당 API 맞게 작성되어야 한다.
    // 시큐리티 에서는 UserDetail라는 타입을 이용함

    //필드
    private String mid, mpw, email ;

    private boolean del, social;

    private Map<String, Object> props ; // 소셜 로그인 정보
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
    /*
    소셜 로그인에 사용한 이메일과 같은 이메일 회원이 있다면 소셜 로그인으로만 로그인 자체가 완료 되어야 한다. 하지만 해당 이메일을 가진 사용자가 없을 때는 새로운 회원으로
    간주하고 멤버 객체를 직접 생성하여 멤버 시큐리티 디티오 에다가 생성해서 반환 해야 한다.

    이때, 중요한 속성이 Socail인데,  자동으로 회원 데이터가 추가할때는 true 지정하고 만일 악용으로 이메일 안다고 해도 직접 로그인 할 때는 socail설정이 false 경우만 조회되서 로그인이 안된다.

     */


    @Override
    public Map<String, Object> getAttributes() {
        return this.getProps();
    }

    @Override
    public String getName() {
        return this.mid;
    }
}
