package com.example.todo.util;

import lombok.Getter;

@Getter
public enum Status {

    NOT_COMPLETED(0),
    COMPLETED(1);

    private final int code;

    Status(int code) {
        this.code = code;
    }
}
