package org.zerock.guestbook.service;

import org.zerock.guestbook.dto.GuestbookDTO;
import org.zerock.guestbook.dto.PageRequestDTO;
import org.zerock.guestbook.dto.PageResultDTO;
import org.zerock.guestbook.entity.Guestbook;

public interface GuestbookService {

    // C R U D 추상 메서드 생성

    //등록 메서드
    Long register(GuestbookDTO dto);    // dto를 받아서 처리 한다.

    PageResultDTO<GuestbookDTO, Guestbook> getList(PageRequestDTO requestDTO);
    // PageRequestDTO 요청을 받아서 PageResultDTO 결과를 출력
    // public PageResultDTO(Page<EN> result, Function<EN, DTO> fn)

    // dto를 entity로 변환, entity를 dto로 변환 하는 코드 추가
    default Guestbook dtoToEntity(GuestbookDTO dto) {// default 설정하면 추상메서드 처리 안하게 처리.

                Guestbook entity = Guestbook.builder()
                        .gno(dto.getGno())
                        .title(dto.getTitle())
                        .content(dto.getContent())
                        .writer(dto.getWriter())
                        .build();

                return entity;
    }// dto를 entity로

    default GuestbookDTO entityToDto(Guestbook entity) {

                GuestbookDTO dto =  GuestbookDTO.builder()
                        .gno(entity.getGno())
                        .title(entity.getTitle())
                        .content(entity.getContent())
                        .writer(entity.getWriter())
                        .regDate(entity.getRegDate())   // db에 있는 날짜 정보를 가져와야 함
                        .modDate(entity.getModDate())   // db에 있는 날짜 정보를 가져와야 함
                        .build();

                return dto;
    }// entity를 dto로
}
