package com.toyota.errorlistingservice.dto;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * DTO class for receiving the data from errorLogin-service and displaying it as response.
 */
public class TTVehicleResponse {
    private String name;
    private LocalDate introductionDate;

    private String color;

    private List<TTVehicleDefectResponse> defect=new ArrayList<>();
    public TTVehicleResponse() {
    }

    public TTVehicleResponse(String name, LocalDate introductionDate, String color, List<TTVehicleDefectResponse> defect) {
        this.name = name;
        this.introductionDate = introductionDate;
        this.color = color;
        this.defect = defect;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getIntroductionDate() {
        return introductionDate;
    }

    public void setIntroductionDate(LocalDate introductionDate) {
        this.introductionDate = introductionDate;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public List<TTVehicleDefectResponse> getDefect() {
        return defect;
    }

    public void setDefect(List<TTVehicleDefectResponse> defect) {
        this.defect = defect;
    }
}
