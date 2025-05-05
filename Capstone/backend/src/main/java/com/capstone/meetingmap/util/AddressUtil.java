package com.capstone.meetingmap.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AddressUtil {
    public static String extractGu(String formattedAddress) {
        Pattern pattern = Pattern.compile("([가-힣]+구)");
        Matcher matcher = pattern.matcher(formattedAddress);
        if (matcher.find()) {
            return matcher.group(1); // 예: "강북구"
        }
        return null;
    }
}
