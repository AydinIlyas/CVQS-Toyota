package com.toyota.apigateway.advice;


import com.toyota.apigateway.exception.UnauthorizedException;
import com.toyota.apigateway.exception.UnexpectedException;
import org.springframework.boot.autoconfigure.web.WebProperties;
import org.springframework.boot.autoconfigure.web.reactive.error.AbstractErrorWebExceptionHandler;
import org.springframework.boot.web.reactive.error.ErrorAttributes;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.*;
import reactor.core.publisher.Mono;

/**
 * Class for centralizing exceptions and handle them.
 */
@Component
@Order(-2)
public class GlobalExceptionHandler extends AbstractErrorWebExceptionHandler{

    public GlobalExceptionHandler(ErrorAttributes errorAttributes,
                                  WebProperties webProperties,
                                  ApplicationContext applicationContext) {
        super(errorAttributes, webProperties.getResources(), applicationContext);
        setMessageWriters(ServerCodecConfigurer.create().getWriters());
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
        else if(throwable instanceof UnexpectedException)
        {
            return ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR).bodyValue(
                    new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR,throwable.getMessage(), serverRequest.path()));
        }
        return ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR).bodyValue(new ErrorResponse
                (HttpStatus.INTERNAL_SERVER_ERROR,"Unexpected Exception", serverRequest.path()));
    }
}


