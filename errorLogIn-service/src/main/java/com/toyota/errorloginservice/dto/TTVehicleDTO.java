package com.toyota.errorloginservice.dto;



import com.toyota.errorloginservice.domain.EngineType;
import com.toyota.errorloginservice.domain.TransmissionType;

import java.time.LocalDate;
import java.util.List;

/**
 * DTO for tt_vehicle used as input.
 */
public class TTVehicleDTO {
    private String model;
    private String vin;
    private LocalDate yearOfProduction;
    private EngineType engineType;
    private TransmissionType transmissionType;
    private String color;

    private List<TTVehicleDefectDTO> defect;

    public TTVehicleDTO(String model, String vin, LocalDate yearOfProduction,
                        EngineType engineType, TransmissionType transmissionType,
                        String color, List<TTVehicleDefectDTO> defect) {
        this.model = model;
        this.vin = vin;
        this.yearOfProduction = yearOfProduction;
        this.engineType = engineType;
        this.transmissionType = transmissionType;
        this.color = color;
        this.defect = defect;
    }

    public TTVehicleDTO() {
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

    public List<TTVehicleDefectDTO> getDefect() {
        return defect;
    }

    public void setDefect(List<TTVehicleDefectDTO> defect) {
        this.defect = defect;
    }


}
