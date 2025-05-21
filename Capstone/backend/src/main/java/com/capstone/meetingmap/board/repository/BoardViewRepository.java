package com.capstone.meetingmap.board.repository;

import com.capstone.meetingmap.board.entity.BoardView;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BoardViewRepository extends JpaRepository<BoardView, Integer> {
    @Query("SELECT b FROM BoardView b " +
            "WHERE (:categoryNo IS NULL OR b.categoryNo = :categoryNo) " +
            "AND (LOWER(b.boardTitle) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(b.boardDescription) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    Page<BoardView> searchByCategoryAndKeyword(@Param("categoryNo") Integer categoryNo,
                                               @Param("keyword") String keyword,
                                               Pageable pageable);

    Page<BoardView> findAllByBoardTitleContainingIgnoreCaseOrBoardDescriptionContainingIgnoreCase(String titleKeyword, String descriptionKeyword, Pageable pageable);
    Page<BoardView> findAllByCategoryNoOrderByBoardNoDesc(Integer categoryNo, Pageable pageable);
    Page<BoardView> findAllByOrderByBoardNoDesc(Pageable pageable);
}
