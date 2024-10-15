package org.zerock.memberboard.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@MappedSuperclass   // 직접적으로 테이블을 생성하지 않는 Entity
@EntityListeners(value = {AuditingEntityListener.class})    // 등록, 수정을 감시한다.
@Getter
abstract class BaseEntity {   // 모든 Entity 부모로 날짜 처리용으로 담당한다.


    @CreatedDate    // 생성시간
    @Column(name = "regdate", updatable = false)    // 한번 등록하면 수정할수 없게 한다.
    private LocalDateTime regDate ; // 등록일 담당

    @LastModifiedDate
    @Column(name = "moddate")
    private LocalDateTime modDate ; // 수정일 담당
}
