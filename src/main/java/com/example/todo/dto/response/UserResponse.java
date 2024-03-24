package com.example.todo.dto.response;

import com.example.todo.entity.ToDo;
import lombok.Data;

import java.util.List;

@Data
public class UserResponse {

    private String name;
    private String email;
    private List<ToDo> todos;
}
