package sk.uniza.fri.telemedicine.enums;

import lombok.Getter;

@Getter
public enum Frequency {
    ONE_TIME_DAILY(1),
    TWO_TIMES_DAILY(2);

    private final int expectedTimes;

    Frequency(int expectedTimes) {
        this.expectedTimes = expectedTimes;
    }
}