package com.toyota.errorloginservice.dto;



import com.toyota.errorloginservice.domain.EngineType;
import com.toyota.errorloginservice.domain.TransmissionType;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;

import java.time.LocalDate;
import java.util.List;

/**
 * DTO for tt_vehicle used as input and response.
 */
public class TTVehicleDTO {
    private Long id;

    @NotBlank(message="Model must not be blank")
    private String model;

    @NotBlank(message="Vehicle identification number must not be blank")
    private String vin;
    @NotNull(message="Year of production must not be null")
    @PastOrPresent(groups = UpdateValidation.class,message="Date must be present or past")
    private LocalDate yearOfProduction;
    @NotNull(message="Engine type must not be null")
    private EngineType engineType;
    @NotNull(message="Transmission type must not be null")
    private TransmissionType transmissionType;
    private String color;
    @Valid
    private List<TTVehicleDefectDTO> defect;

    public TTVehicleDTO(Long id,String model, String vin, LocalDate yearOfProduction,
                        EngineType engineType, TransmissionType transmissionType,
                        String color, List<TTVehicleDefectDTO> defect) {
        this.id=id;
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

    public List<TTVehicleDefectDTO> getDefect() {
        return defect;
    }

    public void setDefect(List<TTVehicleDefectDTO> defect) {
        this.defect = defect;
    }


}
