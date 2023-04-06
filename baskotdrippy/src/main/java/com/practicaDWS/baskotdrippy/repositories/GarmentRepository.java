package com.practicaDWS.baskotdrippy.repositories;

import com.practicaDWS.baskotdrippy.entities.Garment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GarmentRepository extends JpaRepository<Garment, Long> {
}

