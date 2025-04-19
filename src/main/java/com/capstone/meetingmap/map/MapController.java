package com.capstone.meetingmap.map;

import com.capstone.meetingmap.map.dto.AreaBasedListResponseDto;
import com.capstone.meetingmap.map.dto.CodeResponseDto;
import com.capstone.meetingmap.map.dto.DetailCommonResponseDto;
import com.capstone.meetingmap.map.service.MapService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:3000")
public class MapController {

    private final MapService mapService;

    public MapController(MapService mapService) {
        this.mapService = mapService;
    }

    //지역/시군구 코드 반환
    @GetMapping("/map/region")
    public ResponseEntity<List<CodeResponseDto>> getRegionCodes(@RequestParam(value = "code", required = false) String areaCode) {
        List<CodeResponseDto> codeResponseDtoList = mapService.getRegionCodes(areaCode);
        return ResponseEntity.ok(codeResponseDtoList);
    }

    //지도 출력
    @GetMapping("/map")
    public ResponseEntity<List<AreaBasedListResponseDto>> getMap(
            @RequestParam(value = "areaCode", required = false) String areaCode,
            @RequestParam(value = "sigunguCode", required = false) String sigunguCode,
            @RequestParam(value = "contentTypeId", required = false) String contentTypeId
    ) {
        List<AreaBasedListResponseDto> areaBasedListResponseDtoList = mapService.getMap(areaCode, sigunguCode, contentTypeId);
        return ResponseEntity.ok(areaBasedListResponseDtoList);
    }

    @GetMapping("/map/detail")
    public ResponseEntity<DetailCommonResponseDto> getPlaceDetail(@RequestParam(value = "contentId") String contentId) {
        try {
            DetailCommonResponseDto detailCommonResponseDto = mapService.getPlaceDetail(contentId);
            return ResponseEntity.ok(detailCommonResponseDto);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
