package com.toyota.apigateway.config;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Predicate;

/**
 * RouteValidator checks if the endpoint is secured or not
 */
@Component
public class RouteValidator {
    public static final List<String> openApiEndpoints = List.of(
            "/auth/login","/auth/changePassword","/auth/logout"
    );

    public Predicate<ServerHttpRequest> isSecured =
            request -> openApiEndpoints
                    .stream()
                    .noneMatch(uri -> request.getURI().getPath().contains(uri));
}
