package org.zerock.b01.repository;

import jakarta.persistence.Entity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import org.zerock.b01.domain.Member;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, String> {

    @EntityGraph(attributePaths = "roleSet")
    @Query("select m from Member m where m.mid = :mid and m.social = false ")
    Optional<Member> getWithRoles(String mid);
    // 로그인 할 때 룰을 같이 로딩하는 구조, 직접 로그인 할 때 소셜 서비스를 통해 회원가입된 회원들이 같은 패스워드를 가지므로 일반 회원들만 가져오도록 소셜 속성값이 false인 사용자만 처리

    @EntityGraph(attributePaths = "roleSet")
    Optional<Member> findByEmail(String email);
    // email.을 이용하여 회원 정보를 찾을 수 있는 메서드

    @Modifying
    @Transactional
    @Query("update Member m set m.mpw =:mpw where m.mid =:mid ")
    void updatePassword(@Param("mpw") String password, @Param("mid") String mid);

    // @QUERY는 주로 Select 할 때 이용하지만 @Modifying 이랑 같이 사용하면 DML(insert,update,delete) 처리도 가능하다.
}
