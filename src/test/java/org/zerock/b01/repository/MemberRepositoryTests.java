package org.zerock.b01.repository;

import lombok.ToString;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.zerock.b01.domain.Member;
import org.zerock.b01.domain.MemberRole;

import java.util.stream.IntStream;

@SpringBootTest
@Log4j2
public class MemberRepositoryTests {


    @Autowired
    private MemberRepository memberRepository ;

    @Autowired
    private PasswordEncoder passwordEncoder ;


    @Test
    public void insertMembers() {

        IntStream.rangeClosed(1, 100).forEach(i -> {

            Member member = Member.builder()
                    .mid("멤버" + i)
                    .mpw(passwordEncoder.encode("1234"))
                    .email("email" + i + "kkk@kkk.com")
                    .build();

            member.addRole(MemberRole.USER);

            if (i >=90) {
                member.addRole(MemberRole.ADMIN);

            }
            memberRepository.save(member);
        });

        /*
        Hibernate:
    select
        m1_0.mid,
        m1_0.del,
        m1_0.email,
        m1_0.moddate,
        m1_0.mpw,
        m1_0.regdate,
        m1_0.social
    from
        member m1_0
    where
        m1_0.mid=?
Hibernate:
    insert
    into
        member
        (del,email,moddate,mpw,regdate,social,mid)
    values
        (?,?,?,?,?,?,?)
Hibernate:
    insert
    into
        member_role_set
        (member_mid,role_set)
    values
        (?,?)
Hibernate:
    select
        m1_0.mid,
        m1_0.del,
        m1_0.email,
        m1_0.moddate,
        m1_0.mpw,
        m1_0.regdate,
        m1_0.social
    from
        member m1_0
    where
        m1_0.mid=?
Hibernate:
    insert
    into
        member
        (del,email,moddate,mpw,regdate,social,mid)
    values
        (?,?,?,?,?,?,?)

        .... 생략


         */
    }


}
