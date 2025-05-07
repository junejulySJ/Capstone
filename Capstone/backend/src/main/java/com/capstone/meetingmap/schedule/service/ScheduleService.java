package com.capstone.meetingmap.schedule.service;

import com.capstone.meetingmap.api.kakaomobility.service.KakaoMobilityDirectionService;
import com.capstone.meetingmap.groupuser.entity.GroupUser;
import com.capstone.meetingmap.groupuser.repository.GroupUserRepository;
import com.capstone.meetingmap.schedule.dto.*;
import com.capstone.meetingmap.schedule.entity.Schedule;
import com.capstone.meetingmap.schedule.entity.ScheduleDetail;
import com.capstone.meetingmap.schedule.repository.ScheduleDetailRepository;
import com.capstone.meetingmap.schedule.repository.ScheduleRepository;
import com.capstone.meetingmap.user.dto.UserResponseDto;
import com.capstone.meetingmap.user.entity.User;
import com.capstone.meetingmap.user.repository.UserRepository;
import com.capstone.meetingmap.util.DistanceUtil;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ScheduleService {
    private final ScheduleRepository scheduleRepository;
    private final ScheduleDetailRepository scheduleDetailRepository;
    private final UserRepository userRepository;
    private final GroupUserRepository groupUserRepository;
    private final KakaoMobilityDirectionService kakaoMobilityDirectionService;

    public ScheduleService(ScheduleRepository scheduleRepository, ScheduleDetailRepository scheduleDetailRepository, UserRepository userRepository, GroupUserRepository groupUserRepository, KakaoMobilityDirectionService kakaoMobilityDirectionService) {
        this.scheduleRepository = scheduleRepository;
        this.scheduleDetailRepository = scheduleDetailRepository;
        this.userRepository = userRepository;
        this.groupUserRepository = groupUserRepository;
        this.kakaoMobilityDirectionService = kakaoMobilityDirectionService;
    }

    // 회원이 속한 모든 스케줄 가져오기
    public List<ScheduleResponseDto> getSchedulesByUserId(String userId) {
        // userId로 해당되는 그룹 데이터 가져오기
        List<GroupUser> groupUsers = groupUserRepository.findByUser_UserId(userId);

        // Set으로 중복 자동 제거
        Set<Schedule> schedules = groupUsers.stream()
                .map(GroupUser::getSchedule)
                .collect(Collectors.toSet());

        // 스케줄 목록 반환
        return schedules.stream()
                .map(ScheduleResponseDto::fromEntity)
                .collect(Collectors.toList());
    }

    // 스케줄 세부 정보 가져오기
    public List<ScheduleDetailResponseDto> getScheduleDetails(Integer scheduleNo) {
        List<ScheduleDetail> scheduleDetails = scheduleDetailRepository.findBySchedule_ScheduleNo(scheduleNo);
        List<ScheduleDetailResponseDto> responseDtoList = new ArrayList<>();
        for (ScheduleDetail scheduleDetail : scheduleDetails) {
            responseDtoList.add(ScheduleDetailResponseDto.fromEntity(scheduleDetail));
        }
        return responseDtoList;
    }

    // 스케줄에 속한 모든 구성원 가져오기
    public List<UserResponseDto> getUsersByScheduleNo(Integer scheduleNo) {
        List<GroupUser> groupUsers = groupUserRepository.findBySchedule_ScheduleNo(scheduleNo);
        return groupUsers.stream()
                .map(groupUser -> UserResponseDto.fromEntity(groupUser.getUser()))
                .collect(Collectors.toList());
    }

    // 세부 스케줄과 함께 스케줄 추가
    @Transactional
    public Integer saveScheduleWithDetails(String userId, ScheduleSaveRequestDto scheduleSaveRequestDto) {
        // 1. User 조회
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "회원 정보를 찾을 수 없습니다"));

        // 2. Schedule 생성 및 저장
        Schedule schedule = scheduleSaveRequestDto.toEntity(scheduleSaveRequestDto, user);
        Schedule savedSchedule = scheduleRepository.save(schedule);

        // 3. ScheduleDetail 저장
        for (ScheduleDetailRequestDto detailDto : scheduleSaveRequestDto.getDetails()) {
            ScheduleDetail detail = detailDto.toEntity(savedSchedule);
            scheduleDetailRepository.save(detail);
        }

        return savedSchedule.getScheduleNo();
    }

    // 스케줄 수정
    @Transactional
    public void updateSchedule(String userId, Integer scheduleNo, ScheduleUpdateRequestDto requestDto) {

        groupUserRepository.findByUser_UserId(userId);

        // userId와 scheduleNo로 GroupUser 검색 (해당 유저가 참여 중인 스케줄인지 확인)
        GroupUser groupUser = groupUserRepository.findByUser_UserIdAndSchedule_ScheduleNo(userId, scheduleNo)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 스케줄에 대한 참여 정보를 찾을 수 없습니다."));

        Schedule schedule = groupUser.getSchedule();

        scheduleDetailRepository.deleteByScheduleScheduleNo(scheduleNo); // 기존 상세일정 삭제

        //일정 저장
        schedule.setScheduleWithoutUserId(requestDto.getScheduleName(), requestDto.getScheduleAbout());
        scheduleRepository.save(schedule);

        //디테일 저장
        for (ScheduleDetailRequestDto detailDto : requestDto.getDetails()) {
            ScheduleDetail detail = detailDto.toEntity(schedule);
            scheduleDetailRepository.save(detail);
        }
    }

    // 스케줄 삭제
    @Transactional
    public void deleteSchedule(String userId, Integer scheduleNo) {

        // 자신이 만든 schedule만 삭제 가능하도록 변경하기
        if (!scheduleRepository.existsByScheduleNoAndUser_UserId(scheduleNo, userId))
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "자신이 만든 스케줄만 삭제 가능합니다");

        scheduleDetailRepository.deleteByScheduleScheduleNo(scheduleNo);
        scheduleRepository.deleteById(scheduleNo);
    }

    // 스케줄 생성
    public ScheduleCreateResponseDto createSchedule(ScheduleCreateRequestDto dto) {

        List<ScheduleDetailCreateDto> detailCreateDtoList = new ArrayList<>();

        if (dto.getAdditionalRecommendation()) { // 추가 추천 받기를 체크한 경우
            // 장소 수-선택한 장소 만큼 추가 장소를 테마, 평점 기준으로 필터링해 선택(정렬은 아마도 1순위: user_ratings_total, 2순위: rating?)
            // 아직 미구현
            int remainingCount = dto.getTotalPlaceCount() - dto.getSelectedPlace().size();
        }
        // 알고리즘
        Set<SelectedPlace> visited = new HashSet<>();
        LocalDateTime currentTime = dto.getScheduleStartTime();

        // 시작 지점 선택
        SelectedPlace current = dto.getSelectedPlace().stream()
                .filter(p -> p.getContentId().equals(dto.getStartContentId()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("시작 장소를 찾을 수 없습니다: " + dto.getStartContentId()));

        visited.add(current);
        LocalDateTime startTime = currentTime; // 시작 시간 임시 저장
        currentTime = currentTime.plusMinutes(current.getStayMinutes());
        detailCreateDtoList.add(ScheduleDetailCreateDto.builder()
                .scheduleContent(current.getTitle() + " 방문")
                .scheduleAddress(current.getAddress())
                .latitude(new BigDecimal(current.getLatitude()))
                .longitude(new BigDecimal(current.getLongitude()))
                .scheduleStartTime(startTime)
                .scheduleEndTime(currentTime)
                .build());

        boolean hadLunch = false;
        boolean hadDinner = false;

        while (true) {

            // 음식점 필터: 점심 or 저녁시간이면 음식점 우선
            boolean isLunchTime = currentTime.toLocalTime().isAfter(LocalTime.of(11, 30)) &&
                    currentTime.toLocalTime().isBefore(LocalTime.of(13, 30));
            boolean isDinnerTime = currentTime.toLocalTime().isAfter(LocalTime.of(17, 30)) &&
                    currentTime.toLocalTime().isBefore(LocalTime.of(19, 30));

            List<SelectedPlace> candidates = new ArrayList<>();
            for (SelectedPlace place : dto.getSelectedPlace()) {
                if (visited.contains(place)) {
                    continue;
                }

                if (isLunchTime && !hadLunch) { // 점심 or 저녁시간이면 음식점 추가
                    if (List.of("A05020100", "A05020200", "A05020300", "A05020400", "A05020700").contains(place.getCat3())) {
                        candidates.add(place);
                        hadLunch = true;
                    }
                } else if (isDinnerTime && !hadDinner) {
                    if (List.of("A05020100", "A05020200", "A05020300", "A05020400", "A05020700").contains(place.getCat3())) {
                        candidates.add(place);
                        hadDinner = true;
                    }
                } else {
                    if (!List.of("A05020100", "A05020200", "A05020300", "A05020400", "A05020700").contains(place.getCat3())) {
                        candidates.add(place);
                    }
                }
            }

            if (candidates.isEmpty()) break;

            // NN 알고리즘: 가장 가까운 장소 선택
            NearestInfo nearestInfo = DistanceUtil.findNearest(current, candidates);
            SelectedPlace next = nearestInfo.getNearest();
            int travelMinutes = DistanceUtil.estimateTravelTime(nearestInfo.getMinDistance(), 4.0);
            //kakaoMobilityDirectionService.getDistanceAndDuration(current, candidates); 미구현

            if (currentTime.plusMinutes(travelMinutes + next.getStayMinutes()).isAfter(dto.getScheduleEndTime())) {
                break; // 시간 초과
            }

            currentTime = currentTime.plusMinutes(travelMinutes);
            visited.add(next);
            current = next;
            LocalDateTime startTime2 = currentTime; // 시작 시간 임시 저장
            currentTime = currentTime.plusMinutes(current.getStayMinutes());
            detailCreateDtoList.add(ScheduleDetailCreateDto.builder()
                    .scheduleContent(current.getTitle() + " 방문")
                    .scheduleAddress(current.getAddress())
                    .latitude(new BigDecimal(current.getLatitude()))
                    .longitude(new BigDecimal(current.getLongitude()))
                    .scheduleStartTime(startTime2)
                    .scheduleEndTime(currentTime)
                    .build());
        }

        return ScheduleCreateResponseDto.builder()
                .scheduleName(dto.getScheduleName())
                .scheduleAbout(dto.getScheduleAbout())
                .details(detailCreateDtoList)
                .build();
    }
}