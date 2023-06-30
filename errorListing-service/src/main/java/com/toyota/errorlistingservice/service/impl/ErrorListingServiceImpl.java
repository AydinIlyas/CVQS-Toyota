package com.toyota.errorlistingservice.service.impl;


import com.toyota.errorlistingservice.dto.PaginationResponse;
import com.toyota.errorlistingservice.exceptions.BearerTokenNotFoundException;
import com.toyota.errorlistingservice.service.abstracts.ErrorListingService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.core.ParameterizedTypeReference;
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
    public PaginationResponse<Object> getVehicles(HttpServletRequest request, int page, int size, List<String> sortBy,
                                    String sortOrder, String model, String vin, String yearOfProduction,
                                    String transmissionType, String engineType, String color) {
        String authHeader=extractToken(request);
        logger.info("Request for getAll sent");
        PaginationResponse<Object> response = webClientBuilder.build().get()
                .uri("http://error-login-service/ttvehicle/getAll",uriBuilder ->
                        uriBuilder
                                .queryParam("page",page)
                                .queryParam("size",size)
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
                .bodyToMono(new ParameterizedTypeReference<PaginationResponse<Object>>() {})
                .block();
        logger.info("Vehicles Successfully received!");
        if(response!=null)
            logger.info("Page: {} Size: {}, Total:{}",page,size,response.getPageable().getTotalElements());
        return response;
    }

    /**
     * Getting vehicles with paging, filtering and sorting. Sends request to errorLogin.
     * @param request request for sending token to errorLogin
     * @param page page Number starts from 0
     * @param size entity amount on a page
     * @param type desired type of defect
     * @param state desired state of defect
     * @param reportTime desired reportTime of defect
     * @param reportedBy desired reporter
     * @param vin desired vehicle id number
     * @param sortOrder desired sorting direction ASC,DESC
     * @param sortBy ordered by fields
     * @return Custom paging response
     */
    @Override
    public PaginationResponse<Object> getDefects(HttpServletRequest request,int page, int size,String type, String state,
                                   String reportTime, String reportedBy, String vin,
                                   String sortOrder, String sortBy) {
        String authHeader=extractToken(request);
        logger.info("Request for getAll sent");
        PaginationResponse<Object> response = webClientBuilder.build().get()
                .uri("http://error-login-service/ttvehicleDefect/getAll",uriBuilder ->
                        uriBuilder
                                .queryParam("page",page)
                                .queryParam("size",size)
                                .queryParam("sortBy",sortBy)
                                .queryParam("sortOrder",sortOrder)
                                .queryParam("type",type)
                                .queryParam("state",state)
                                .queryParam("reportTime",reportTime)
                                .queryParam("reportedBy",reportedBy)
                                .queryParam("vin",vin)
                                .build()
                )
                .headers(h -> h.setBearerAuth(authHeader))
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<PaginationResponse<Object>>() {})
                .block();
        logger.info("Vehicles Successfully received!");
        if(response!=null)
            logger.info("Page: {} Size: {}, Total:{}",page,size,response.getPageable().getTotalElements());
        return response;
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
        throw new BearerTokenNotFoundException("USER IS UNAUTHORIZED");
    }
}

