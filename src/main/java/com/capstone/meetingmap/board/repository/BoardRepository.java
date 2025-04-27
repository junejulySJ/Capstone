package com.capstone.meetingmap.board.repository;

import com.capstone.meetingmap.board.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BoardRepository extends JpaRepository<Board, Integer> {
    List<Board> findByCategory_CategoryNoOrderByBoardNoDesc(Integer categoryNo);
    List<Board> findAllByOrderByBoardNoDesc();
}
