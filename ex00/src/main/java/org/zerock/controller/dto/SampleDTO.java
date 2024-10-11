package org.zerock.controller.dto;


import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder(toBuilder = true)  // 빌더를 toString처럼 사용할 수 있다.
public class SampleDTO {

    private Long sno;

    private String first;

    private String last;

    private LocalDateTime regTime;

}

