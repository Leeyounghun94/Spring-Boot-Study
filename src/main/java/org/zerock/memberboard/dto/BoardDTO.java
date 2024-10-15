package org.zerock.memberboard.dto;

import lombok.*;

import java.time.LocalDateTime;

@Data
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BoardDTO {

    private Long bno ;

    private String title, content ;

    private String writerEmail ; // 작성자의 이메일

    private String writerName ; // 작성자의 이름

    private LocalDateTime regDate, modDate ;

    private int replyCount ;    // 해당 게시글의 댓글 수


    // 엔티티와다른 점은 멤버를 참조 하고 있는 대신에 화면에서는 작성자의 이메일, 이름으로 처리하고 있는 점 !
}
