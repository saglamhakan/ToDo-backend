package com.example.todo.controller;

import com.example.todo.dto.request.TodoRequest;
import com.example.todo.dto.response.ToDoResponse;
import com.example.todo.service.ToDoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/todo")
public class ToDoController {

    private final ToDoService toDoService;

    public ToDoController(ToDoService toDoService) {
        this.toDoService = toDoService;
    }

    @GetMapping
    public ResponseEntity<List<ToDoResponse>> getAll(){
        return new ResponseEntity<>(toDoService.getAll(), HttpStatus.OK);
    }
    @PostMapping
    public ResponseEntity<ToDoResponse> create(@RequestBody TodoRequest todoRequest){
        return new ResponseEntity<>(toDoService.create(todoRequest),HttpStatus.CREATED);
    }

    @DeleteMapping("{id}")
    public void delete(@PathVariable Long id){
        toDoService.delete(id);
    }

    @PutMapping("{id}")
    public ResponseEntity<ToDoResponse> update(@PathVariable Long id, @RequestBody TodoRequest request){
        return new ResponseEntity<>(toDoService.update(id,request), HttpStatus.OK);

    }
}
