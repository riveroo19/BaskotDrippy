package com.practicaDWS.baskotdrippy.security;

import com.practicaDWS.baskotdrippy.entities.User;
import com.practicaDWS.baskotdrippy.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Component
public class DatabaseUsersLoader {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostConstruct
    private void init(){

        List<String> adminRoles = new ArrayList<>();
        adminRoles.add("ADMIN");
        adminRoles.add("USER");
        userRepository.save(new User("admin", "administrador", "administrar", passwordEncoder.encode("secure!pass"), "admin@tech.org", adminRoles));

    }


}
