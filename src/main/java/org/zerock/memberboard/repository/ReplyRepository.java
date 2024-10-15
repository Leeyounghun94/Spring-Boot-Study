package org.zerock.memberboard.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.zerock.memberboard.entity.Board;
import org.zerock.memberboard.entity.Reply;

import java.util.List;

public interface ReplyRepository extends JpaRepository<Reply, Long> {

    // board 삭제 시 댓글들 삭제
    @Modifying
    @Query("delete from Reply r where r.board.bno =:bno ")
    void deleteByBno(Long bno);
    // 특정 게시물 번호로 댓글을 삭제하는 기능

    List<Reply> getRepliesByBoardOrderByRno(Board board);   // 게시물로 댓글 목록 가져오기
}
