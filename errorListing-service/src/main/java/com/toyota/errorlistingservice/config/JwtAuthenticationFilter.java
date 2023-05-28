package com.toyota.errorlistingservice.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientException;

import java.io.IOException;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final WebClient.Builder webClientBuilder;

    private final Logger logger= LogManager.getLogger(JwtAuthenticationFilter.class);
    /**
     * @param request
     * @param response
     * @param filterChain
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {
        String authHeader = extractToken(request);
        try {
            String verificationUrl = "http://verification-authorization-service//auth/verify";
            if(authHeader==null)
            {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED,"NO BEARER TOKEN FOUND");
                return;
            }
            Set<String> permissions = webClientBuilder.build().get().uri(verificationUrl)
                    .headers(h -> h.setBearerAuth(authHeader))
                    .retrieve()
                    .bodyToMono(Set.class)
                    .block();
            if(permissions==null)
            {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED,"NO PERMISSIONS FOUND");
                return;
            }
            if (permissions.contains("LEADER")) {
                logger.info("AUTHORIZED SUCCESSFULLY");
                filterChain.doFilter(request, response);
            } else {
                logger.warn("USER NOT AUTHORIZED");
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "USER NOT AUTHORIZED");
            }
        } catch (WebClientException ex) {
            logger.warn("USER NOT FOUND");
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "USER DOES NOT EXIST");
        }
    }

    private String extractToken(HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            return authorizationHeader.substring(7);
        }
        return null;
    }
}
