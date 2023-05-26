package com.toyota.errorloginservice.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final WebClient.Builder webClientBuilder;
    /**
     * @param request
     * @param response
     * @param filterChain
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String authHeader=extractToken(request);
        try {
            String verificationUrl = "http://verification-authorization-service//auth/verify"; // Replace with your verification microservice URL

            Set<String> permissions=webClientBuilder.build().get().uri(verificationUrl)
                    .headers(h->h.setBearerAuth(authHeader))
                    .retrieve()
                            .bodyToMono(Set.class)
                                    .block();

            // 3. If the verification is successful, proceed with the request
            if(permissions.contains("OPERATOR"))
            filterChain.doFilter(request, response);
            else{
                throw new HttpClientErrorException(HttpStatus.UNAUTHORIZED);
            }
        } catch (HttpClientErrorException ex) {
            // 4. Handle cases where the verification microservice returns an error (e.g., invalid token)
            if (ex.getStatusCode() == HttpStatus.UNAUTHORIZED) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            } else {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            }
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
