package com.example.todo.service;

import com.example.todo.exception.BusinessException;
import org.springframework.stereotype.Component;

@Component
public class EnumConverter{

    public static <E extends Enum<E>> E findEnumByName(Class<E> enumClass, String name) {
        for (E e : enumClass.getEnumConstants()) {
            if (e.name().equals(name)) {
                return e;
            }
        }
        throw new  BusinessException("Bu isimde bir güncelleme yapamazsınız");
    }
}

