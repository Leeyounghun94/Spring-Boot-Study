package org.zerock.memberboard.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.zerock.memberboard.entity.M_member;

import java.util.stream.IntStream;

@SpringBootTest
public class M_memberRepositoryTests {

    @Autowired
    private M_memberRepository m_memberRepository;

    @Test
    public void memberInsert() {

        IntStream.rangeClosed(1, 100).forEach( i -> {

            M_member member = M_member.builder()
                    .email("r" + i + "@kkk.com")
                    .pw("1234")
                    .nickName("관찰자" + i)
                    .build();

            m_memberRepository.save(member);

        });

        /*
        Hibernate:
    insert
    into
        m_member
        (email, moddate, nick_name, pw, regdate)
    values
        (?, ?, ?, ?, ?)
Hibernate:
    insert
    into
        m_member
        (email, moddate, nick_name, pw, regdate)
    values
        (?, ?, ?, ?, ?)


... 생략 ;;
         */
    }
}
