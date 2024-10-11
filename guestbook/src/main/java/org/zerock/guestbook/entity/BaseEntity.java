package org.zerock.guestbook.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@MappedSuperclass   // entity 이지만 직접 테이블을 생성하지 않으며 부모 클래스 역할을 제공
@EntityListeners(value = {AuditingEntityListener.class})    // main메서드 동작 시 감시체제 동작!
@Getter
public class BaseEntity {
    // 모든 테이블에는 등록날짜, 수정시간이 공통으로 들어간다.
    // 감시체제를 활용하여 객체 수정 시 날짜 수정까지 제공한다.

    @CreatedDate    // 최초로 생성 시 에만 동작
    @Column(name = "regDate", updatable = false)    // db저장 시 필드명은 regDate로 업데이트는 안하게
    private LocalDateTime regDate ; // 등록일

    @LastModifiedDate   // 마지막 수정일 동작
    @Column(name = "moddate")   // db저장 시 필드명은 moddate로 생성
    private LocalDateTime modDate ; // 수정일
}
