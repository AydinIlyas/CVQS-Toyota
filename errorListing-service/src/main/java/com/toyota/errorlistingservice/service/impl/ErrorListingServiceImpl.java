package com.toyota.errorlistingservice.service.impl;


import com.toyota.errorlistingservice.dto.ImageDTO;
import com.toyota.errorlistingservice.dto.PaginationResponse;
import com.toyota.errorlistingservice.dto.TTVehicleDefectLocationDTO;
import com.toyota.errorlistingservice.exceptions.DefectNotFoundException;
import com.toyota.errorlistingservice.exceptions.ImageNotFoundException;
import com.toyota.errorlistingservice.exceptions.ImageProcessingException;
import com.toyota.errorlistingservice.service.abstracts.ErrorListingService;
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
     * @return Mono custom paging response
     */
    @Override
    public Mono<PaginationResponse<Object>> getVehicles(int page, int size, List<String> sortBy,
                                                        String sortOrder, String model, String vin, String yearOfProduction,
                                                        String transmissionType, String engineType, String color) {
        logger.info("Sending request for fetching vehicles to errorLogin-service");
        return webClientBuilder.build().get()
                .uri("http://error-login-service/vehicle/getAll", uriBuilder ->
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
     * Gets defects with paging, filtering and sorting.
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
     * @return Mono Custom paging response
     */
    @Override
    public Mono<PaginationResponse<Object>> getDefects(int page, int size, String type, String state,
                                                       String reportTime, String reportedBy, String vin,
                                                       String sortOrder, String sortBy) {
        logger.info("Sending request for fetching defects to errorLogin-service");
        return webClientBuilder.build().get()
                .uri("http://error-login-service/defect/getAll", uriBuilder ->
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
     * Gets processed image
     * @param defectId ID of defect with the image
     * @param format Format of the image (png/jpeg)
     * @param processed Specifies whether the image has been processed.
     * @return byte[] image
     */
    @Override
    public byte[] getImage(Long defectId,
                           String format,
                           boolean processed) {
        ImageDTO imageDTO = webClientBuilder.build().get()
                .uri("http://error-login-service/defect/{defectId}/get/image", defectId)
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
            for (TTVehicleDefectLocationDTO location : imageDTO.getLocationDTO()) {
                Color color = Color.decode(location.getColorHex());
                g.setColor(color);
                int x = location.getX_Axis() - location.getWidth() / 2;
                int y = location.getY_Axis() - location.getHeight() / 2;
                g.fillRect(x, y, location.getWidth(), location.getHeight());
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

}

