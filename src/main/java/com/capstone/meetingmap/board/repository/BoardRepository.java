package com.capstone.meetingmap.board.repository;

import com.capstone.meetingmap.board.entity.Board;

import java.util.List;
import java.util.Optional;

public interface BoardRepository {
    Board save(Board board);
    Optional<Board> findByBoardNo(Integer boardNo);
    List<Board> findByCategoryNoOrderByBoardNoDesc(Integer categoryNo);
    List<Board> findAllOrderByBoardNoDesc();
}
