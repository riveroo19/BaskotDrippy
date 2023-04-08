package com.practicaDWS.baskotdrippy.queries;

import com.practicaDWS.baskotdrippy.entities.Garment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface QuerySearchGarment extends JpaRepository<Garment, Long> {
    @Query(value = "select * from garment where (garment_name = :search) " +
            "or (url LIKE CONCAT('%', :search, '%')) or (type = :search)", nativeQuery = true)
    List<Garment> searchByWord(@Param("search") String search);
}
