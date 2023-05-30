package com.toyota.errorloginservice.dto;

import com.toyota.errorloginservice.domain.TTVehicleDefectLocation;
import jakarta.persistence.Lob;
import jakarta.validation.constraints.NotNull;

import java.util.List;

/**
 * DTO for tt_vehicle_defect used as input.
 */

public class TTVehicleDefectDTO {
    @NotNull
    private String type;
    @Lob
    private byte[] defectImage;

    private List<TTVehicleDefectLocationDTO> location;

    public TTVehicleDefectDTO() {
    }

    public TTVehicleDefectDTO(String type, byte[] defectImage, List<TTVehicleDefectLocationDTO> location) {
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

    public List<TTVehicleDefectLocationDTO> getLocation() {
        return location;
    }

    public void setLocation(List<TTVehicleDefectLocationDTO> location) {
        this.location = location;
    }
}

