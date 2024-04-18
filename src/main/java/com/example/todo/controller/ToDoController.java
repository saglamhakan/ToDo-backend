package com.example.todo.controller;

import com.example.todo.dto.request.TodoRequest;
import com.example.todo.dto.response.ToDoResponse;
import com.example.todo.entity.ToDo;
import com.example.todo.service.ToDoService;
import com.example.todo.util.Preference;
import com.example.todo.util.Status;
import com.example.todo.util.Tag;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/todo")
public class ToDoController {

    private final ToDoService toDoService;

    public ToDoController(ToDoService toDoService) {
        this.toDoService = toDoService;
    }

    @GetMapping
    public ResponseEntity<List<ToDoResponse>> getAll() {
        return new ResponseEntity<>(toDoService.getAll(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<ToDoResponse> create(@RequestBody TodoRequest todoRequest) {
        return new ResponseEntity<>(toDoService.create(todoRequest), HttpStatus.CREATED);
    }

    @DeleteMapping("{id}")
    public void delete(@PathVariable Long id) {
        toDoService.delete(id);
    }

    @PutMapping("{id}")
    public ResponseEntity<ToDoResponse> update(@PathVariable Long id, @RequestBody TodoRequest request) {
        return new ResponseEntity<>(toDoService.update(id, request), HttpStatus.OK);

    }

    @PatchMapping("/todos/{id}")
    public ResponseEntity<ToDoResponse> updateToDoFields(@PathVariable Long id, @RequestBody Map<String, Object> updates) {
        try {
            ToDoResponse updatedToDo = toDoService.updateToDoFields(id, updates);
            return ResponseEntity.ok(updatedToDo);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(null); // null yerine daha açıklayıcı bir hata mesajı veya nesnesi döndürebilirsiniz
        }
    }
    @GetMapping("/todos/completed/count")
    public ResponseEntity<Long> getCompletedToDosCount(
            @RequestParam Long userId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(required = false) Status status) {

        Long count = toDoService.getCompletedToDosCount(userId, status,startDate, endDate);
        return ResponseEntity.ok(count);
    }

    @GetMapping("/todos/completed/name")
    public ResponseEntity<List<ToDo>> getCompletedToDosName(
            @RequestParam Long userId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate, @RequestParam Status status) {

        List<ToDo> name = toDoService.getCompletedToDosName(userId, status,startDate, endDate);
        return ResponseEntity.ok(name);
    }
    @GetMapping("calculatePercent")
    public ResponseEntity<String> calculatePercent(){
        return new ResponseEntity<>(toDoService.percentToDoCompleted(), HttpStatus.OK);
    }
    @GetMapping("sortedTodo/{userId}")
    public ResponseEntity<List<ToDo>> sort(@PathVariable Long userId,
            @RequestParam(required = false) Status status,
                                           @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
                                           @RequestParam(required = false) Preference preference,
                                           @RequestParam(required = false) Tag tag,
                                           @RequestParam(defaultValue = "status") String sortOrder){
        return new ResponseEntity<>(toDoService.filterAndSortToDos(userId, status, tag, date,preference, sortOrder), HttpStatus.OK);
    }
}

