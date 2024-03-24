package com.example.todo.service;

import com.example.todo.dto.request.UserRequest;
import com.example.todo.dto.response.ToDoResponse;
import com.example.todo.dto.response.UserResponse;
import com.example.todo.entity.ToDo;
import com.example.todo.entity.User;
import com.example.todo.repository.ToDoRepository;
import com.example.todo.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;

    private final ToDoRepository toDoRepository;

    public UserService(UserRepository userRepository, ToDoRepository toDoRepository) {
        this.userRepository = userRepository;
        this.toDoRepository = toDoRepository;
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
    public UserResponse create(UserRequest request){

        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());

        User savedUser = userRepository.save(user);

        return convertToToDoResponse(savedUser);

    }

    public UserResponse convertToToDoResponse(User user){
        UserResponse response = new UserResponse();
        response.setName(user.getName());
        response.setEmail(user.getEmail());


        return response;
    }
}


