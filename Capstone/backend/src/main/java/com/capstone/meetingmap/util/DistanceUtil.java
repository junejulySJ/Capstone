package com.capstone.meetingmap.util;

import com.capstone.meetingmap.schedule.dto.NearestInfo;
import com.capstone.meetingmap.schedule.dto.SelectedPlace;

import java.util.List;

public class DistanceUtil {
    public static NearestInfo findNearest(SelectedPlace current, List<SelectedPlace> candidates) {
        SelectedPlace nearest = null;
        double minDistance = Double.MAX_VALUE;

        double lat1 = ParseUtil.parseDoubleSafe(current.getLatitude());
        double lon1 = ParseUtil.parseDoubleSafe(current.getLongitude());

        for (SelectedPlace candidate : candidates) {
            double lat2 = ParseUtil.parseDoubleSafe(candidate.getLatitude());
            double lon2 = ParseUtil.parseDoubleSafe(candidate.getLongitude());

            double distance = haversine(lat1, lon1, lat2, lon2);
            System.out.println("distance=" + distance);

            if (distance < minDistance) { // 현재 최소거리보다 작으면 최소거리 변경
                minDistance = distance;
                nearest = candidate;
            }
        }

        return NearestInfo.builder()
                .nearest(nearest)
                .minDistance(minDistance)
                .build();
    }

    private static double haversine(double lat1, double lon1, double lat2, double lon2) {
        double R = 6371.0; // 지구 반지름 (km)
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c;
    }

    public static int estimateTravelTime(double minDistance, double averageSpeed) {
        return (int)Math.round((minDistance / averageSpeed) * 60);
    }
}
