package com.capstone.meetingmap.board.repository;

import com.capstone.meetingmap.board.entity.BoardView;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardViewRepository extends JpaRepository<BoardView, Integer> {
    Page<BoardView> findAllByCategoryNoOrderByBoardNoDesc(Integer categoryNo, Pageable pageable);
    Page<BoardView> findAllByOrderByBoardNoDesc(Pageable pageable);
}
