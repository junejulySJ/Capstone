package com.capstone.meetingmap.board.controller;

import com.capstone.meetingmap.board.dto.BoardCreateRequestDto;
import com.capstone.meetingmap.board.dto.BoardCreateResponseDto;
import com.capstone.meetingmap.board.dto.BoardResponseDto;
import com.capstone.meetingmap.board.service.BoardService;
import com.capstone.meetingmap.category.service.CategoryService;
import com.capstone.meetingmap.util.SessionUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
public class BoardController {

    private final BoardService boardService;

    public BoardController(BoardService boardService, CategoryService categoryService) {
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

        if (SessionUtil.getLoggedInUserId() == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null); // 로그인 안 된 경우 접근 거부
        }

        try {
            BoardCreateResponseDto boardCreateResponseDto = boardService.create(boardCreateRequestDto, SessionUtil.getLoggedInUserId());

            System.out.println("post added: " + boardCreateRequestDto.getBoardTitle());

            return ResponseEntity.status(HttpStatus.CREATED).body(boardCreateResponseDto);
        } catch(RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null); // 존재하지 않는 카테고리나 회원 정보가 없으면 404 NOT FOUND
        }
    }
}
