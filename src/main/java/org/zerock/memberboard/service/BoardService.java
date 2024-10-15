package org.zerock.memberboard.service;

import org.zerock.memberboard.dto.BoardDTO;
import org.zerock.memberboard.dto.PageRequestDTO;
import org.zerock.memberboard.dto.PageResultDTO;
import org.zerock.memberboard.entity.Board;
import org.zerock.memberboard.entity.Member;

public interface BoardService {

    Long register(BoardDTO dto);

    PageResultDTO<BoardDTO, Object[]> getList(PageRequestDTO pageRequestDTO);   // 목록 처리

    BoardDTO get(Long bno); // 게시물 조회 처리

    void removeWithReplies(Long bno);// 삭제 기능

    void modify(BoardDTO boardDTO); //  게시물 수정 처리

    default Board dtoToEntity(BoardDTO dto){
        Member member = Member.builder().email(dto.getWriterEmail()).build();

        Board board = Board.builder()
                .bno(dto.getBno())
                .title(dto.getTitle())
                .content(dto.getContent())
                .writer(member)
                .build();
        return board;
    }


    default BoardDTO entityToDTO(Board board, Member member, Long replyCount) {

        BoardDTO boardDTO = BoardDTO.builder()
                .bno(board.getBno())
                .title(board.getTitle())
                .content(board.getContent())
                .regDate(board.getRegDate())
                .modDate(board.getModDate())
                .writerEmail(member.getEmail())
                .writerName(member.getName())
                .replyCount(replyCount.intValue()) //int로 처리하도록
                .build();

        return boardDTO;

    }

}
