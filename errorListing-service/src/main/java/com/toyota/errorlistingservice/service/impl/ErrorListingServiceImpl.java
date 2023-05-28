package com.toyota.errorlistingservice.service.impl;

import com.toyota.errorlistingservice.dto.TTVehicleResponse;
import com.toyota.errorlistingservice.exceptions.AttributeNotFoundException;
import com.toyota.errorlistingservice.exceptions.UnauthorizedException;
import com.toyota.errorlistingservice.service.abstracts.ErrorListingService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Service class for listing errors with filtering,sorting and paging.
 */
@Service
@RequiredArgsConstructor
public class ErrorListingServiceImpl implements ErrorListingService {
    private final WebClient.Builder webClientBuilder;

    private final Logger logger= LogManager.getLogger(ErrorListingServiceImpl.class);

    /**
     * @param request Request for send bearer token to errorLogin service
     * @param sort Attribute to determine which field to sort by.
     * @param direction Determines in which direction to sort.
     * @param page Determines which page to show.
     * @param size Determines how many objects are on one page.
     * @param attribute Determines what field you are looking for.
     * @param desiredValue The string you are looking for.
     * @return List of VehicleResponses
     */
    @Override
    public List<TTVehicleResponse> getAll(HttpServletRequest request,String sort, String direction, Integer page,
                                          Integer size, String attribute, String desiredValue) {
        String authHeader=extractToken(request);
        List<TTVehicleResponse> entities = webClientBuilder.build().get()
                .uri("http://error-login-service/ttvehicle/getAll")
                .headers(h->h.setBearerAuth(authHeader))
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<TTVehicleResponse>>() {
                })
                .block();
        if (sort != null && direction != null) {
            logger.debug("Sorting entities by sort: {}, direction: {}",sort,direction);
            sortEntities(entities, sort, direction);
        }
        if(attribute!=null&&desiredValue!=null&&entities!=null)
        {
            logger.debug("Searching for desired value: {}, attribute {}",desiredValue,attribute);
            entities=filterEntities(entities,attribute,desiredValue);
        }
        return getPage(entities, page, size);
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

    /**
     * Specifies which attribute to sort by.
     * @param entities list of VehicleResponses
     * @param sort sorting attribute
     * @param direction direction (asc/desc)
     */
    private void sortEntities(List<TTVehicleResponse> entities, String sort, String direction) {
        Comparator<TTVehicleResponse> comparator;
        if (sort.equals("name")) {
            comparator = Comparator.comparing(TTVehicleResponse::getName);
        } else if (sort.equals("introduction_date")) {
            comparator = Comparator.comparing(TTVehicleResponse::getIntroductionDate);
        } else if (sort.equals("color")) {
            comparator = Comparator.comparing(TTVehicleResponse::getColor);
        } else {
            logger.warn("ATTRIBUTE FOR SORTING INVALID: "+sort);
            throw new AttributeNotFoundException("ATTRIBUTE FOR SORTING INVALID: "+sort);
        }
        sort(entities, comparator, direction);
    }


    /**
     * Sorts entities
     * @param entities list of VehicleResponses
     * @param comparator comparator
     * @param direction direction (asc/desc)
     */
    private void sort(List<TTVehicleResponse> entities, Comparator<TTVehicleResponse> comparator, String direction) {
        Collections.sort(entities, comparator);
        if (direction.equalsIgnoreCase("desc")) {
            Collections.reverse(entities);
        }
        logger.info("Sorting completed");
    }

    /**
     * @param entities list of VehicleResponses
     * @param attribute The field you are looking for.
     * @param desired Desired String
     * @return List of TTVehicleResponse
     */
    private List<TTVehicleResponse> filterEntities(List<TTVehicleResponse> entities, String attribute, String desired) {
        List<TTVehicleResponse> filtered = new ArrayList<>();
        for (TTVehicleResponse e : entities) {
            String attributeValue = getAttributes(e,attribute);
            if (attributeValue!=null)
            {

                Pattern pattern=Pattern.compile(desired);
                Matcher matcher= pattern.matcher(attributeValue);
                if(matcher.find()){
                    filtered.add(e);
                }

            }
        }
        logger.info("Searching for desired value completed");
        return filtered;
    }

    /**
     *  Returns the attribute field you are looking for.
     * @param entity VehicleResponse
     * @param attribute Field name
     * @return Field as String
     */
    private String getAttributes(TTVehicleResponse entity, String attribute) {
        if (attribute.equals("name")) {
            return entity.getName();
        } else if (attribute.equals("introduction_date")) {
            return entity.getIntroductionDate().toString();
        } else if (attribute.equals("color")) {
            return entity.getColor();
        } else {
            logger.warn("INVALID ATTRIBUTE: "+attribute);
            throw new AttributeNotFoundException("INVALID ATTRIBUTE:"+attribute);
        }

    }

    /**
     * @param entities List of Vehicle Responses
     * @param page The page number
     * @param size Number of objects on a page.
     * @return List of Vehicle Responses
     */
    private List<TTVehicleResponse> getPage(List<TTVehicleResponse> entities, Integer page, Integer size) {
        if (page != null && size != null) {
            logger.debug("Paging started! Page: {}, Size: {}",page,size);
            int start = (page - 1) * size;
            int end = Math.min(start + size, entities.size());
            logger.info("Paging Successfully");
            return entities.subList(start, end);
        }
        logger.warn("Paging skipped");
        return entities;
    }
}

