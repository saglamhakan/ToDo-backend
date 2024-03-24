package com.example.todo.service;

import com.example.todo.dto.request.TodoRequest;
import com.example.todo.dto.response.ToDoResponse;
import com.example.todo.entity.ToDo;
import com.example.todo.entity.User;
import com.example.todo.exception.BusinessException;
import com.example.todo.repository.ToDoRepository;
import com.example.todo.repository.UserRepository;
import com.example.todo.util.Status;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class ToDoService {

    private final ToDoRepository toDoRepository;

    private final UserRepository userRepository;

    public ToDoService(ToDoRepository toDoRepository, UserRepository userRepository) {
        this.toDoRepository = toDoRepository;
        this.userRepository = userRepository;
    }

    public List<ToDoResponse> getAll(){
        List<ToDoResponse> toDoResponses = new ArrayList<>();
        List<ToDo> toDoList = toDoRepository.findAll();

        for (ToDo toDo: toDoList) {
            ToDoResponse toDoResponse = new ToDoResponse();
            toDoResponse.setName(toDo.getName());
            toDoResponse.setDescription(toDo.getDescription());
            toDoResponse.setStatus(toDo.getStatus());
            toDoResponse.setDateTime(toDo.getDateTime());
            toDoResponse.setTag(toDo.getTag());
            toDoResponse.setPreference(toDo.getPreference());
            toDoResponse.setDeliveryTime(toDo.getDeliveryTime());

            toDoResponses.add(toDoResponse);

        }
        return toDoResponses;
    }

    public ToDoResponse create(TodoRequest request){
        ToDo toDo = new ToDo();

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found with id: " + request.getUserId()));


        toDo.setName(request.getName());
        toDo.setDescription(request.getDescription());
        toDo.setStatus(request.getStatus());
        toDo.setDateTime(request.getDateTime());
        toDo.setTag(request.getTag());
        toDo.setPreference(request.getPreference());
        toDo.setDeliveryTime(request.getDeliveryTime());
      //  toDo.setMessage(request.getMessage());
        toDo.setUser(user);

        ToDo savedTodo = toDoRepository.save(toDo);

         return convertToToDoResponse(savedTodo);

    }

    public void delete(Long id){
        ToDo toDo = toDoRepository.findById(id).orElse(null);

        if (toDo != null){
            toDoRepository.deleteById(id);
        }else {
            throw new BusinessException("ToDo you wanted to delete was not found");
        }

    }

    public ToDoResponse update(Long id, TodoRequest request){
        ToDo toDo = toDoRepository.findById(id).orElse(null);

        if (toDo != null){
            toDo.setName(request.getName());
            toDo.setDescription(request.getDescription());
            toDo.setStatus(request.getStatus());
            toDo.setDateTime(request.getDateTime());
            toDo.setTag(request.getTag());
            toDo.setPreference(request.getPreference());
            toDo.setDeliveryTime(request.getDeliveryTime());

            ToDo savedTodo = toDoRepository.save(toDo);

            return convertToToDoResponse(savedTodo);
        }else {
            throw new BusinessException("ToDo you wanted to update was not found");
        }

    }

    private ToDoResponse convertToToDoResponse(ToDo toDo){
        ToDoResponse response = new ToDoResponse();
        response.setName(toDo.getName());
        response.setDescription(toDo.getDescription());
        response.setStatus(toDo.getStatus());
        response.setDateTime(toDo.getDateTime());
        response.setTag(toDo.getTag());
        response.setPreference(toDo.getPreference());
        response.setDeliveryTime(toDo.getDeliveryTime());

        return response;
    }

    @Scheduled(cron = "0 38 19 * * ?") // Her gün gece yarısı çalıştır
    public void updateMessageForDueTodos() {
        // Şu anki zamanı al
        LocalDateTime now = LocalDateTime.now();

        // Tüm NOT_COMPLETED ve deliveryTime'ı geçmiş ToDo'ları bul
        List<ToDo> dueTodos = toDoRepository.findAll().stream()
                .filter(todo -> todo.getStatus() == Status.NOT_COMPLETED && todo.getDeliveryTime().isBefore(now))
                .toList();

        // Her bir ToDo için işlem yap
        for (ToDo todo : dueTodos) {
            // İlişkili User'ın e-mail adresini kullanarak bir mesaj yaz
            String message = "Merhaba, ToDo'nuz (" + todo.getName() + ") için teslim zamanı geçmiştir. Lütfen kontrol ediniz.";
            todo.setMessage(message);

            // Güncellenmiş ToDo'yu kaydet
            toDoRepository.save(todo);
        }
    }

}
