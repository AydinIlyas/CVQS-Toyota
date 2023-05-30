package com.toyota.errorloginservice.dto;

import com.toyota.errorloginservice.domain.EngineType;
import com.toyota.errorloginservice.domain.TransmissionType;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Response DTO for tt_vehicle used as response.
 */
public class TTVehicleResponse {
    private Long id;
    private String model;
    private String vin;
    private LocalDate yearOfProduction;
    private EngineType engineType;
    private TransmissionType transmissionType;
    private String color;

    private List<TTVehicleDefectResponse> defect=new ArrayList<>();


    public TTVehicleResponse(Long id, String model, String vin,
                             LocalDate yearOfProduction, EngineType engineType,
                             TransmissionType transmissionType, String color,
                             List<TTVehicleDefectResponse> defect) {
        this.id = id;
        this.model = model;
        this.vin = vin;
        this.yearOfProduction = yearOfProduction;
        this.engineType = engineType;
        this.transmissionType = transmissionType;
        this.color = color;
        this.defect = defect;
    }

    public TTVehicleResponse() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getVin() {
        return vin;
    }

    public void setVin(String vin) {
        this.vin = vin;
    }

    public LocalDate getYearOfProduction() {
        return yearOfProduction;
    }

    public void setYearOfProduction(LocalDate yearOfProduction) {
        this.yearOfProduction = yearOfProduction;
    }

    public EngineType getEngineType() {
        return engineType;
    }

    public void setEngineType(EngineType engineType) {
        this.engineType = engineType;
    }

    public TransmissionType getTransmissionType() {
        return transmissionType;
    }

    public void setTransmissionType(TransmissionType transmissionType) {
        this.transmissionType = transmissionType;
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
