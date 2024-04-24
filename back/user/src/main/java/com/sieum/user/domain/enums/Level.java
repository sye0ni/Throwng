package com.sieum.user.domain.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Level {
    EARPHONES("EARPHONES", 1),
    BUDS("BUDS", 2),
    BUDS_PRO("BUDS_PRO", 3);

    private String value;

    @JsonValue private final int number;
}
