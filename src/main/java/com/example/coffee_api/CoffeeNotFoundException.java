package com.example.coffee_api;

import org.springframework.http.*;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class CoffeeNotFoundException extends RuntimeException{
    
    public CoffeeNotFoundException(Long id) { 
        super("Coffee not found with ID: " + id);
    }
}
