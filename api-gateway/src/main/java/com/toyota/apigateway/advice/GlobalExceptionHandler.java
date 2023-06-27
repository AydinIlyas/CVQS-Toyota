package com.toyota.apigateway.advice;


import com.toyota.apigateway.exception.MissingBearerToken;
import com.toyota.apigateway.exception.UnauthorizedException;
import org.springframework.boot.autoconfigure.web.WebProperties;
import org.springframework.boot.autoconfigure.web.reactive.error.AbstractErrorWebExceptionHandler;
import org.springframework.boot.web.reactive.error.ErrorAttributes;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.reactive.function.server.*;
import reactor.core.publisher.Mono;

/**
 * Class for centralizing exceptions and handle them.
 */
@Component
@Order(-2)
public class GlobalExceptionHandler extends AbstractErrorWebExceptionHandler{

    public GlobalExceptionHandler(ErrorAttributes errorAttributes, WebProperties webProperties, ApplicationContext applicationContext) {
        super(errorAttributes, webProperties.getResources(), applicationContext);
        setMessageWriters(ServerCodecConfigurer.create().getWriters());
    }

    /**
     * Handles Entity Not found custom exception
     *
     * @param ex UnauthorizedException
     * @return ResponseEntity
     */
    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<Object> handleEntityNotFound(UnauthorizedException ex) {
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.UNAUTHORIZED, ex.getMessage(), getRequestPath());
        return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
    }


    /**
     * @return Request path
     */
    private String getRequestPath() {
        return ((ServletRequestAttributes) org.springframework.web.context.request.RequestContextHolder.currentRequestAttributes())
                .getRequest().getRequestURI();
    }

    /**
     * @param errorAttributes ErrorAttributes
     * @return RouterFunction<ServerResponse>
     */
    @Override
    protected RouterFunction<ServerResponse> getRoutingFunction(ErrorAttributes errorAttributes) {
        return RouterFunctions.route(RequestPredicates.all(), this::renderErrorResponse);
    }

    private Mono<ServerResponse> renderErrorResponse(ServerRequest serverRequest) {
        Throwable throwable=getError(serverRequest);
        if(throwable instanceof UnauthorizedException)
        {
            return ServerResponse.status(HttpStatus.UNAUTHORIZED)
                    .bodyValue(new ErrorResponse(HttpStatus.UNAUTHORIZED,throwable.getMessage(),serverRequest.path()));
        }
        else if(throwable instanceof MissingBearerToken)
        {
            return ServerResponse.status(HttpStatus.UNAUTHORIZED)
                    .bodyValue(new ErrorResponse(HttpStatus.UNAUTHORIZED,throwable.getMessage(),serverRequest.path()));
        }
        return ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
}


