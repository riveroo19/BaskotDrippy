package com.practicaDWS.baskotdrippy.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.security.SecureRandom;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    RepositoryUserDetailsService userDetailsService;

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder(10, new SecureRandom());
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }


    @Override
    public void configure (HttpSecurity http) throws Exception{
        //users
        http.authorizeRequests().antMatchers("/login").permitAll();
        http.authorizeRequests().antMatchers("/register").permitAll();
        http.authorizeRequests().antMatchers("/search").hasAnyRole("USER", "ADMIN");
        http.authorizeRequests().antMatchers("/error").permitAll();
        http.authorizeRequests().antMatchers("/").permitAll();
        http.authorizeRequests().antMatchers("/home").permitAll();
        http.authorizeRequests().antMatchers("/team.html").permitAll();
        http.authorizeRequests().antMatchers("/searchUsers").hasAnyRole("USER", "ADMIN");
        http.authorizeRequests().antMatchers("/users").hasAnyRole("USER", "ADMIN");
        http.authorizeRequests().antMatchers("/users/{username}").hasAnyRole("USER", "ADMIN");
        http.authorizeRequests().antMatchers("/users/deleteUser/{username}").hasAnyRole("USER", "ADMIN");
        http.authorizeRequests().antMatchers("/users/updateUser/{username}").hasAnyRole("USER", "ADMIN");
        http.authorizeRequests().antMatchers("/users/modifySuccess").hasAnyRole("USER", "ADMIN");
        http.authorizeRequests().antMatchers("/users/{username}/createOutfit").hasAnyRole("USER", "ADMIN");
        http.authorizeRequests().antMatchers("/deleteUsers").hasRole("ADMIN");

        //outfits
        http.authorizeRequests().antMatchers("/outfits").permitAll();
        http.authorizeRequests().antMatchers("/outfits/{id}").permitAll();
        http.authorizeRequests().antMatchers("/searchOutfits").permitAll();
        http.authorizeRequests().antMatchers("/outfits/deleteGarment/{id}").hasAnyRole("USER", "ADMIN");
        http.authorizeRequests().antMatchers("/outfits/createOutfit").hasAnyRole("USER", "ADMIN");
        http.authorizeRequests().antMatchers("/outfits/updateOutfit/{id}").hasAnyRole("USER", "ADMIN");
        http.authorizeRequests().antMatchers("/outfits/modifySuccess").hasAnyRole("USER", "ADMIN");
        http.authorizeRequests().antMatchers("/outfits/{idOutfit}/quitGarment/{id}").hasAnyRole("USER", "ADMIN");
        http.authorizeRequests().antMatchers("/outfits/{idOutfit}/addGarment").hasAnyRole("USER", "ADMIN");
        http.authorizeRequests().antMatchers("/outfits/{idOutfit}/{id}/success").hasAnyRole("USER", "ADMIN");
        http.authorizeRequests().antMatchers("/deleteOutfits").hasRole("ADMIN");
        //garments
        http.authorizeRequests().antMatchers("/garments").permitAll();
        http.authorizeRequests().antMatchers("/searchGarment").permitAll();
        http.authorizeRequests().antMatchers("/garments/{id}").permitAll();
        http.authorizeRequests().antMatchers("/garments/deleteGarment/{id}").hasRole("ADMIN");
        http.authorizeRequests().antMatchers("/garments/createGarment").hasAnyRole("USER", "ADMIN");
        http.authorizeRequests().antMatchers("/garments/updateGarment/{id}").hasRole("ADMIN");
        http.authorizeRequests().antMatchers("/garments/modifySuccess").hasAnyRole("ADMIN");
        http.authorizeRequests().antMatchers("/deleteGarments").hasRole("ADMIN");

        http.authorizeRequests().antMatchers("/admin").hasRole("ADMIN");
        http.authorizeRequests().antMatchers("/assets/css/**").permitAll();
        http.authorizeRequests().antMatchers("/assets/bootstrap/**").permitAll();
        http.authorizeRequests().antMatchers("/assets/fonts/**").permitAll();
        http.authorizeRequests().antMatchers("/assets/img/**").permitAll();
        http.authorizeRequests().antMatchers("/assets/js/**").permitAll();
        http.authorizeRequests().anyRequest().authenticated();

        http.formLogin().loginPage("/login");
        http.formLogin().usernameParameter("username");
        http.formLogin().passwordParameter("password");
        http.formLogin().defaultSuccessUrl("/home");
        http.formLogin().failureUrl("/error");
        http.logout().logoutUrl("/logout");
        http.logout().logoutSuccessUrl("/home");


        //http.csrf().disable();

    }


}
