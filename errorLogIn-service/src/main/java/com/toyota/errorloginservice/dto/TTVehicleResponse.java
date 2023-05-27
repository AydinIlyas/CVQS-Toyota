package com.toyota.errorloginservice.dto;

import com.toyota.errorloginservice.domain.TTVehicleDefect;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Response DTO for tt_vehicle used as response.
 */
public class TTVehicleResponse {
    private String name;
    private LocalDate introductionDate;

    private String color;

    private List<TTVehicleDefect> defect=new ArrayList<>();
    public TTVehicleResponse() {
    }

    public TTVehicleResponse(String name, LocalDate introductionDate, String color, List<TTVehicleDefect> defect) {
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

    public List<TTVehicleDefect> getDefect() {
        return defect;
    }

    public void setDefect(List<TTVehicleDefect> defect) {
        this.defect = defect;
    }
}
