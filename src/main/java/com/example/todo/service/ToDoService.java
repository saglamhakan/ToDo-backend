package com.example.todo.service;

import com.example.todo.dto.request.TodoRequest;
import com.example.todo.dto.response.ToDoResponse;
import com.example.todo.entity.ToDo;
import com.example.todo.entity.User;
import com.example.todo.exception.BusinessException;
import com.example.todo.repository.ToDoRepository;
import com.example.todo.repository.UserRepository;
import com.example.todo.util.Preference;
import com.example.todo.util.Status;
import com.example.todo.util.Tag;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ReflectionUtils;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ToDoService {

    private static final Logger logger = LoggerFactory.getLogger(ToDoService.class);

    private final ToDoRepository toDoRepository;

    private final UserRepository userRepository;

    public ToDoService(ToDoRepository toDoRepository, UserRepository userRepository) {
        this.toDoRepository = toDoRepository;
        this.userRepository = userRepository;
    }

    public List<ToDoResponse> getAll() {
        List<ToDoResponse> toDoResponses = new ArrayList<>();
        List<ToDo> toDoList = toDoRepository.findAll();

        for (ToDo toDo : toDoList) {
            ToDoResponse toDoResponse = new ToDoResponse();
            toDoResponse.setId(toDo.getId());
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

    public ToDoResponse create(TodoRequest request) {
        ToDo toDo = new ToDo();

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new BusinessException("User not found with id: " + request.getUserId()));

        toDo.setName(request.getName());
        toDo.setDescription(request.getDescription());
        toDo.setStatus(request.getStatus());
        toDo.setDateTime(request.getDateTime());
        toDo.setTag(request.getTag());
        toDo.setPreference(request.getPreference());
        toDo.setDeliveryTime(request.getDeliveryTime());
        toDo.setUser(user);

        ToDo savedTodo = toDoRepository.save(toDo);

        return convertToToDoResponse(savedTodo);

    }


    public void delete(Long id) {
        ToDo toDo = toDoRepository.findById(id).orElse(null);

        if (toDo != null) {
            toDoRepository.deleteById(id);
        } else {
            throw new BusinessException("ToDo you wanted to delete was not found");
        }

    }

    public ToDoResponse update(Long id, TodoRequest request) {
        ToDo toDo = toDoRepository.findById(id).orElse(null);

        if (toDo != null) {
            toDo.setName(request.getName());
            toDo.setDescription(request.getDescription());
            toDo.setStatus(request.getStatus());
            toDo.setDateTime(request.getDateTime());
            toDo.setTag(request.getTag());
            toDo.setPreference(request.getPreference());
            toDo.setDeliveryTime(request.getDeliveryTime());

            ToDo savedTodo = toDoRepository.save(toDo);

            return convertToToDoResponse(savedTodo);
        } else {
            throw new BusinessException("ToDo you wanted to update was not found");
        }

    }

    public ToDoResponse updateToDoFields(Long id, Map<String, Object> updates) {
        ToDo toDo = toDoRepository.findById(id).orElseThrow(() -> new BusinessException("Görev bulunamadı: " + id));

        updates.forEach((key, value) -> {
            Field field = ReflectionUtils.findField(ToDo.class, key); //Todo nun içindeki fieldları buluyor
            if (field != null) {
                field.setAccessible(true);
                if (field.getType().isEnum()) {

                    @SuppressWarnings("unchecked")
                    Object enumValue = EnumConverter.findEnumByName((Class<Enum>) field.getType(), (String) value);
                    ReflectionUtils.setField(field, toDo, enumValue);

                } else {
                    ReflectionUtils.setField(field, toDo, value);
                }
            } else {
                throw new BusinessException("Güncellenmek istenen alan bulunamadı: " + key);
            }
        });

        ToDo updatedToDo = toDoRepository.save(toDo);
        return convertToToDoResponse(updatedToDo);
    }

    private ToDoResponse convertToToDoResponse(ToDo toDo) {
        ToDoResponse response = new ToDoResponse();
        response.setId(toDo.getId());
        response.setName(toDo.getName());
        response.setDescription(toDo.getDescription());
        response.setStatus(toDo.getStatus());
        response.setDateTime(toDo.getDateTime());
        response.setTag(toDo.getTag());
        response.setPreference(toDo.getPreference());
        response.setDeliveryTime(toDo.getDeliveryTime());

        return response;

    }

    @Scheduled(cron = "0 15 15 * * ?")
    public void sendMessage() {
        LocalDateTime now = LocalDateTime.now();

        List<ToDo> dueTodos = toDoRepository.findAll();
        for (ToDo todo : dueTodos) {
            if (todo.getStatus() == Status.NOT_COMPLETED && todo.getDeliveryTime().isBefore(now)) {
                String message = "Merhaba, ToDo'nuz (" + todo.getName() + ") için teslim zamanı geçmiştir. Lütfen kontrol ediniz.";
                todo.setMessage(message);
                logger.info("Kullanıcıya hatırlatma yapıldı");
                toDoRepository.save(todo);
            }

        }
    }

    public Long getCompletedToDosCount(Long userId, Status status, LocalDate startDate, LocalDate endDate) {
        return toDoRepository.countByUserIdAndStatusAndDateTimeBetween(userId, status, startDate.atStartOfDay(), endDate.atStartOfDay());
    }

    public List<ToDo> getCompletedToDosName(Long userId, Status status, LocalDate startDate, LocalDate endDate) {
        return toDoRepository.findToDoNameByUserIdAndStatusAndDateTimeBetween(userId, status, startDate.atStartOfDay(), endDate.atStartOfDay());
    }

    public String percentToDoCompleted() {
        List<ToDo> toDo = toDoRepository.findAll().stream().filter(t -> t.getStatus() == Status.COMPLETED).toList();
        List<ToDo> toDo1 = toDoRepository.findAll();
        long countCompleted = toDo.size();
        long countTotal = toDo1.size();

        if (countCompleted == 0) {
            logger.warn("No completed ToDos found");
            throw new BusinessException("Tamamladığınız görev bulunmamakta");
        } else {
            double percent = ((double) countCompleted / countTotal) * 100.0;
            logger.info("Percentage of ToDos completed:");
            return String.format("%.2f%%", percent);
        }
    }

    public List<ToDo> filterAndSortToDos(Long userId, Status status, Tag tag, LocalDate date, Preference preference, String sortOrder) {
        userRepository.findById(userId).orElseThrow(() -> new BusinessException("Böyle bir kullanıcı bulunamadı"));


        return toDoRepository.findAll().stream()
                .filter(t -> (status == null || t.getStatus() == status) &&
                        (date == null || t.getDateTime().equals(date)) &&
                        (preference == null || t.getPreference() == preference) &&
                        (tag == null || t.getTag() == tag))
                .sorted(getComparator(sortOrder))
                .collect(Collectors.toList());

    }

    private Comparator<ToDo> getComparator(String sortOrder) {
        switch (sortOrder) {
            case "date":
                return Comparator.comparing(ToDo::getDateTime);
            case "preference":
                return Comparator.comparing(ToDo::getPreference);
            case "tag":
                return Comparator.comparing(ToDo::getTag);
            default:
                return Comparator.comparing(ToDo::getStatus);
        }

    }
}















/*
    @Scheduled(cron = "0 38 19 * * ?") // Her gün gece yarısı çalıştır
    public void updateMessageForDueTodos() {

        LocalDateTime now = LocalDateTime.now();


        List<ToDo> dueTodos = toDoRepository.findAll().stream()
                .filter(todo -> todo.getStatus() == Status.NOT_COMPLETED && todo.getDeliveryTime().isBefore(now))
                .toList();


        for (ToDo todo : dueTodos) {
            String message = "Merhaba, ToDo'nuz (" + todo.getName() + ") için teslim zamanı geçmiştir. Lütfen kontrol ediniz.";
            todo.setMessage(message);

            toDoRepository.save(todo);
        }
    }

 */


