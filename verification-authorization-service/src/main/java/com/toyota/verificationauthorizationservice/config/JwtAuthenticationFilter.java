package com.toyota.verificationauthorizationservice.config;

import com.toyota.verificationauthorizationservice.service.abstracts.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final UserDetailsService userDetailsService;

    private final JwtService jwtService;
    private final Logger logger= LogManager.getLogger(JwtAuthenticationFilter.class);
    /**
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @param filterChain FilterChain
     * @throws ServletException ServletException
     * @throws IOException IOException
     */
    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {
        final String authHeader=request.getHeader("Authorization");
        final String jwt;
        final String username;

        if(authHeader==null||!authHeader.startsWith("Bearer "))
        {
            logger.info("BEARER IS NULL OR INVALID");
            filterChain.doFilter(request,response);
            return;
        }
        jwt=authHeader.substring(7);
        username=jwtService.extractUsername(jwt);

        if(username!=null && SecurityContextHolder.getContext().getAuthentication()==null)
        {
            logger.debug("EXTRACTED USERNAME: {}",username);
            UserDetails userDetails=this.userDetailsService.loadUserByUsername(username);

            if(jwtService.isTokenValid(jwt,userDetails))
            {
                UsernamePasswordAuthenticationToken authToken=new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );
                authToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
        filterChain.doFilter(request,response);
    }
}
