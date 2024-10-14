package org.zerock.guestbook.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Builder    // 빌더 패턴 쓰면 Setter(대체용)
@NoArgsConstructor
@AllArgsConstructor
// 빌더 3종 세트
@Data
public class GuestbookDTO {

    // Entity 에 있는 정보를 객체화 시킨다.


    private Long gno ;

    private String title, content, writer ; // 이런식으로 써도 된다

    private LocalDateTime regDate, modDate ;


}
