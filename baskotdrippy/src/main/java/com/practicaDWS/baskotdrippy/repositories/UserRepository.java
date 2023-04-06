package com.practicaDWS.baskotdrippy.repositories;

import com.practicaDWS.baskotdrippy.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
}
