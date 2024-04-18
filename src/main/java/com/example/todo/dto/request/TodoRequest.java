package com.example.todo.dto.request;

import com.example.todo.util.Preference;
import com.example.todo.util.Status;
import com.example.todo.util.Tag;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TodoRequest {

    private String name;
    private String description;
    private Status status;
    private LocalDateTime dateTime;
    private Tag tag;
    private Preference preference;
    private LocalDateTime deliveryTime;
    private Long userId;
   // private String message;

}
