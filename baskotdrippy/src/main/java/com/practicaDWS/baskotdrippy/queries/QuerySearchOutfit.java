package com.practicaDWS.baskotdrippy.queries;

import com.practicaDWS.baskotdrippy.entities.Outfit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface QuerySearchOutfit extends JpaRepository<Outfit, Long> {
    @Query(value = "select * from outfit where (outfit_name = :search) " +
            "or (description LIKE CONCAT('%', :search, '%')) or (owner = :search)", nativeQuery = true)
    List<Outfit> searchByWord(@Param("search") String search);
}
