package com.example.coffee_api;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

public record Coffee(
    Long id, 
    String name, 
    BigDecimal price, 
    boolean available, 
    OffsetDateTime createdAt
) {}
