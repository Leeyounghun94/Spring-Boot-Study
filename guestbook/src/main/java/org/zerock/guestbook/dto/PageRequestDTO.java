package org.zerock.guestbook.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@Builder
@AllArgsConstructor
@Data
// @NoArgsConstructor -> 세트 이지만 아래에 생성자가 있기 때문에 빨간줄 나온다.
public class PageRequestDTO {

    // LIST 요청 시 페이징 처리 사용하는 데이터를 재사용하기 위한 처리
    // 페이지 번호, 목록의 개수, 검색 조건 등. . .


    private int page ;

    private int size ;

    public  PageRequestDTO() {// 생성자

        this.page = 1;  // 기본 페이지 번호
        this.size = 10; // 기본 게시물 수

    }

    public Pageable getPageable(Sort sort) {

        return PageRequest.of(page -1, size, sort);
        //  Pageable pageable = PageRequest.of(0, 10, Sort.by("gno").descending()); -> 이걸 메서드로 만듦
    }

}
