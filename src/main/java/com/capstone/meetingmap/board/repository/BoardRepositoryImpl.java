package com.capstone.meetingmap.board.repository;

import com.capstone.meetingmap.board.entity.Board;
import com.capstone.meetingmap.user.entity.User;
import jakarta.persistence.EntityManager;

import java.util.List;
import java.util.Optional;

public class BoardRepositoryImpl implements BoardRepository {
    private final EntityManager em;

    public BoardRepositoryImpl(EntityManager em) {
        this.em = em;
    }

    @Override
    public Board save(Board board) {
        em.persist(board);
        return board;
    }

    @Override
    public Optional<Board> findByBoardNo(Integer boardNo) {
        Board board = em.find(Board.class, boardNo);
        return Optional.ofNullable(board);
    }

    @Override
    public List<Board> findByCategoryNoOrderByBoardNoDesc(Integer categoryNo) {
        return em.createQuery("select b from Board b where b.category.categoryNo = :categoryNo order by b.boardNo desc", Board.class)
                .setParameter("categoryNo", categoryNo)
                .getResultList();
    }

    @Override
    public List<Board> findAllOrderByBoardNoDesc() {
        return em.createQuery("select b from Board b order by b.boardNo desc", Board.class)
                .getResultList();
    }
}
