package com.example.todo.service;

import com.example.todo.dto.request.UserRequest;
import com.example.todo.dto.response.ToDoResponse;
import com.example.todo.dto.response.UserResponse;
import com.example.todo.entity.ToDo;
import com.example.todo.entity.User;
import com.example.todo.exception.BusinessException;
import com.example.todo.repository.ToDoRepository;
import com.example.todo.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class UserService {
    private final UserRepository userRepository;


    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<UserResponse> getAll() {
        List<UserResponse> userResponses = new ArrayList<>();
        List<User> userList = userRepository.findAll();

        for (User user : userList) {
            UserResponse userResponse = new UserResponse();
            userResponse.setName(user.getName());
            userResponse.setEmail(user.getEmail());
            userResponse.setTodos(user.getTodos());


            userResponses.add(userResponse);

        }

        return userResponses;

    }

    public UserResponse create(UserRequest request) {

        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());

        User savedUser = userRepository.save(user);

        return convertToToDoResponse(savedUser);

    }

    public void deleteById(Long id) {
        User user = userRepository.findById(id).orElse(null);
        if (user != null) {
            userRepository.deleteById(id);
        } else {
            throw new BusinessException("User you wanted to delete was not found");
        }
    }


    public UserResponse updateToDoFields(Long id, Map<String, Object> updates) {
        User user = userRepository.findById(id).orElseThrow(() -> new BusinessException("Görev bulunamadı: " + id));

        updates.forEach((key, value) -> {
            Field field = ReflectionUtils.findField(User.class, key); //Todo nun içindeki fieldları buluyor
            if (field != null) {
                field.setAccessible(true);
                ReflectionUtils.setField(field, user, value);

            } else {
                throw new BusinessException("Güncellenmek istenen alan bulunamadı: " + key);
            }
        });

        User updatedUser = userRepository.save(user);
        return convertToToDoResponse(updatedUser);
    }


    public UserResponse convertToToDoResponse(User user) {
        UserResponse response = new UserResponse();
        response.setName(user.getName());
        response.setEmail(user.getEmail());

        return response;
    }
}


