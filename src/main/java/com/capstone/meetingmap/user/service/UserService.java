package com.capstone.meetingmap.user.service;

import com.capstone.meetingmap.auth.dto.KakaoUserInfo;
import com.capstone.meetingmap.user.dto.UserCheckIdRequestDto;
import com.capstone.meetingmap.user.dto.UserCheckIdResponseDto;
import com.capstone.meetingmap.user.dto.UserRegisterRequestDto;
import com.capstone.meetingmap.user.dto.UserResponseDto;
import com.capstone.meetingmap.user.entity.User;
import com.capstone.meetingmap.user.repository.UserRepository;
import com.capstone.meetingmap.userrole.entity.UserRole;
import com.capstone.meetingmap.userrole.repository.UserRoleRepository;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public UserService(UserRepository userRepository, UserRoleRepository userRoleRepository) {
        this.userRepository = userRepository;
        this.userRoleRepository = userRoleRepository;
        this.bCryptPasswordEncoder = new BCryptPasswordEncoder();
    }

    //중복 아이디 검증
    public UserCheckIdResponseDto existsByUserId(UserCheckIdRequestDto userCheckIdRequestDto) {
        String userId = userCheckIdRequestDto.getUserId();
        boolean isExists = userRepository.existsByUserId(userId);
        return new UserCheckIdResponseDto(!isExists);
    }

    //일반 회원가입
    @Transactional
    public UserResponseDto join(UserRegisterRequestDto userRegisterRequestDto) {
        String userId = userRegisterRequestDto.getUserId();
        String userPasswd = userRegisterRequestDto.getUserPasswd();
        boolean isExists = userRepository.existsByUserId(userId);

        //중복 회원 검증
        if (isExists) {
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }

        String hashedUserPasswd = bCryptPasswordEncoder.encode(userPasswd); //비밀번호를 해시함수로 암호화
        UserRole userRole = userRoleRepository.findByUserType(1).get(); //회원 유형을 1(일반사용자)로 고정

        User user = userRegisterRequestDto.toEntity(hashedUserPasswd, userRole); // 암호화된 비밀번호와 회원 유형을 넘겨서 User 엔티티 생성
        userRepository.save(user);

        return UserResponseDto.fromEntity(user); //User 엔티티를 UserResponseDto로 변환해 반환
    }

    //일반 회원가입
    @Transactional
    public User kakaoJoin(KakaoUserInfo kakaoUser) {
        String userId = "kakao_" + kakaoUser.getId();

        UserRole userRole = userRoleRepository.findByUserType(1).get(); //회원 유형을 1(일반사용자)로 고정

        User user = User.builder()
                .userId(userId)
                .userPasswd("")
                .userEmail(kakaoUser.getKakaoAccount().getEmail())
                .userNick(kakaoUser.getKakaoAccount().getProfile().getNickname())
                .userImg(kakaoUser.getKakaoAccount().getProfile().getProfileImageUrl())
                .userRole(userRole)
                .build();
        userRepository.save(user);

        return user;
    }

    //특정 유저 조회
    public UserResponseDto findOne(String userId) {
        String jwtUserId = SecurityContextHolder.getContext().getAuthentication().getName();
        String role = SecurityContextHolder.getContext().getAuthentication().getAuthorities().iterator().next().getAuthority();

        // 관리자는 바로 조회 가능
        if ("ROLE_Admin".equals(role)) {
            return userRepository.findById(userId)
                    .map(UserResponseDto::fromEntity)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "회원 정보를 찾을 수 없습니다"));
        }

        // 일반 사용자는 본인만 조회 가능
        if (!jwtUserId.equals(userId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "본인만 접근 가능합니다");
        }

        return userRepository.findById(userId)
                .map(UserResponseDto::fromEntity)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "회원 정보를 찾을 수 없습니다"));
    }

    //모든 유저 조회
    public List<UserResponseDto> findAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(UserResponseDto::fromEntity)
                .collect(Collectors.toList());
    }
}
