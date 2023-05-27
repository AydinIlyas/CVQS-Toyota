package com.toyota.errorloginservice.dto;

import com.toyota.errorloginservice.domain.TTVehicleDefectLocation;
import jakarta.validation.constraints.NotNull;

import java.util.List;

/**
 * DTO for tt_vehicle_defect used as input.
 */

public class TTVehicleDefectDTO {
    @NotNull
    private String type;
    private byte[] defectImage;

    private List<TTVehicleDefectLocation> location;

    public TTVehicleDefectDTO() {
    }

    public TTVehicleDefectDTO(String type, byte[] defectImage, List<TTVehicleDefectLocation> location) {
        this.type = type;
        this.defectImage = defectImage;
        this.location = location;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public byte[] getDefectImage() {
        return defectImage;
    }

    public void setDefectImage(byte[] defectImage) {
        this.defectImage = defectImage;
    }

    public List<TTVehicleDefectLocation> getLocation() {
        return location;
    }

    public void setLocation(List<TTVehicleDefectLocation> location) {
        this.location = location;
    }
}

