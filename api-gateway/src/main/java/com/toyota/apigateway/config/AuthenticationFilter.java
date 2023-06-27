package com.toyota.apigateway.config;

import com.toyota.apigateway.exception.MissingBearerToken;
import com.toyota.apigateway.exception.UnauthorizedException;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.cloud.gateway.route.Route;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.util.Map;

@Component
public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config>{
    private final WebClient.Builder webClientBuilder;
    private final RouteValidator routeValidator;


    public AuthenticationFilter(WebClient.Builder webClientBuilder, RouteValidator routeValidator) {
        super(Config.class);
        this.webClientBuilder = webClientBuilder;
        this.routeValidator = routeValidator;
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            if (routeValidator.isSecured.test(exchange.getRequest())) {
                String authHeader = extractToken(exchange.getRequest());
                if (authHeader == null) {
                    throw new MissingBearerToken("Bearer Token is missing");
                }

                Route route=exchange.getAttribute
                        (org.springframework.cloud.gateway.support.ServerWebExchangeUtils.GATEWAY_ROUTE_ATTR);
                assert route != null;
                String requiredRole=route.getMetadata().get("requiredRole").toString();
                String verificationUrl = "http://verification-authorization-service/auth/verifyAndUsername";
                return webClientBuilder.build().get().uri(verificationUrl)
                        .headers(headers -> headers.setBearerAuth(authHeader))
                        .retrieve()
                        .bodyToMono(Map.class)
                        .flatMap(result -> {
                            if(result==null){
                                return Mono.error(new UnauthorizedException("User Not Found"));
                            }
                            else if (result.containsKey(requiredRole)) {
                                return chain.filter(exchange);
                            } else {
                                return Mono.error(new UnauthorizedException("User not authorized!"));
                            }
                        })
                        .onErrorResume(UnauthorizedException.class,ex-> {
                            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                            throw new UnauthorizedException("User not authorized");
                        })
                        .onErrorResume(WebClientResponseException.class,ex->{
                            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                            throw new UnauthorizedException("User not Found");
                        });

            }
            return chain.filter(exchange);
        };
    }
    private String extractToken(ServerHttpRequest request) {
        String authorizationHeader = request.getHeaders().getFirst("Authorization");
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            return authorizationHeader.substring(7);
        }
        return null;
    }


    public static class Config{
    }
}
