package com.example.todo.util;

import lombok.Getter;

@Getter
public enum Tag {

    BUSINESS(1),
    PERSONALITY(2);

    private final int code;

    Tag(int code) {
        this.code = code;
    }
}
