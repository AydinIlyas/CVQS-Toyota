package com.toyota.errorloginservice.dto;

import lombok.Data;

import java.time.LocalDate;

public class TTVehicleDTO {
    private String name;
    private LocalDate introductionDate;

    private String color;

    public TTVehicleDTO() {
    }

    public TTVehicleDTO(String name, LocalDate introductionDate, String color) {
        this.name = name;
        this.introductionDate = introductionDate;
        this.color = color;
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
}
