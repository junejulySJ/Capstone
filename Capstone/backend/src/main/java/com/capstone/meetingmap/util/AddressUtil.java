package com.capstone.meetingmap.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AddressUtil {

    private static final String ADDRESS_REGEX =
            "^.*[가-힣]+\\s+[가-힣]+(시|군|구)\\s+.*(동|읍|면|길|로|산)\\s*\\d+(-\\d+)?\\s*$";

    public static String extractGu(String formattedAddress) {
        Pattern pattern = Pattern.compile("([가-힣]+구)");
        Matcher matcher = pattern.matcher(formattedAddress);
        if (matcher.find()) {
            return matcher.group(1); // 예: "강북구"
        }
        return null;
    }

    public static boolean isAddressQuery(String input) {
        if (input == null) return false;
        if (!input.matches(".*[가-힣]+.*")) return false; // 한글 포함 필수

        return input.matches(ADDRESS_REGEX);
    }
}
