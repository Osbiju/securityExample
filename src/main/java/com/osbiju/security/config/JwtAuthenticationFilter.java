package com.osbiju.security.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component//to tell that the class is a management bean
@RequiredArgsConstructor //create a constructor using any final field that we declare as atribut
public class JwtAuthenticationFilter extends OncePerRequestFilter { //because wqe waNT ONCE PER EVERY REQUEST

    //create the class to extract userEmail from JwtToken
    private final JwtService jwtService;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String userEmail;
        //lets chceck if JWT exist for user
        if (authHeader == null || !authHeader.startsWith("Bearer ")){
            filterChain.doFilter(request, response);
            return; //return because we dont want to continue the execution of the rest
        }
        //lets try to extract the token of the authentication
        jwt = authHeader.substring(7); //7 because if we count "BEARER "(with space, the count is 7)

        //after checking if JWT exists, we need to call the userDetailsService to check if we have the user already in the database.
        //to do that we need to call the JWTService to extract the userName(under final string jwt)
        //extraction:
        //TODO extract userEmail from JwtToken(to do that we need a class to manipulate the JwtToken;
        userEmail = jwtService.extractUserName(jwt);

    }
}
