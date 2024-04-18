package com.example.todo.repository;

import com.example.todo.entity.ToDo;
import com.example.todo.util.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ToDoRepository extends JpaRepository<ToDo, Long> {

    Long countByUserIdAndStatusAndDateTimeBetween(Long userId, Status status, LocalDateTime startDate, LocalDateTime endDate);
    List<ToDo> findToDoNameByUserIdAndStatusAndDateTimeBetween(Long userId, Status status, LocalDateTime startDate, LocalDateTime endDate);
   // Long calculateTotalMinutesWorkedByStatusAndUserIdAndDateTimeBetween(Long userId, Status status, LocalDateTime localDateTime, LocalDateTime localDateTime1);


}
