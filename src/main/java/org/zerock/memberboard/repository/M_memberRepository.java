package org.zerock.memberboard.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.zerock.memberboard.entity.M_member;

public interface M_memberRepository extends JpaRepository<M_member, Long> {
}
