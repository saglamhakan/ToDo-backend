package com.example.todo.dto.request;

import com.example.todo.entity.ToDo;
import lombok.Data;

import java.util.List;

@Data
public class UserRequest {

    private String name;
    private String email;
   // private List<Long> todoIds;
}
