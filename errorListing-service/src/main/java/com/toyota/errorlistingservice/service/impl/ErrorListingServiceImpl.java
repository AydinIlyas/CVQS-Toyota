package com.toyota.errorlistingservice.service.impl;

import com.toyota.errorlistingservice.dto.TTVehicleResponse;
import com.toyota.errorlistingservice.service.abstracts.ErrorListingService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class ErrorListingServiceImpl implements ErrorListingService {
    private final WebClient.Builder webClientBuilder;

    /**
     * @param sort
     * @param direction
     * @param page
     * @param size
     * @param attribute
     * @return
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
            sortEntities(entities, sort, direction);
        }
        if(attribute!=null&&desiredValue!=null)
        {
            entities=filterEntities(entities,attribute,desiredValue);
        }
        return getPage(entities, page, size);
    }
    private String extractToken(HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            return authorizationHeader.substring(7);
        }
        return null;
    }
    private void sortEntities(List<TTVehicleResponse> entities, String sort, String direction) {
        Comparator<TTVehicleResponse> comparator;
        if (sort.equals("name")) {
            comparator = Comparator.comparing(TTVehicleResponse::getName);
        } else if (sort.equals("introduction_date")) {
            comparator = Comparator.comparing(TTVehicleResponse::getIntroductionDate);
        } else if (sort.equals("color")) {
            comparator = Comparator.comparing(TTVehicleResponse::getColor);
        } else {
            throw new IllegalArgumentException();
        }
        sort(entities, comparator, direction);
    }

    private void sort(List<TTVehicleResponse> entities, Comparator<TTVehicleResponse> comparator, String direction) {
        Collections.sort(entities, comparator);
        if (direction.equalsIgnoreCase("desc")) {
            Collections.reverse(entities);
        }
    }

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
        return filtered;
    }

    private String getAttributes(TTVehicleResponse entity, String attribute) {
        if (attribute.equals("name")) {
            return entity.getName();
        } else if (attribute.equals("introduction_date")) {
            return entity.getIntroductionDate().toString();
        } else if (attribute.equals("color")) {
            return entity.getColor();
        } else {
            throw new IllegalArgumentException();
        }

    }

    private List<TTVehicleResponse> getPage(List<TTVehicleResponse> entities, Integer page, Integer size) {
        if (page != null && size != null) {
            int start = (page - 1) * size;
            int end = Math.min(start + size, entities.size());
            return entities.subList(start, end);
        }
        return entities;
    }
}

