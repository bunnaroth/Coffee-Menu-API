package com.example.coffee_api.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.coffee_api.Coffee;
import com.example.coffee_api.CoffeeNotFoundException;
import com.example.coffee_api.repository.CoffeeRepository;

@Service 
public class CoffeeService {
    
    private final CoffeeRepository coffeeRepository; 


    public CoffeeService(CoffeeRepository coffeeRepository) { 
        this.coffeeRepository = coffeeRepository;
    }

    public List<Coffee> findAll() { 
        return coffeeRepository.findAll(); 
    }

    public Coffee findById(Long id) { 
        return coffeeRepository.findById(id).
                orElseThrow(() -> new CoffeeNotFoundException(id)); 
    }

    @Transactional 
    public Coffee create(String name, BigDecimal price, boolean available) { 
        validate(name, price); 

        return coffeeRepository.save(name.trim(), price, available);
    }

    @Transactional 
    public Coffee update(Long id, String name, BigDecimal price, boolean available) { 
        validate(name, price);

        boolean updated = coffeeRepository.update(id, name.trim(), price, available);

        if (!updated) { 
            throw new CoffeeNotFoundException(id);
        }

        return findById(id);
    }

    @Transactional 
    public void delete(Long id) { 
        boolean deleted = coffeeRepository.deleteById(id); 

        if (!deleted) { 
            throw new CoffeeNotFoundException(id);
        }
    }

    public void validate(String name, BigDecimal price) { 
        if (name == null || name.isBlank()) { 
            throw new IllegalArgumentException(
                "Coffee name is required."
            );
        }

        if (price == null || price.signum() < 0) { 
            throw new IllegalArgumentException( 
                "Price is required"
            ); 
        }
    }
}
