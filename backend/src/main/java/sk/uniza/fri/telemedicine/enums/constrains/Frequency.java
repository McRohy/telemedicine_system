package sk.uniza.fri.telemedicine.enums.constrains;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter @AllArgsConstructor
public enum Frequency {
    ONE_TIME_DAILY("1x denne"),
    TWO_TIMES_DAILY("2x denne");

    private final String description;
}