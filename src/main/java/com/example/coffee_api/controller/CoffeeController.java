package com.example.coffee_api.controller;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import com.example.coffee_api.Coffee;
import com.example.coffee_api.service.CoffeeService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;


@RestController 
@RequestMapping("api/coffees")
public class CoffeeController {
    
    private final CoffeeService coffeeService; 

    public CoffeeController(CoffeeService coffeeService) { 
        this.coffeeService = coffeeService; 
    }

    public record CoffeeRequest( 
        @NotNull 
        @Size(max = 100)
        String name, 

        @NotNull 
        @DecimalMin("0.00")
        BigDecimal price, 

        boolean available
    ) {}

    public record CoffeeResponse(
        Long id, 
        String name,
        BigDecimal price, 
        boolean available, 
        OffsetDateTime createdAt
    ){
        public static CoffeeResponse from(Coffee coffee) { 
            return new CoffeeResponse(
                coffee.id(), 
                coffee.name(), 
                coffee.price(), 
                coffee.available(), 
                coffee.createdAt()
            );
        }
    }

    @GetMapping 
    public List<CoffeeResponse> getAll() { 
        return coffeeService.findAll().stream().map(CoffeeResponse::from).toList();
    }

    @GetMapping("/{id}")
    public CoffeeResponse getById(@PathVariable Long id) {
        return CoffeeResponse.from(
            coffeeService.findById(id)
        );
    }
    
    @PostMapping 
    @ResponseStatus(HttpStatus.CREATED)
    public CoffeeResponse create(@Valid  @RequestBody CoffeeRequest request) { 
        Coffee coffee = coffeeService.create(request.name(), request.price(), request.available()); 

        return CoffeeResponse.from(coffee);
    }

    @PutMapping("/{id}")
    public CoffeeResponse update(
        @PathVariable Long id, 
        @Valid @RequestBody CoffeeRequest request
    ) { 
        Coffee coffee = coffeeService.update(
            id,
            request.name(), 
            request.price(), 
            request.available()
        );
        
        return CoffeeResponse.from(coffee);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) { 
        coffeeService.delete(id);
    }


}
