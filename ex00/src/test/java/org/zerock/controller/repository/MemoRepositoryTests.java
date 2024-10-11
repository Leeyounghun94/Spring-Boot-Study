package org.zerock.controller.repository;


import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;
import org.zerock.controller.entity.Memo;

import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

@Log4j2
@SpringBootTest // 부트용 테스트 코드 실행 기반
public class MemoRepositoryTests {

    @Autowired  // 객체 자동 주입
    MemoRepository memoRepository;  // = MemoRepository memoRepository = New MemoRepository();

    @Test   // 메서드 기반 테스트
    public void testClass() {
        // 객체 사용 여부를 체크

        log.info("testClass 메서드 실행 . . . ." + memoRepository.getClass().getName());
        // testClass 메서드 실행 . . . .jdk.proxy3.$Proxy119 -> 인텔리제이가 만든 구현 클래스($Proxy119)
    }



    @Test
    public void testInsertDummies(){
        // 메모 테이블의 더미데이터 100개 입력하기 테스트 : save()
        IntStream.rangeClosed(1,100).forEach(
                i -> {
                    Memo memo = Memo.builder()
                            .memoText("sampleMemo ...." + i)
                            .writer("kkk....." + i)
                            .build();   // setter 끝~

                    memoRepository.save(memo); // insert into용 코드
                }
        );

    }

    @Test
    public void testSelect() {
        // 검색하여 값을 가져온다(select * from )

        Long mno = 100L;

        Optional<Memo> result = memoRepository.findById(mno);
        //  Optional<객체> : 객체가 있으면 가져온다.
        // findById(값) : select * from tbl_memo where mno = 값;

        log.info("=================================================");
        if(result.isPresent()){ //객체가 있는지 확인용

            Memo memo = result.get();   // 찾아온 값을 가져와서 memo객체에 넣는다

            log.info("100번값 가져오기 결과 : " + memo);    //toString 되어 있다.
            //  100번값 가져오기 결과 : Memo(mno=100, memoText=샘플메모 ....100, writer=작성자.....100)
        }
    }

    //@Transactional  // 동시에 다중 쿼리용( no Session 필수)
    @Test
    public void testSelect2() {
        //getOne() 메서드도 찾아오는 값 방식이 조금 다르다.
        Long mno = 100L;

        Memo memo = memoRepository.getOne(mno);
        log.info("================================================");
        log.info("100번 객체를 가져온다 (getOne()메서드 활용)" + memo);
        //  100번 객체를 가져온다 (getOne()메서드 활용)Memo(mno=100, memoText=샘플메모 ....100, writer=작성자.....100)

        // 쿼리 짤 때 @Transactional 이 필요한데 오류 나올때
        // no Session 나오면 @Transactional 꼭 써줘야 한다.
    }


    @Test
    public void testUpdate() {
        // 1개를 가져와서 수정하는 쿼리. -> .save() (없으면 insert, 있으면 Update)

        Memo memo = Memo.builder()
                .mno(100L)
                .memoText("수정한 메모")
                .writer("김수정")
                .build();
                // 여기까지 객체 생성 완료
        memoRepository.save(memo);  //update용 코드

        /* Hibernate:  select 선행 작업으로 기존 데이터 존재 파악 후 update 진행 함.
                select
                    m1_0.mno,
                    m1_0.memo_text,
                    m1_0.writer
                from
                    tbl_memo m1_0
                where
                    m1_0.mno=?
            Hibernate:
                update
                    tbl_memo
                set
                    memo_text=?,
                    writer=?
                where
                    mno=?
            */

        // 만약 삭제 메서드를 이용하여 해당 값을 삭제하고 나서 UPDATE 실행하게 되면
        // .save 가 select를 하고 해당 값이 없는걸 파악하고 insert를 한다.
    }


    @Test
    public void testDelete() {
        // mno 가지고 객체를 삭제 한다.

        memoRepository.deleteById(100L);
        /* Hibernate:
                select
                    m1_0.mno,
                    m1_0.memo_text,
                    m1_0.writer
                from
                    tbl_memo m1_0
                where
                    m1_0.mno=?
            Hibernate:
                delete
                from
                    tbl_memo
                where
                    mno=?
         */

    }

    @Test
    public void testPageDefault() {

        Sort sort1 = Sort.by("mno").descending();   // 정렬(오름차순)

        Pageable pageable = PageRequest.of(0, 10, sort1);
        // Pageable 인터페이스의 PageRequest 구현 클래스의 of 내장 메서드르르 사용한다.

        Page<Memo> result = memoRepository.findAll(pageable);
        // Page 인터페이스는 나누어진 블럭화 객체

        // 페이징 처리는 이렇게 3개만 기억하도록 하자!

        log.info("0페이지, 10개 값 출력" + result);
        //  0페이지, 10개 값 출력Page 1 of 30 containing org.zerock.controller.entity.Memo instances

        log.info("==========================================");

        System.out.println(" 총 페이지 수 : " + result.getTotalPages());
        System.out.println(" 전체 개수 : " + result.getTotalElements());
        System.out.println(" 현재 페이지 번호 : " + result.getNumber());
        System.out.println(" 페이지당 데이터 개수 : " + result.getSize());
        System.out.println(" 다음 페이지 여부 : " + result.hasNext());
        System.out.println(" 시작 페이지 여부 : " + result.isFirst());
        System.out.println(" 마지막 페이지 인지 : " + result.isLast());
        /*  총 페이지 수 : 30
             전체 개수 : 300
             현재 페이지 번호 : 0
             페이지당 데이터 개수 : 10
             다음 페이지 여부 : true
             시작 페이지 여부 : true
             마지막 페이지 인지 : false

         */

        System.out.println("==============================현재 결과물============================== " );
        for (Memo memo : result.getContent()){  // result로 내용 가져온다
            System.out.println(memo);

        }
    }

    //  List<Memo> findByMnoBetweenOrderByMnoDesc(Long from, Long to);
    @Test
    public void testQueryMethods() {

        List<Memo> list = memoRepository.findByMnoBetweenOrderByMnoDesc(40L, 50L);

        for (Memo memo : list) {
            System.out.println("결과물 : " + memo);
        }
    }// 많이 안쓰이지만 그래도 알고는 가자!


    @Test
    public void testQueryMethodWithPagingandSort() {

        Pageable pageable = PageRequest.of(0, 10, Sort.by("mno").descending());

        Page<Memo> result = memoRepository.findByMnoBetween(10L, 50L, pageable);

        result.get().forEach(memo -> {
            System.out.println(memo);
        });

    }
}
