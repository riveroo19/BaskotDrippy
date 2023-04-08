package com.practicaDWS.baskotdrippy.queries;

import com.practicaDWS.baskotdrippy.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface QuerySearchUser extends JpaRepository<User, String> {

    @Query(value = "select * from user where (username LIKE CONCAT('%', :search, '%')) " +
            "or (fullname LIKE CONCAT('%', :search, '%')) or (bio LIKE CONCAT('%', :search, '%'))", nativeQuery = true)
    List<User> searchByWord(@Param("search") String search);

}
