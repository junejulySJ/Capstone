package com.capstone.meetingmap.board.service;

import com.capstone.meetingmap.board.dto.BoardCreateRequestDto;
import com.capstone.meetingmap.board.dto.BoardCreateResponseDto;
import com.capstone.meetingmap.board.dto.BoardResponseDto;
import com.capstone.meetingmap.board.entity.Board;
import com.capstone.meetingmap.board.repository.BoardRepository;
import com.capstone.meetingmap.category.entity.Category;
import com.capstone.meetingmap.category.repository.CategoryRepository;
import com.capstone.meetingmap.user.entity.User;
import com.capstone.meetingmap.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BoardService {
    private final BoardRepository boardRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

    public BoardService(CategoryRepository categoryRepository, BoardRepository boardRepository, UserRepository userRepository) {
        this.categoryRepository = categoryRepository;
        this.boardRepository = boardRepository;
        this.userRepository = userRepository;
    }

    //게시글 생성
    @Transactional
    public BoardCreateResponseDto create(BoardCreateRequestDto boardCreateRequestDto, String userId) {
        // 실제 존재하는 카테고리 가져오기
        Category category = categoryRepository.findByCategoryNo(boardCreateRequestDto.getCategoryNo())
                .orElseThrow(() -> new RuntimeException("Category not found"));

        // 실제 존재하는 회원 가져오기
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Board board = boardCreateRequestDto.toEntity(category, user);
        boardRepository.save(board);

        return BoardCreateResponseDto.fromEntity(board);
    }

    //게시글 상세보기
    public BoardResponseDto searchByBoardNo(Integer boardNo) {
        Board board = boardRepository.findByBoardNo(boardNo)
                .orElseThrow(() -> new RuntimeException("Board not found"));
        return BoardResponseDto.fromEntity(board);
    }

    //카테고리별 게시글 보기
    public List<BoardResponseDto> searchByCategoryNoDesc(Integer categoryNo) {
        // 실제 존재하는 카테고리인지 검증
        categoryRepository.findByCategoryNo(categoryNo)
                .orElseThrow(() -> new RuntimeException("Category not found"));

        List<Board> boards = boardRepository.findByCategoryNoOrderByBoardNoDesc(categoryNo);
        List<BoardResponseDto> boardDtos = new ArrayList<>();
        for (Board board : boards) {
            boardDtos.add(BoardResponseDto.fromEntity(board));
        }
        return boardDtos;
    }

    //전체 게시글 보기
    public List<BoardResponseDto> searchAllDesc() {
        List<Board> boards = boardRepository.findAllOrderByBoardNoDesc();
        List<BoardResponseDto> posts = new ArrayList<>();
        for (Board board : boards) {
            posts.add(BoardResponseDto.fromEntity(board));
        }
        return posts;
    }
}
