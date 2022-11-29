package com.example.sharablead.util;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Objects;

public class TimeUtil {
    public static String getShortTime(LocalDateTime time) {

        if (Objects.isNull(time)){
            return "";
        }
        ZoneId zoneId = ZoneId.systemDefault();
        LocalDateTime now = LocalDateTime.now();
        String shortString;
        int delta;

        long delTime = (now.atZone(zoneId).toInstant().toEpochMilli() - time.atZone(zoneId).toInstant().toEpochMilli()) / 1000;
        if (delTime > 365 * 24 * 60 * 60) {
            delta = (int) (delTime / (365 * 24 * 60 * 60));
            shortString = delta == 1 ? "a year ago" :  delta + " years ago";
        } else if(delTime > 30 * 24 * 60 * 60){
            delta = (int) (delTime / (30 * 24 * 60 * 60));
            shortString = delta == 1 ? "a month ago" : delta + " months age";
        }else if (delTime > 24 * 60 * 60) {
            delta = (int) (delTime / (24 * 60 * 60));
            shortString = delta == 1 ? "a day ago" : delta + " days ago";
        } else if (delTime > 60 * 60) {
            delta = (int) (delTime / (60 * 60));
            shortString = delta == 1 ? "an hour ago" : delta + " hours ago";
        } else if (delTime > 60) {
            delta = (int) (delTime / (60));
            shortString =  delta == 1 ? "a minute ago" : delta + " minutes ago";
        } else if (delTime > 1) {
            shortString = delTime + " seconds ago";
        } else {
            shortString = "now";
        }
        return shortString;
    }
}
