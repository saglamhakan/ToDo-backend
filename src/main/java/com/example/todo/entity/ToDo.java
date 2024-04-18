package com.example.todo.entity;

import com.example.todo.util.Preference;
import com.example.todo.util.Status;
import com.example.todo.util.Tag;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class ToDo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
    @Enumerated(EnumType.STRING)
    private Status status;
    @Enumerated(EnumType.STRING)
    private Tag tag;
    @Enumerated(EnumType.STRING)
    private Preference preference;
    private LocalDateTime deliveryTime;
    private LocalDateTime dateTime;
    private String message;

    @ManyToOne(fetch = FetchType.EAGER)
    @JsonBackReference
    private User user;
}
