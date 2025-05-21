package com.capstone.meetingmap.user.controller;

import com.capstone.meetingmap.board.entity.BoardView;
import com.capstone.meetingmap.board.service.BoardService;
import com.capstone.meetingmap.user.dto.*;
import com.capstone.meetingmap.user.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    private final UserService userService;
    private final BoardService boardService;

    public UserController(UserService userService, BoardService boardService) {
        this.userService = userService;
        this.boardService = boardService;
    }

    //아이디 중복 검사
    @PostMapping("/check-id")
    public ResponseEntity<?> checkUserId(@RequestBody UserCheckIdRequestDto userCheckIdRequestDto) {
        UserCheckIdResponseDto userCheckIdResponseDto = userService.existsByUserId(userCheckIdRequestDto);
        return ResponseEntity.ok(userCheckIdResponseDto);
    }

    //회원가입 처리
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody UserRegisterRequestDto userRegisterRequestDto) {
        UserResponseDto userResponseDto = userService.join(userRegisterRequestDto);

        log.info("New User Registered!: {}", userRegisterRequestDto.getUserId());

        return ResponseEntity.status(HttpStatus.CREATED).body(userResponseDto);
    }

    //회원 본인 조회
    @GetMapping
    public ResponseEntity<?> getUser() {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        UserResponseDto userResponseDto = userService.findOne(userId);
        return ResponseEntity.ok(userResponseDto);
    }

    //전체 회원 조회
    @GetMapping("/list")
    public ResponseEntity<List<UserResponseDto>> list() {
        List<UserResponseDto> users = userService.findAllUsers();
        return ResponseEntity.ok(users);
    }

    //회원 정보 수정
    @PutMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> updateUser(
            @RequestPart("user") UserUpdateRequestDto userUpdateRequestDto,
            @RequestPart(value = "profileImage", required = false) MultipartFile profileImage
    ) {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();

        userService.updateUser(userUpdateRequestDto, profileImage, userId);

        log.info("User Updated: {}", userId);

        return ResponseEntity.noContent().build();
    }

    // 작성한 글 조회
    @GetMapping("/boards")
    public ResponseEntity<?> getMyBoards(
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(defaultValue = "boardWriteDate") String sortBy,
            @RequestParam(defaultValue = "desc") String direction
    ) {
        Sort sort = direction.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);

        String userId = SecurityContextHolder.getContext().getAuthentication().getName();

        Page<BoardView> boardView = boardService.searchArticlesByUserDesc(userId, pageable);
        return ResponseEntity.ok(boardView);
    }

    // 좋아요한 글 조회
    @GetMapping("/boards/liked")
    public ResponseEntity<?> getLikedBoards(
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(defaultValue = "boardWriteDate") String sortBy,
            @RequestParam(defaultValue = "desc") String direction
    ) {
        Sort sort = direction.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);

        String userId = SecurityContextHolder.getContext().getAuthentication().getName();

        Page<BoardView> boardView = boardService.searchArticlesByLikedDesc(userId, pageable);
        return ResponseEntity.ok(boardView);
    }
}
