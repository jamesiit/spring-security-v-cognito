package org.example.backend.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.backend.service.JWTService;
import org.example.backend.service.MyUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JWTFilter extends OncePerRequestFilter {

    private JWTService jwtService;

    // using field injection is not recommended, but using constructor injection here runs a huge risk when it comes to cyclic dependencies
    @Autowired
    ApplicationContext applicationContext;

    public JWTFilter(JWTService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");
        String token = null;
        String username = null;

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            // extract the token from the header
            token = authHeader.substring(7);
            username = jwtService.extractUserName(token);
        }

        // check if username is not empty and if the thread has been authenticated before
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            //do the DB checkup of the user
            UserDetails userDetails = applicationContext.getBean(MyUserDetailsService.class).loadUserByUsername(username);

            if (jwtService.validateToken(token, userDetails)) {
                // the temporary token returns! But here we don't have username and password like the one in controller
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                // attaches session id and ip address to the thread
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                // token gets glued to the thread!
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }

        }

    }
}
