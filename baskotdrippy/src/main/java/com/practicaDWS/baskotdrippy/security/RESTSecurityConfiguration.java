package com.practicaDWS.baskotdrippy.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;


@Configuration
@Order(1)
public class RESTSecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    RepositoryUserDetailsService userDetailsService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception{
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception{
        http.antMatcher("/api/**");

        http.authorizeRequests().antMatchers(HttpMethod.GET, "/api/users").hasAnyRole("USER", "ADMIN");
        http.authorizeRequests().antMatchers(HttpMethod.GET, "/api/users/{username}").hasAnyRole("USER", "ADMIN");
        http.authorizeRequests().antMatchers(HttpMethod.DELETE, "/api/users/{username}").hasAnyRole("USER", "ADMIN");
        http.authorizeRequests().antMatchers(HttpMethod.POST, "/api/users").permitAll(); //"register"
        http.authorizeRequests().antMatchers(HttpMethod.PATCH, "/api/users/{username}").hasAnyRole("USER", "ADMIN");
        http.authorizeRequests().antMatchers(HttpMethod.PATCH, "/api/users/{username}/addOutfit").hasAnyRole("USER", "ADMIN");
        http.authorizeRequests().antMatchers(HttpMethod.PATCH, "/api/users/{username}/quitOutfit/{id}").hasAnyRole("USER", "ADMIN");
        http.authorizeRequests().antMatchers(HttpMethod.PATCH, "/api/users/{username}/modifyOutfit/{id}").hasAnyRole("USER", "ADMIN");
        http.authorizeRequests().antMatchers(HttpMethod.DELETE, "/api/deleteUsers").hasRole("ADMIN");


        http.authorizeRequests().antMatchers(HttpMethod.GET,"/api/outfits").permitAll();
        http.authorizeRequests().antMatchers(HttpMethod.GET, "/api/outfits/{id}").permitAll();
        http.authorizeRequests().antMatchers(HttpMethod.DELETE, "/api/outfits/{id}").hasAnyRole("USER", "ADMIN");
        http.authorizeRequests().antMatchers(HttpMethod.PATCH, "/api/outfits/{id}").hasAnyRole("USER", "ADMIN");
        http.authorizeRequests().antMatchers(HttpMethod.POST, "/api/outfits").hasAnyRole("USER", "ADMIN");
        http.authorizeRequests().antMatchers(HttpMethod.PATCH, "/api/outfits/{idOutfit}/addGarment/{id}").hasAnyRole("USER", "ADMIN");
        http.authorizeRequests().antMatchers(HttpMethod.PATCH, "/api/outfits/{idOutfit}/quitGarment/{id}").hasAnyRole("USER", "ADMIN");
        http.authorizeRequests().antMatchers(HttpMethod.DELETE, "/api/deleteOutfits").hasRole("ADMIN");


        http.authorizeRequests().antMatchers(HttpMethod.GET, "/api/garments").permitAll();
        http.authorizeRequests().antMatchers(HttpMethod.GET, "/api/garments/{id}").permitAll();
        http.authorizeRequests().antMatchers(HttpMethod.DELETE, "/api/garments/{id}").hasRole("ADMIN");
        http.authorizeRequests().antMatchers(HttpMethod.POST, "/api/garments").hasAnyRole("USER", "ADMIN");
        http.authorizeRequests().antMatchers(HttpMethod.PUT, "/api/garments/{id}").hasRole("ADMIN");
        http.authorizeRequests().antMatchers(HttpMethod.DELETE, "/api/deleteGarments").hasRole("ADMIN");


        http.httpBasic();
        http.formLogin().disable();
        http.formLogin().failureUrl("/error").disable();
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.csrf().disable();
    }


}
