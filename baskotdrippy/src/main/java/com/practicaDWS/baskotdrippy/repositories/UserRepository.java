package com.practicaDWS.baskotdrippy.repositories;

import com.practicaDWS.baskotdrippy.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findByUsername(String username);
    void deleteAllByRolesNotContaining(String role);
}
