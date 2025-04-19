package com.capstone.meetingmap.user.service;

import com.capstone.meetingmap.user.dto.*;
import com.capstone.meetingmap.user.entity.User;
import com.capstone.meetingmap.user.repository.UserRepository;
import com.capstone.meetingmap.userrole.entity.UserRole;
import com.capstone.meetingmap.userrole.repository.UserRoleRepository;
import com.capstone.meetingmap.util.HashUtil;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;

    public UserService(UserRepository userRepository, UserRoleRepository userRoleRepository) {
        this.userRepository = userRepository;
        this.userRoleRepository = userRoleRepository;
    }

    //중복 회원 검증
    private void validateDuplicateUser(String userId) {
        userRepository.findByUserId(userId)
                .ifPresent(u -> {
                    throw new IllegalStateException("이미 존재하는 회원입니다.");
                });
    }

    // 요청한 회원 id와 세션의 회원 id 비교
    public void checkCurrentUser(String userId, String sessionUserId) {
        if (sessionUserId == null || !sessionUserId.equals(userId)) { //세션이 없거나 회원 id가 불일치한 경우
            throw new SecurityException("권한이 없습니다.");
        }
    }

    //현재 사용자 정보로 관리자인지 검증
    public void checkAdmin(String sessionUserId) {
        User user = userRepository.findByUserId(sessionUserId)
                .orElseThrow(() -> new EntityNotFoundException("회원 정보가 존재하지 않습니다.")); //해당 회원을 찾지 못하면 예외처리

        if (user.getUserRole().getUserType().equals(1)) {
            throw new SecurityException("권한이 없습니다.");
        }
    }

    //중복 아이디
    public UserCheckIdResponseDto existsByUserId(UserCheckIdRequestDto userCheckIdRequestDto) {
        return new UserCheckIdResponseDto(!userRepository.existsByUserId(userCheckIdRequestDto.getUserId()));
    }

    //회원가입
    @Transactional
    public UserRegisterResponseDto join(UserRegisterRequestDto userRegisterRequestDto) {
        validateDuplicateUser(userRegisterRequestDto.getUserId()); //중복 회원 검증

        String hashedUserPasswd = HashUtil.hashSha512(userRegisterRequestDto.getUserPasswd()); //비밀번호를 해시함수로 암호화
        UserRole userRole = userRoleRepository.findByUserType(2).get(); //회원 유형을 2(일반사용자)로 고정

        User user = userRegisterRequestDto.toEntity(hashedUserPasswd, userRole); // 암호화된 비밀번호와 회원 유형을 넘겨서 User 엔티티 생성
        userRepository.save(user);

        return UserRegisterResponseDto.fromEntity(user); //User 엔티티를 UserRegisterResponseDto로 변환해 반환
    }

    //로그인
    public LoginResponseDto login(LoginRequestDto loginRequestDto) {

        User user = userRepository.findByUserId(loginRequestDto.getUserId())
                .orElseThrow(() -> new RuntimeException("아이디 또는 비밀번호가 잘못되었습니다.")); //해당 회원을 찾지 못하면 예외처리

        if (!HashUtil.hashSha512(loginRequestDto.getUserPasswd()).equals(user.getUserPasswd())) {
            throw new RuntimeException("아이디 또는 비밀번호가 잘못되었습니다."); //비밀번호가 맞지 않으면 예외처리
        }

        return LoginResponseDto.fromEntity(user, "로그인 성공!"); //User 엔티티를 LoginResponseDto로 변환해 반환
    }

    //특정 유저 조회
    public Optional<UserResponseDto> findOne(String userId) {
        Optional<User> optionalUser = userRepository.findByUserId(userId);
        return optionalUser.map(UserResponseDto::fromEntity);
    }

    //모든 유저 조회
    public List<UserResponseDto> findAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(UserResponseDto::fromEntity)
                .collect(Collectors.toList());
    }




}
