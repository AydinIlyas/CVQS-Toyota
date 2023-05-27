package com.toyota.errorloginservice.dto;


import jakarta.validation.constraints.NotNull;

/**
 * DTO for tt_vehicle_defect_location used as input.
 */
public class TTVehicleDefectLocationDTO {
    @NotNull
    private Integer x_Axis;
    @NotNull
    private Integer y_Axis;

    public TTVehicleDefectLocationDTO() {
    }

    public TTVehicleDefectLocationDTO(Integer x_Axis, Integer y_Axis) {
        this.x_Axis = x_Axis;
        this.y_Axis = y_Axis;
    }

    public Integer getX_Axis() {
        return x_Axis;
    }

    public void setX_Axis(Integer x_Axis) {
        this.x_Axis = x_Axis;
    }

    public Integer getY_Axis() {
        return y_Axis;
    }

    public void setY_Axis(Integer y_Axis) {
        this.y_Axis = y_Axis;
    }
}
