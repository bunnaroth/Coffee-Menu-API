package com.example.coffee_api.repository;

import org.springframework.jdbc.core.RowMapper;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.example.coffee_api.Coffee;

@Repository 
public class CoffeeRepository {
    
    private final JdbcTemplate jdbcTemplate; 

    public CoffeeRepository(JdbcTemplate jdbcTemplate) { 
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<Coffee> coffeeRowMapper =
            (rs, rowNum) -> new Coffee(
                    rs.getLong("id"),
                    rs.getString("name"),
                    rs.getBigDecimal("price"),
                    rs.getBoolean("is_available"),
                    rs.getObject(
                            "created_at",
                            java.time.OffsetDateTime.class
                    )
            );

    public List<Coffee> findAll() { 
        String sql = """
            SELECT id, name, price, is_available, created_at
            FROM coffees
            ORDER BY id
        """;

        return jdbcTemplate.query(sql, coffeeRowMapper);
    }
            
    public Optional<Coffee> findById(Long id) { 
        String sql = """
            SELECT id, name, price, is_available, created_at 
            FROM coffees
            WHERE id = ?
        """;

        List<Coffee> coffees = jdbcTemplate.query(sql, coffeeRowMapper, id); 

        return coffees.stream().findFirst(); 
    }

    public Coffee save(String name, BigDecimal price, boolean available) { 
        String sql = """
            INSERT INTO coffees (name, price, is_available)
            VALUES (?, ?, ?)
            RETURNING  id, name, price, is_available, created_at
        """;

        return jdbcTemplate.queryForObject(sql, coffeeRowMapper, name, price, available); 
    }

    public boolean update(Long id, String name, BigDecimal price, boolean available) { 
        String sql = """
            UPDATE coffees 
            SET name = ?, price = ?, is_available = ? 
            WHERE id = ? 
        """;

        return jdbcTemplate.update(sql, name, price, available, id) > 0; 
    }

    public boolean deleteById(Long id) { 
        String sql = "DELETE FROM coffees WHERE id = ?"; 

        return jdbcTemplate.update(sql, id) > 0;
    }
}
