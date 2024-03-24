package com.example.todo.util;

import com.example.todo.entity.ToDo;
import com.example.todo.repository.ToDoRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Component;

import javax.management.Notification;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.List;

@Component
public class NotificationScheduler {
/*
    @Autowired
    private TaskScheduler taskScheduler;

    @Autowired
    private ToDoRepository todoRepository;

    @Autowired
    private NotificationService notificationService;

    // Uygulama başlatıldığında çalışır
    @PostConstruct
    public void scheduleTaskForDueTodos() {
        List<ToDo> allTodos = todoRepository.findAll();
        for (ToDo todo : allTodos) {
            scheduleNotificationForTodo(todo);
        }
    }

    private void scheduleNotificationForTodo(ToDo todo) {
        if (todo.getStatus() == Status.NOT_COMPLETED) {
            Runnable task = () -> {
                if (todo.getStatus() == Status.NOT_COMPLETED) {
                    notificationService.sendNotification(todo);
                }
            };

            // `deliveryTime` geçmişse, görevi hemen çalıştır
            Instant now = Instant.now();
            Instant deliveryTime = todo.getDeliveryTime().toInstant(ZoneOffset.UTC);
            if (deliveryTime.isBefore(now)) {
                task.run();
            } else {
                // `deliveryTime` gelecekteyse, o zamana kadar beklet
                long delay = deliveryTime.toEpochMilli() - now.toEpochMilli();
                taskScheduler.schedule(task, new Date(System.currentTimeMillis() + delay));
            }
        }
    }

    // Yeni ToDo ekledikten sonra bu metod çağrılır
    public void scheduleNotificationForNewTodo (ToDo newTodo) {
        scheduleNotificationForTodo(newTodo);
    }

 */
}
