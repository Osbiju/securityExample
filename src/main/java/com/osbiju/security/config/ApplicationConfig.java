package com.osbiju.security.config;

import com.osbiju.security.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor //por si queremos inyectar algo
public class ApplicationConfig {

    //to fecth user we need to inject the repo
    private final UserRepository userRepository;

    @Bean //to indicate that this method represents a bean, allways public
    public UserDetailsService userDetailsService(){
        return username -> userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    @Bean
    public AuthenticationProvider authenticationProvider(){ //is the data access object which is responseble to fetch the userdetails, password...

        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        //now we have to specify some properties to authProvider:
        authProvider.setUserDetailsService(userDetailsService()); //we need to tell the auth provider which user details service to use in order to fetch information about our user
        //we need to provide a pw encoder
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;


    }

    @Bean
    //is the responsible to manage the authentication, have a bunch of methods, on of them allows/help us to authenticate user based/using username and password
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
