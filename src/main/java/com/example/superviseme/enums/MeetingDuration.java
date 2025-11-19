package com.example.superviseme.enums;

import java.time.Duration;

import lombok.Getter;

@Getter
public enum MeetingDuration {
	MIN_15(1, Duration.ofMinutes(15)),
    MIN_30(2, Duration.ofMinutes(30)),
    ONE_HOUR(3,Duration.ofHours(1)),
    ONE_HOUR_30_MINS(4,Duration.ofHours(1).plusMinutes(30)),
    TWO_HOURS(5,Duration.ofHours(2));

    private final int code;
    private final Duration duration;


	MeetingDuration(int code, Duration duration) {
        this.code = code;
        this.duration = duration;
    }
	
	
	public static MeetingDuration fromCode(int code) {
        for (MeetingDuration duration : MeetingDuration.values()) {
            if (duration.code == code) {
                return duration;
            }
        }
        throw new IllegalArgumentException("Invalid code: " + code);
    }

}
