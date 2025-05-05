package com.capstone.meetingmap.board.controller;

import com.capstone.meetingmap.board.dto.BoardCreateRequestDto;
import com.capstone.meetingmap.board.dto.BoardCreateResponseDto;
import com.capstone.meetingmap.board.dto.BoardResponseDto;
import com.capstone.meetingmap.board.service.BoardService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/boards")
public class BoardController {

    private static final Logger log = LoggerFactory.getLogger(BoardController.class);
    private final BoardService boardService;

    public BoardController(BoardService boardService) {
        this.boardService = boardService;
    }

    //게시글 상세보기
    @GetMapping("/{boardNo}")
    public ResponseEntity<BoardResponseDto> viewDetail(@PathVariable("boardNo") Integer boardNo) {
        return ResponseEntity.ok(boardService.searchByBoardNo(boardNo));
    }

    //전체/카테고리별 게시글 보기
    @GetMapping
    public ResponseEntity<List<BoardResponseDto>> search(@RequestParam(value = "category", required = false) Integer categoryNo) {
        // category 파라미터가 없으면 전체 목록을 가져오는 메서드 호출
        if (categoryNo == null) {
            return ResponseEntity.ok(boardService.searchAllDesc());
        }
        // categoryNo 파라미터가 있으면 해당 카테고리 목록을 가져오는 메서드 호출
        return ResponseEntity.ok(boardService.searchByCategoryNoDesc(categoryNo));
    }

    //게시글 추가
    @PostMapping("/create")
    public ResponseEntity<BoardCreateResponseDto> apiCreate(@RequestBody BoardCreateRequestDto boardCreateRequestDto) {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        BoardCreateResponseDto boardCreateResponseDto = boardService.create(boardCreateRequestDto, userId);

        log.info("post added: {}", boardCreateRequestDto.getBoardTitle());

        return ResponseEntity.status(HttpStatus.CREATED).body(boardCreateResponseDto);
    }
}
