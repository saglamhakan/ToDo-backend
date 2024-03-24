package com.example.todo.util;

import lombok.Getter;

@Getter
public enum Status {

    NOT_COMPLETED(0),
    WAITING(1),
    COMPLETED(2);

    private final int code;

    Status(int code) {
        this.code = code;
    }
}
