package org.zerock.memberboard.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.zerock.memberboard.entity.Member;

public interface MemberRepository extends JpaRepository<Member, String> {
}
