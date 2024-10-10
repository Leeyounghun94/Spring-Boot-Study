package org.zerock.controller.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import org.zerock.controller.entity.Memo;

import java.util.List;

public interface MemoRepository extends JpaRepository<Memo, Long> {
    // JpaRepository를 상속 받으면 모든 기능을 사용한다. 그리고 <클래스객체, PK의타입> 해줘야 한다.
    // JPA를 활용하여 CRUD, PAGING, SORT 까지 알아서 처리해주는 객체

    // JpaRepository의 내장된 메서드는 save, findById(키타입), getOne(키타입), deleteById(키타입), delete(객체)
    // save메서드는 없으면 생성, 있으면 수정한다.
    // findById, getOne는 select
    // deleteById, delete는 delete


    // Query Method : 메서드명을 이용하여 SQL문이 생성됨
    List<Memo> findByMnoBetweenOrderByMnoDesc(Long from, Long to);
    // form ~ to 까지 mno값을 이용하여 찾아오고 내림차순 정렬 -> 결과 : List객체

    Page<Memo> findByMnoBetween(Long from, Long to, Pageable pageable);
    // form ~ to 까지 mno값을 이용하여 찾아오고 결과를 페이징 처리화와 정렬 기법을 적용한다.


    //  @Query는 sql문을 자바용으로 사용한다.(Entity 객체를 사용한다)
    @Query("select m from Memo m order by m.mno desc")
    List<Memo> getListDesc();
    // Memo 클래스에 있는 테이블을 mno기준으로 내림차순 정렬하여 Memo객체를 리스트에 연결하여 리턴한다.


    //  @Query 파라미터 바인딩(입력 값을 받아서 처리한다.)
    @Transactional  // 트랜젝션 처리 동시에 여러 값이 처리 될 때
    @Modifying      // 수정 작업 시 트렌젝션 처리
    @Query("update Memo m set m.memoText = :memoText where m.mno = :mno")
    int updateMemoText(@Param("mno") Long mno, @Param("memoText") String memoText);
    // test코드 에서 int result = memoRepository.updateMemoText(100L, "헬로"); -> 값을 전달 받음


    // 객체 전달 받는 용
    @Transactional
    @Modifying
    @Query("update Memo m set m.memoText = :#{#param.memoText} where m.mno = :#{#param.mno}")
    int updateMemoBean(@Param("param") Memo memo);

}
