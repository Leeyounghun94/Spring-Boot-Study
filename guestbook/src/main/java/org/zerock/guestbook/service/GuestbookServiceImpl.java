package org.zerock.guestbook.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.zerock.guestbook.dto.GuestbookDTO;
import org.zerock.guestbook.dto.PageRequestDTO;
import org.zerock.guestbook.dto.PageResultDTO;
import org.zerock.guestbook.entity.Guestbook;
import org.zerock.guestbook.repository.GuestbookRepository;

import java.util.function.Function;

@Service    // 서비스 계층을 알림
@Log4j2
@RequiredArgsConstructor    // 의존성 자동 주입
public class GuestbookServiceImpl implements GuestbookService{


    private  final GuestbookRepository repository;  // JPA 연결


    @Override
    public Long register(GuestbookDTO dto) {// return은 gno, 입력은 dto

        log.info("GuestbookServiceImpl.register 메서드 실행 ");
        log.info("dto : " + dto);

        Guestbook entity = dtoToEntity(dto);    // 화면에서 받은 객체를 db로 전달
        log.info("entity : " +entity);

            repository.save(entity);    // jpa로 insert 처리

        return entity.getGno(); // insert된 방명록 번호가 리턴된다.
    }

    @Override
    public PageResultDTO<GuestbookDTO, Guestbook> getList(PageRequestDTO requestDTO) {

        Pageable pageable = requestDTO.getPageable(Sort.by("gno").descending());
        /*
          public Pageable getPageable(Sort sort) {

        return PageRequest.of(page -1, size, sort);
        }
         */

        Page<Guestbook> result = repository.findAll(pageable);  // jpa를 이용하여 페이징처리 + 목록

        Function<Guestbook, GuestbookDTO> fn = (entity -> entityToDto(entity)); //Function<멤버entity, 멤버dto>
        // 함수 생성<entity, Dto> fn 이름으로 결과가 들어간다.

        return new PageResultDTO<>(result, fn);
        // public PageResultDTO(Page<EN> result, Function<EN, DTO> fn)
    }
}
