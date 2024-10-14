package org.zerock.guestbook.dto;

import lombok.Data;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Data
public class PageResultDTO<DTO, EN> {

    // LIST 응답 시 페이징
    // 결과 처리 결과를 Page<Entity> 타입으로 변환
    // 화면에 출력 필요한 페이지 정보를 구성 : 시작 번호, 마지막 번호, 이전, 다음


    private List<DTO> dtoList ; //제네릭으로 받은 dto를 리스트 처리 한다.

    // 총 페이지 번호
    private int totalPage ;

    // 현재 페이지 번호
    private int page;

    // 목록 사이즈
    private int size ;

    // 시작 페이지번호, 끝 페이지 번호
    private int start, end ;

    // 이전, 다음
    private boolean prev, next ;

    // 페이지 번호 목록
    private  List<Integer> pageList ;   // 밑에 있는 가로 리스트 1, 2, 3, 4, 5 . . .11, 12, 13, ...

    public PageResultDTO(Page<EN> result, Function<EN, DTO> fn) {
        // Function<EN, DTO> fn -> 엔티티 객체를 dto로 변환해 주는 기능
        // 나중에 어떤 종류의 Page<E> 타입이 생성되더라도 처리가 가능하다.
        dtoList = result.stream().map(fn).collect(Collectors.toList());

        // 추가
        totalPage = result.getTotalPages(); // 68페이지 참고하기 (총 페이지)

        makePageList(result.getPageable()); // 아래에 메서드 생성
    }

    private void makePageList(Pageable pageable) {

        this.page = pageable.getPageNumber() + 1;
        this.size = pageable.getPageSize();

        // 계산용 페이징
        int tempEnd = (int)(Math.ceil(page/10.0)) * 10;

        start = tempEnd - 9;

        prev = start > 1;   // 시작 페이지가 1보다 크면 있음

        end = totalPage > tempEnd ? tempEnd : totalPage;
        //      조건              ?   참       : 거짓

        next = totalPage > tempEnd; // next 버튼 (다음 페이지용 코드)

        pageList = IntStream.rangeClosed(start, end).boxed().collect(Collectors.toList());
        // 페이징용 가로 리스트 결과       prev(t/f) | 1 2 3 4 5 6 7 . . . | next(t/f)

    }//이해되면 이해되는대로 하고 안되면 걍 복사해서 붙여써~

}
