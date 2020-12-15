package de.fabiankeck.schaetzmeisterinbackendserver.security;

import io.jsonwebtoken.Claims;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Instant;
import java.util.Collections;
import java.util.Date;

@Service
public class JwtAuthFilter extends OncePerRequestFilter {


    private final JwtUtils jwtUtils;

    public JwtAuthFilter(JwtUtils jwtUtils) {
        this.jwtUtils = jwtUtils;
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authorization = request.getHeader("Authorization");

        if(authorization == null || authorization.isBlank()){
            filterChain.doFilter(request,response);
            return;
        }

        String token = authorization.replace("Bearer ","").trim();
        try {
            Claims claims = jwtUtils.parseToken(token);
            if (claims.getExpiration().after(Date.from(Instant.now()))) {
                SecurityContextHolder.getContext().setAuthentication(
                        new UsernamePasswordAuthenticationToken(claims.get("playerId",String.class),"", Collections.emptyList()));
            }
        } catch (Exception e){
            System.out.println(e);
        }
        filterChain.doFilter(request,response);
    }


}
