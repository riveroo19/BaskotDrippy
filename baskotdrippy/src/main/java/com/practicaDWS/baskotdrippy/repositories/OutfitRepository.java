package com.practicaDWS.baskotdrippy.repositories;

import com.practicaDWS.baskotdrippy.entities.Outfit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OutfitRepository extends JpaRepository<Outfit, Long> {
}
