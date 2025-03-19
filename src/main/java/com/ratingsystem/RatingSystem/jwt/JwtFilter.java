package com.ratingsystem.RatingSystem.jwt;

import com.ratingsystem.RatingSystem.service.authenticationService.MyUserDetailService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtility jwtUtility;
    private final MyUserDetailService myUserDetailService;

    @Autowired
    public JwtFilter(JwtUtility jwtUtility, MyUserDetailService myUserDetailService) {
        this.jwtUtility = jwtUtility;
        this.myUserDetailService = myUserDetailService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String header = request.getHeader("Authorization");

        if (header == null || !header.startsWith("Bearer ")) {
            filterChain.doFilter(request,response);
            return;
        }

            String token = header.substring(7);
            String userEmail = jwtUtility.getUserEmail(token);

            if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                try {
                    UserDetails userDetails = myUserDetailService.loadUserByUsername(userEmail);

                    UsernamePasswordAuthenticationToken authenticationToken =
                            new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);

                } catch (UsernameNotFoundException e) {
                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "User not found");

                }catch (BadCredentialsException e){
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Bad credentials !");
                }
            }

        filterChain.doFilter(request, response);
    }
}
