package com.example.todo.controller;

import com.example.todo.dto.request.UserRequest;
import com.example.todo.dto.response.UserResponse;
import com.example.todo.service.UserService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/user")
public class UserController {

    private final UserService userService;


    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<UserResponse> create(@RequestBody UserRequest request){
        return new ResponseEntity<>(userService.create(request), HttpStatus.CREATED);

    }
    @GetMapping
    public ResponseEntity<List<UserResponse>> getAll(){
        return new ResponseEntity<>(userService.getAll(), HttpStatus.OK);
    }

    @DeleteMapping("{id}")
    public void deleteById(@PathVariable Long id){
        userService.deleteById(id);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<UserResponse> updateFields(@PathVariable Long id, @RequestBody Map<String, Object> updates){
        return new ResponseEntity<>(userService.updateToDoFields(id, updates), HttpStatus.CREATED);
    }

}
