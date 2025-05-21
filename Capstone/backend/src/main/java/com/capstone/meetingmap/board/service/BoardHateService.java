package com.capstone.meetingmap.board.service;

import com.capstone.meetingmap.board.entity.Board;
import com.capstone.meetingmap.board.entity.BoardHate;
import com.capstone.meetingmap.board.repository.BoardHateRepository;
import com.capstone.meetingmap.board.repository.BoardRepository;
import com.capstone.meetingmap.user.entity.User;
import com.capstone.meetingmap.user.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class BoardHateService {
    private final BoardRepository boardRepository;
    private final UserRepository userRepository;
    private final BoardHateRepository boardHateRepository;

    public BoardHateService(BoardRepository boardRepository, UserRepository userRepository, BoardHateRepository boardHateRepository) {
        this.boardRepository = boardRepository;
        this.userRepository = userRepository;
        this.boardHateRepository = boardHateRepository;
    }

    @Transactional
    public void addHate(Integer boardNo, String userId) {
        if (boardHateRepository.existsByBoard_BoardNoAndUser_UserId(boardNo, userId)) {
            throw new IllegalStateException("이미 싫어요를 눌렀습니다");
        }
        Board board = boardRepository.findById(boardNo)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 게시글을 찾을 수 없습니다"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "회원 정보를 찾을 수 없습니다"));

        boardHateRepository.save(BoardHate.builder()
                .board(board)
                .user(user)
                .build());
    }
}
