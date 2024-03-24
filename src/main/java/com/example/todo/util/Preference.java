package com.example.todo.util;

import lombok.Getter;

@Getter
public enum Preference {

    LOW(0),
    MIDDLE(1),
    HIGH(2);

    private final int code;

    Preference(int code) {
        this.code = code;
    }
}
