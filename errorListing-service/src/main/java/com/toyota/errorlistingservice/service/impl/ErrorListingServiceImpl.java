package com.toyota.errorlistingservice.service.impl;


import com.toyota.errorlistingservice.dto.ImageDTO;
import com.toyota.errorlistingservice.dto.PaginationResponse;
import com.toyota.errorlistingservice.dto.TTVehicleDefectLocationDTO;
import com.toyota.errorlistingservice.exceptions.BearerTokenNotFoundException;
import com.toyota.errorlistingservice.exceptions.DefectNotFoundException;
import com.toyota.errorlistingservice.exceptions.ImageNotFoundException;
import com.toyota.errorlistingservice.exceptions.ImageProcessingException;
import com.toyota.errorlistingservice.service.abstracts.ErrorListingService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
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

    private final Logger logger = LogManager.getLogger(ErrorListingServiceImpl.class);

    /**
     * Gets vehicles with paging,filtering and sorting
     *
     * @param model            desired vehicle model
     * @param vin              desired vehicle identity number
     * @param engineType       desired engine type
     * @param transmissionType desired transmission type
     * @param color            desired color
     * @param yearOfProduction desired year of production
     * @param page             page number
     * @param size             objects on a page
     * @param sortBy           sorted by Field
     * @param sortOrder        sort Direction
     * @return vehicle objects
     */
    @Override
    public Mono<PaginationResponse<Object>> getVehicles(int page, int size, List<String> sortBy,
                                                        String sortOrder, String model, String vin, String yearOfProduction,
                                                        String transmissionType, String engineType, String color) {
        logger.info("Sending request for fetching vehicles to errorLogin-service");
        return webClientBuilder.build().get()
                .uri("http://error-login-service/ttvehicle/getAll", uriBuilder ->
                        uriBuilder
                                .queryParam("page", page)
                                .queryParam("size", size)
                                .queryParam("sortBy", sortBy)
                                .queryParam("sortOrder", sortOrder)
                                .queryParam("model", model)
                                .queryParam("vin", vin)
                                .queryParam("yearOfProduction", yearOfProduction)
                                .queryParam("transmissionType", transmissionType)
                                .queryParam("engineType", engineType)
                                .queryParam("color", color)
                                .build()
                )
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<PaginationResponse<Object>>() {
                })
                .doOnSuccess(response -> {
                    logger.debug("Vehicles successfully fetched from errorLogin-service.");
                    if (response != null)
                        logger.info("Page: {} Size: {}, Total:{}"
                                , page, size, response.getPageable().getTotalElements());
                });
    }

    /**
     * Gets defects with paging, filtering and sorting. Sends request to errorLogin.
     *
     * @param page       page number starts from 0
     * @param size       entity amount on a page
     * @param type       desired type of defect
     * @param state      desired state of defect
     * @param reportTime desired reportTime of defect
     * @param reportedBy desired reporter
     * @param vin        desired vehicle id number
     * @param sortOrder  desired sorting direction ASC,DESC
     * @param sortBy     ordered by fields
     * @return Custom paging response
     */
    @Override
    public Mono<PaginationResponse<Object>> getDefects(int page, int size, String type, String state,
                                                       String reportTime, String reportedBy, String vin,
                                                       String sortOrder, String sortBy) {
        logger.info("Sending request for fetching defects to errorLogin-service");
        return webClientBuilder.build().get()
                .uri("http://error-login-service/ttvehicleDefect/getAll", uriBuilder ->
                        uriBuilder
                                .queryParam("page", page)
                                .queryParam("size", size)
                                .queryParam("sortBy", sortBy)
                                .queryParam("sortOrder", sortOrder)
                                .queryParam("type", type)
                                .queryParam("state", state)
                                .queryParam("reportTime", reportTime)
                                .queryParam("reportedBy", reportedBy)
                                .queryParam("vin", vin)
                                .build()
                )
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<PaginationResponse<Object>>() {
                })
                .doOnSuccess(response -> {
                    logger.info("Defects successfully fetched from errorLogin-service.");
                    if (response != null)
                        logger.info("Page: {} Size: {}, Total:{}", page, size, response.getPageable().getTotalElements());
                });
    }

    /**
     * @param defectId
     * @param format
     * @param width
     * @param height
     * @param colorHex
     * @param processed
     * @return
     */
    @Override
    public byte[] getImage(Long defectId,
                           String format,
                           int width,
                           int height,
                           String colorHex,
                           boolean processed) {
        ImageDTO imageDTO = webClientBuilder.build().get()
                .uri("http://error-login-service/ttvehicleDefect/get/image/{defectId}", defectId)
                .retrieve()
                .onStatus(HttpStatusCode::isError, clientResponse -> {
                    logger.warn("Defect not found. ID: {}", defectId);
                    throw new DefectNotFoundException("Defect not found. ID: " + defectId);
                })
                .bodyToMono(ImageDTO.class)
                .block();
        if(imageDTO==null)
        {
            logger.warn("Defect has no Image! Defect ID: {}",defectId);
            throw new ImageNotFoundException("Defect has no Image! Defect ID: "+defectId);
        }
        if (!processed)
            return imageDTO.getImage();
        try {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(imageDTO.getImage());
            BufferedImage image = ImageIO.read(inputStream);
            Graphics2D g = (Graphics2D) image.getGraphics();
            g.setStroke(new BasicStroke(3));
            Color color = Color.decode(isValidColorHex(colorHex));
            g.setColor(color);
            for (TTVehicleDefectLocationDTO location : imageDTO.getLocationDTO()) {
                int x = location.getX_Axis() - width / 2;
                int y = location.getY_Axis() - height / 2;
                g.fillRect(x, y, width, height);
            }
            g.dispose();
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            String formatName = format.equalsIgnoreCase("png") ? "png" : "jpeg";
            ImageIO.write(image, formatName, outputStream);
            return outputStream.toByteArray();
        } catch (IOException e) {
            throw new ImageProcessingException("Failed to read Input-stream");
        }
    }
    private String isValidColorHex(String color) {
        String regex = "^#([A-Fa-f0-9]{6}|[A-Fa-f0-9]{3})$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(color);
        if (matcher.matches())
            return color;
        else
            return "#FF0000";

    }
}

