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
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

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
        Category category = categoryRepository.findById(boardCreateRequestDto.getCategoryNo())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 카테고리가 없습니다"));

        // 실제 존재하는 회원 가져오기
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "회원 정보를 찾을 수 없습니다"));

        Board board = boardCreateRequestDto.toEntity(category, user);
        boardRepository.save(board);

        return BoardCreateResponseDto.fromEntity(board);
    }

    //게시글 상세보기
    public BoardResponseDto searchByBoardNo(Integer boardNo) {
        Board board = boardRepository.findById(boardNo)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 게시글이 없습니다"));
        return BoardResponseDto.fromEntity(board);
    }

    //카테고리별 게시글 보기
    public List<BoardResponseDto> searchByCategoryNoDesc(Integer categoryNo) {
        // 실제 존재하는 카테고리인지 검증
        categoryRepository.findById(categoryNo)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 카테고리가 없습니다"));

        List<Board> boards = boardRepository.findByCategory_CategoryNoOrderByBoardNoDesc(categoryNo);
        List<BoardResponseDto> boardDtos = new ArrayList<>();
        for (Board board : boards) {
            boardDtos.add(BoardResponseDto.fromEntity(board));
        }
        return boardDtos;
    }

    //전체 게시글 보기
    public List<BoardResponseDto> searchAllDesc() {
        List<Board> boards = boardRepository.findAllByOrderByBoardNoDesc();
        List<BoardResponseDto> posts = new ArrayList<>();
        for (Board board : boards) {
            posts.add(BoardResponseDto.fromEntity(board));
        }
        return posts;
    }
}
