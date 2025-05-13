package com.capstone.meetingmap.schedule.service;

import com.capstone.meetingmap.api.kakaomobility.service.KakaoMobilityDirectionService;
import com.capstone.meetingmap.api.tmap.dto.*;
import com.capstone.meetingmap.api.tmap.service.TMapApiService;
import com.capstone.meetingmap.friendship.entity.FriendshipStatus;
import com.capstone.meetingmap.friendship.repository.FriendshipRepository;
import com.capstone.meetingmap.groupuser.entity.GroupUser;
import com.capstone.meetingmap.groupuser.entity.GroupUserId;
import com.capstone.meetingmap.groupuser.repository.GroupUserRepository;
import com.capstone.meetingmap.map.dto.PlaceResponseDto;
import com.capstone.meetingmap.map.service.MapService;
import com.capstone.meetingmap.schedule.dto.*;
import com.capstone.meetingmap.schedule.entity.Schedule;
import com.capstone.meetingmap.schedule.entity.ScheduleDetail;
import com.capstone.meetingmap.schedule.repository.ScheduleDetailRepository;
import com.capstone.meetingmap.schedule.repository.ScheduleRepository;
import com.capstone.meetingmap.user.dto.UserResponseDto;
import com.capstone.meetingmap.user.entity.User;
import com.capstone.meetingmap.user.repository.UserRepository;
import com.capstone.meetingmap.util.ParseUtil;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ScheduleService {
    private final TMapApiService tMapApiService;
    private final ScheduleRepository scheduleRepository;
    private final ScheduleDetailRepository scheduleDetailRepository;
    private final UserRepository userRepository;
    private final GroupUserRepository groupUserRepository;
    private final KakaoMobilityDirectionService kakaoMobilityDirectionService;
    private final FriendshipRepository friendshipRepository;
    private final MapService mapService;

    public ScheduleService(TMapApiService tMapApiService, ScheduleRepository scheduleRepository, ScheduleDetailRepository scheduleDetailRepository, UserRepository userRepository, GroupUserRepository groupUserRepository, KakaoMobilityDirectionService kakaoMobilityDirectionService, FriendshipRepository friendshipRepository, MapService mapService) {
        this.tMapApiService = tMapApiService;
        this.scheduleRepository = scheduleRepository;
        this.scheduleDetailRepository = scheduleDetailRepository;
        this.userRepository = userRepository;
        this.groupUserRepository = groupUserRepository;
        this.kakaoMobilityDirectionService = kakaoMobilityDirectionService;
        this.friendshipRepository = friendshipRepository;
        this.mapService = mapService;
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
    public List<UserResponseDto> getUsersByScheduleNo(String userId, Integer scheduleNo) {

        // 자신이 속한 group만 조회 가능
        if (!groupUserRepository.existsBySchedule_ScheduleNoAndUser_UserId(scheduleNo, userId))
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "자신이 속한 그룹만 조회 가능합니다");

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
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 스케줄에 대한 참여 정보를 찾을 수 없습니다"));

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

        // 자신이 만든 schedule만 삭제 가능
        if (!scheduleRepository.existsByScheduleNoAndUser_UserId(scheduleNo, userId))
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "자신이 만든 스케줄만 삭제 가능합니다");

        scheduleDetailRepository.deleteByScheduleScheduleNo(scheduleNo);
        scheduleRepository.deleteById(scheduleNo);
    }

    // 스케줄 공유
    public void shareSchedule(String userId, ScheduleShareRequestDto scheduleShareRequestDto) {
        // 자신이 만든 schedule만 공유 가능
        if (!scheduleRepository.existsByScheduleNoAndUser_UserId(scheduleShareRequestDto.getScheduleNo(), userId))
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "자신이 만든 스케줄만 공유 가능합니다");

        for (String friendId : scheduleShareRequestDto.getUserIds()) {
            if (!friendshipRepository.existsByUser_UserIdAndOpponent_UserIdAndStatus(userId, friendId, FriendshipStatus.ACCEPTED)) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "친구가 아닌 사용자가 포함되어 있습니다: " + friendId);
            }

            User friendUser = userRepository.findById(friendId)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "회원 정보를 찾을 수 없습니다"));

            Schedule schedule = scheduleRepository.findById(scheduleShareRequestDto.getScheduleNo())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "스케줄을 찾을 수 없습니다"));

            // groupUser에 추가가 안되어있으면 추가
            if (!groupUserRepository.existsBySchedule_ScheduleNoAndUser_UserId(schedule.getScheduleNo(), friendId)) {
                groupUserRepository.save(new GroupUser(schedule, friendUser));
            }
        }
    }

    // 스케줄 공유 취소
    public void unshareSchedule(String userId, ScheduleShareRequestDto scheduleShareRequestDto) {
        Schedule schedule = scheduleRepository.findById(scheduleShareRequestDto.getScheduleNo())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "스케줄을 찾을 수 없습니다"));

        // 자신이 만든 schedule만 공유 취소 가능
        if (!scheduleRepository.existsByScheduleNoAndUser_UserId(scheduleShareRequestDto.getScheduleNo(), userId))
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "자신이 만든 스케줄만 공유 취소 가능합니다");

        for (String friendId : scheduleShareRequestDto.getUserIds()) {
            if (!groupUserRepository.existsBySchedule_ScheduleNoAndUser_UserId(scheduleShareRequestDto.getScheduleNo(), friendId)) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "해당 사용자가 스케줄에 없습니다: " + friendId);
            }

            groupUserRepository.deleteById(new GroupUserId(scheduleShareRequestDto.getScheduleNo(), friendId));
        }

    }

    // 스케줄 생성
    public ScheduleCreateResponseDto createSchedule(ScheduleCreateRequestDto dto) {

        List<ScheduleDetailCreateDto> detailCreateDtoList = new ArrayList<>();

        if (dto.getAdditionalRecommendation()) { // 추가 추천 받기를 체크한 경우
            // 장소 수-선택한 장소 만큼 추가 장소를 시작 장소를 기준으로 테마, 평점 기준으로 필터링해 선택(정렬은 user_ratings_total 기준 정렬)
            int remainingCount = dto.getTotalPlaceCount() - dto.getSelectedPlace().size();

            // 시작 지점 찾기
            SelectedPlace startPlace = dto.getSelectedPlace().stream()
                    .filter(place -> place.getContentId().equals(dto.getStartContentId()))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("시작 장소를 찾을 수 없습니다: " + dto.getStartContentId()));

            List<PlaceResponseDto> mergedResponse = new ArrayList<>();

            // 음식점을 1개도 고르지 않은 경우 처리
            if (dto.getSelectedPlace().stream().noneMatch(place -> place.getCategory() != null && place.getCategory().startsWith("food-"))) {
                System.out.println("이거 실행되야함");
                List<PlaceResponseDto> response = mapService.getAllPlaces("user_ratings_total_dsc", startPlace.getLatitude(), startPlace.getLongitude(), "food");
                // 점심식사 가능한 시간
                if (dto.getScheduleStartTime().toLocalTime().isBefore(LocalTime.of(11, 30)) && dto.getScheduleEndTime().toLocalTime().isAfter(LocalTime.of(13, 30))) {
                    mergedResponse.add(response.get(0));
                    remainingCount = remainingCount - 1;
                }
                // 저녁식사 가능한 시간
                if (dto.getScheduleStartTime().toLocalTime().isBefore(LocalTime.of(17, 30)) && dto.getScheduleEndTime().toLocalTime().isAfter(LocalTime.of(19, 30))) {
                    mergedResponse.add(response.get(1));
                    remainingCount = remainingCount - 1;
                }
                System.out.println("mergedResponse.size=!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!" + mergedResponse.size());
            }
            String category = null;
            switch (dto.getTheme()) {
                case "tour" -> category = "tour";
                case "nature" -> {
                    category = "tour-nature";
                }
                case "history" -> {
                    category = "tour-tradition";
                }
                case "food" -> {
                    category = "food";
                }
                case "shopping" -> {
                    category = "shopping";
                }
                case "date" -> {
                    category = "tour-park";
                }
            }
            List<PlaceResponseDto> response = mapService.getAllPlaces("user_ratings_total_dsc", startPlace.getLatitude(), startPlace.getLongitude(), category);
            if (dto.getTheme().equals("date")) { // 데이트 테마인 경우 추가 검색
                List<PlaceResponseDto> response2 = mapService.getAllPlaces("user_ratings_total_dsc", startPlace.getLatitude(), startPlace.getLongitude(), "cafe");
                response.addAll(response2);
                List<PlaceResponseDto> response3 = mapService.getAllPlaces("user_ratings_total_dsc", startPlace.getLatitude(), startPlace.getLongitude(), "tour-theme-park");
                response.addAll(response3);
            }
            response.sort(Comparator.comparing(PlaceResponseDto::getUserRatingsTotal));
            for (int i = 0; i < remainingCount; i++) {
                mergedResponse.add(response.get(i));
            }

            List<SelectedPlace> additionalPlaceList = new ArrayList<>();
            for (PlaceResponseDto placeDto : mergedResponse) {
                additionalPlaceList.add(placeDto.toSelectedPlace(dto.getStayMinutesMean()));
            }
            dto.getSelectedPlace().addAll(additionalPlaceList);

        }
        // 알고리즘 변수
        Set<SelectedPlace> visited = new HashSet<>();
        LocalDateTime currentTime = dto.getScheduleStartTime();

        // 시작 지점 선택
        SelectedPlace currentPlace = dto.getSelectedPlace().stream()
                .filter(p -> p.getContentId().equals(dto.getStartContentId()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("시작 장소를 찾을 수 없습니다: " + dto.getStartContentId()));

        // 시작 지점 방문 처리
        visited.add(currentPlace);

        // 시작 시간 임시 저장
        LocalDateTime startTime = currentTime;

        // 시작 장소 방문 후 머문 시간을 반영해 변경
        currentTime = currentTime.plusMinutes(currentPlace.getStayMinutes());

        //detail dto에 반영
        detailCreateDtoList.add(ScheduleDetailCreateDto.builder()
                .scheduleContent(currentPlace.getName() + " 방문")
                .scheduleAddress(currentPlace.getAddress())
                .latitude(new BigDecimal(currentPlace.getLatitude()))
                .longitude(new BigDecimal(currentPlace.getLongitude()))
                .scheduleStartTime(startTime)
                .scheduleEndTime(currentTime)
                .build());

        // 식사 여부
        boolean hadLunch = false;
        boolean hadDinner = false;

        while (true) {

            // 방문해야 할 장소
            List<SelectedPlace> candidates = new ArrayList<>();

            if (dto.getTheme().equals("food")) {
                for (SelectedPlace place : dto.getSelectedPlace()) {
                    if (visited.contains(place)) { // 이미 방문한 장소면 continue
                        continue;
                    }
                    candidates.add(place);
                }
            } else {
                // 음식점 필터: 점심 or 저녁시간이면 음식점 우선
                boolean isLunchTime = currentTime.toLocalTime().isAfter(LocalTime.of(11, 30)) &&
                        currentTime.toLocalTime().isBefore(LocalTime.of(13, 30));
                boolean isDinnerTime = currentTime.toLocalTime().isAfter(LocalTime.of(17, 30)) &&
                        currentTime.toLocalTime().isBefore(LocalTime.of(19, 30));

                for (SelectedPlace place : dto.getSelectedPlace()) {
                    if (visited.contains(place)) { // 이미 방문한 장소면 continue
                        continue;
                    }

                    if (isLunchTime && !hadLunch) { // 점심시간이면 음식점 추가
                        if (place.getCategory().startsWith("food-")) {
                            candidates.add(place);
                            hadLunch = true;
                        }
                    } else if (isDinnerTime && !hadDinner) { // 저녁시간이면 음식점 추가
                        if (place.getCategory().startsWith("food-")) {
                            candidates.add(place);
                            hadDinner = true;
                        }
                    } else { // 그 외 시간이면 음식점을 제외하고 추가
                        if (!place.getCategory().startsWith("food-")) {
                            candidates.add(place);
                        }
                    }
                }
            }

            // 방문해야 할 장소가 없으면 반복 종료
            if (candidates.isEmpty()) break;

            // NN 알고리즘: 가장 가까운 장소 선택
            // 도보거나 자동차이면 tmap api로 거리/시간 계산
            List<RouteResponse> routeResponseList = new ArrayList<>();
            if (dto.getTransport().equals("도보")) {
                for (SelectedPlace candidate : candidates) {
                    RouteResponse routeResponse = tMapApiService.getPedestrianRoutes(PedestrianRouteRequest.builder()
                            .startX(ParseUtil.parseDoubleSafe(currentPlace.getLongitude()))
                            .startY(ParseUtil.parseDoubleSafe(currentPlace.getLatitude()))
                            .endX(ParseUtil.parseDoubleSafe(candidate.getLongitude()))
                            .endY(ParseUtil.parseDoubleSafe(candidate.getLatitude()))
                            .build());
                    routeResponseList.add(routeResponse);
                }
            } else if (dto.getTransport().equals("자동차")) {
                for (SelectedPlace candidate : candidates) {
                    RouteResponse routeResponse = tMapApiService.getCarRoutes(CarRouteRequest.builder()
                            .startX(ParseUtil.parseDoubleSafe(currentPlace.getLongitude()))
                            .startY(ParseUtil.parseDoubleSafe(currentPlace.getLatitude()))
                            .endX(ParseUtil.parseDoubleSafe(candidate.getLongitude()))
                            .endY(ParseUtil.parseDoubleSafe(candidate.getLatitude()))
                            .build());
                    routeResponseList.add(routeResponse);
                }
            }
            // 다음 장소 결정
            NearestInfo nearestInfo = tMapApiService.findNearest(routeResponseList, candidates);
            SelectedPlace next = nearestInfo.getNearest();

            // 이동 시간 결정(30분 단위로 올림)
            int travelMinutes = ((nearestInfo.getMinTime() + 29) / 30) * 30;

            // 이동 후 머문 시간이 종료 시간을 넘으면 반복 종료
            if (currentTime.plusMinutes(travelMinutes + next.getStayMinutes()).isAfter(dto.getScheduleEndTime())) {
                break;
            }

            // 이동 시간 반영
            currentTime = currentTime.plusMinutes(travelMinutes);

            // 방문 처리
            visited.add(next);

            // 현재 장소를 변경
            currentPlace = next;

            // 시간 임시 저장
            LocalDateTime startTime2 = currentTime;

            // 장소 방문 후 머문 시간을 반영해 변경
            currentTime = currentTime.plusMinutes(currentPlace.getStayMinutes());

            //detail dto에 반영
            detailCreateDtoList.add(ScheduleDetailCreateDto.builder()
                    .scheduleContent(currentPlace.getName() + " 방문")
                    .scheduleAddress(currentPlace.getAddress())
                    .latitude(new BigDecimal(currentPlace.getLatitude()))
                    .longitude(new BigDecimal(currentPlace.getLongitude()))
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