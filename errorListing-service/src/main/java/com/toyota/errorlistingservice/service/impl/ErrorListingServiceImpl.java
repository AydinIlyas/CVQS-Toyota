package com.toyota.errorlistingservice.service.impl;


import com.toyota.errorlistingservice.exceptions.UnauthorizedException;
import com.toyota.errorlistingservice.service.abstracts.ErrorListingService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.*;

/**
 * Service class for listing errors with filtering,sorting and paging.
 */
@Service
@RequiredArgsConstructor
public class ErrorListingServiceImpl implements ErrorListingService {
    private final WebClient.Builder webClientBuilder;

    private final Logger logger= LogManager.getLogger(ErrorListingServiceImpl.class);

    /**
     * Getting all vehicles with paging,filtering and sorting
     * @param request request for adding bearer tokens
     * @param model desired vehicle model
     * @param vin desired vehicle identity number
     * @param engineType desired engine type
     * @param transmissionType desired transmission type
     * @param color desired color
     * @param yearOfProduction desired year of production
     * @param page page number
     * @param size objects on a page
     * @param sortBy sorted by Field
     * @param sortOrder sort Direction
     * @return vehicle objects
     */
    @Override
    public Page<Object> getAll(HttpServletRequest request, int page, int size, String sortBy,
                                     String sortOrder, String model, String vin, String yearOfProduction,
                                     String transmissionType, String engineType, String color) {
        String authHeader=extractToken(request);
        logger.info("Request for getAll sent");
        List<Object> response = webClientBuilder.build().get()
                .uri("http://error-login-service/ttvehicle/getAll",uriBuilder ->
                        uriBuilder
                                .queryParam("sortBy",sortBy)
                                .queryParam("sortOrder",sortOrder)
                                .queryParam("model",model)
                                .queryParam("vin",vin)
                                .queryParam("yearOfProduction",yearOfProduction)
                                .queryParam("transmissionType",transmissionType)
                                .queryParam("engineType",engineType)
                                .queryParam("color",color)
                                .build()
                )
                .headers(h -> h.setBearerAuth(authHeader))
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<Object>>() {})
                .block();
        logger.info("Vehicles Successfully received!");
        int startIndex=size*page;
        int endIndex=Math.min(startIndex+size,response.size());
        PageRequest pageRequest=PageRequest.of(page,size);
        List<Object> pageContent=response.subList(startIndex,endIndex);
        Page<Object> responsePage =new PageImpl<>(pageContent,pageRequest,response.size());
        logger.info("Page: {} Size: {}, Total:{}",page,size,response.size());
        return responsePage;
    }

    /**
     * @param request For extracting the token.
     * @return  Bearer Token
     */
    private String extractToken(HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            return authorizationHeader.substring(7);
        }
        logger.warn("USER IS UNAUTHORIZED!");
        throw new UnauthorizedException("USER IS UNAUTHORIZED");
    }
}

