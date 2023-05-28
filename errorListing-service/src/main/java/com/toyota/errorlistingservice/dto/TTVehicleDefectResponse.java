package com.toyota.errorlistingservice.dto;

import java.util.List;

/**
 * DTO class for receiving the data from errorLogin-service and displaying it as response.
 */
public class TTVehicleDefectResponse {

    private String type;
    private byte[] defectImage;

    private List<TTVehicleDefectLocationResponse> location;

    public TTVehicleDefectResponse() {
    }

    public TTVehicleDefectResponse(String type, byte[] defectImage, List<TTVehicleDefectLocationResponse> location) {
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

    public List<TTVehicleDefectLocationResponse> getLocation() {
        return location;
    }

    public void setLocation(List<TTVehicleDefectLocationResponse> location) {
        this.location = location;
    }
}
