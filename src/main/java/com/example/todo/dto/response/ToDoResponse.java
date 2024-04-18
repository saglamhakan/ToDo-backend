package com.example.todo.dto.response;

import com.example.todo.util.Preference;
import com.example.todo.util.Status;
import com.example.todo.util.Tag;
import lombok.Data;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;

@Data
public class ToDoResponse {

    private Long id;
    private String name;
    private String description;
    private Status status;
    private LocalDateTime dateTime;
    private Tag tag;
    private Preference preference;
    private LocalDateTime deliveryTime;

}
