package com.capstone.meetingmap.board.controller;

import com.capstone.meetingmap.board.dto.BoardCreateRequestDto;
import com.capstone.meetingmap.board.dto.BoardCreateResponseDto;
import com.capstone.meetingmap.board.dto.BoardResponseDto;
import com.capstone.meetingmap.board.service.BoardService;
import com.capstone.meetingmap.util.SessionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
public class BoardController {

    private static final Logger log = LoggerFactory.getLogger(BoardController.class);
    private final BoardService boardService;

    public BoardController(BoardService boardService) {
        this.boardService = boardService;
    }

    //게시글 상세보기
    @GetMapping("/boards/{boardNo}")
    public ResponseEntity<BoardResponseDto> viewDetail(@PathVariable("boardNo") Integer boardNo) {
        return ResponseEntity.ok(boardService.searchByBoardNo(boardNo));
    }

    //전체/카테고리별 게시글 보기
    @GetMapping("/boards")
    public ResponseEntity<List<BoardResponseDto>> search(@RequestParam(value = "category", required = false) Integer categoryNo) {
        System.out.println(categoryNo);

        // category 파라미터가 없으면 전체 목록을 가져오는 메서드 호출
        if (categoryNo == null) {
            return ResponseEntity.ok(boardService.searchAllDesc());
        }
        // categoryNo 파라미터가 있으면 해당 카테고리 목록을 가져오는 메서드 호출
        return ResponseEntity.ok(boardService.searchByCategoryNoDesc(categoryNo));
    }

    //게시글 추가
    @PostMapping("/boards/create")
    public ResponseEntity<BoardCreateResponseDto> apiCreate(@RequestBody BoardCreateRequestDto boardCreateRequestDto) {

        String userId = SessionUtil.getLoggedInUserId();
        if (userId == null) {
            log.warn("Unauthorized access attempt to create board.");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null); // 로그인 안 된 경우 접근 거부
        }

        try {
            BoardCreateResponseDto boardCreateResponseDto = boardService.create(boardCreateRequestDto, userId);

            log.info("post added: {}", boardCreateRequestDto.getBoardTitle());

            return ResponseEntity.status(HttpStatus.CREATED).body(boardCreateResponseDto);
        } catch(RuntimeException e) {
            log.error("Failed to create post: {} by userId: {}", boardCreateRequestDto.getBoardTitle(), userId, e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null); // 존재하지 않는 카테고리나 회원 정보가 없으면 404 NOT FOUND
        }
    }
}
