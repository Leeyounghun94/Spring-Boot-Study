package org.zerock.b01.domain;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "roleSet")
@Getter
public class Member extends BaseEntity{

    // 시큐리티의 실제 사요자의 정보 로딩은 UserDetailService를 이용해서 처리하며 DB를 이용하여 설정한다.

    @Id
    private String mid; // 회원 아이디

    private String mpw, email;  // 회원 패스워드 , 이메일

    private boolean del, social;    // 탈퇴 여부, 소셜 로그인용

    @ElementCollection(fetch = FetchType.LAZY)
    // 지연로딩 -> 각 회원이 권한을 가질수 있도록 필수 , 컬렉션 객체임을 JPA가 알게 한다. 엔티티가 아닌 값 타입, 임베이드타입에 대한 테이블 생성하고 1대 다 관계로 다룬다
    @Builder.Default
    private Set<MemberRole> roleSet = new HashSet<>();// 멤버 룰 적용

    public void changePassword(String mpw) {// 암호 변경
        this.mpw = mpw ;
    }

    public void changeEmail(String email) {//이메일 변경
        this.email = email;
    }

    public void changeDel(boolean del) {// 탈퇴
        this.del = del;
    }

    public void addRole(MemberRole memberRole) {// 룰 추가하기
        this.roleSet.add(memberRole);
    }

    public void clearRoles() {// 룰 제거
        this.roleSet.clear();
    }

    public void changeSocial(boolean social) {// 소셜 전환
        this.social = social;
    }
}
