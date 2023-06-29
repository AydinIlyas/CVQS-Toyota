package com.toyota.errorloginservice.dto;


import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

/**
 * DTO for tt_vehicle_defect_location used as input.
 */
public class TTVehicleDefectLocationDTO {
    private Long id;
    @NotNull(message = "X Axis must not be null")
    @Min(groups = UpdateValidation.class,value = 0,message = "X Axis value must be greater than or equal to zero")
    private Integer x_Axis;
    @NotNull(message = "Y Axis must not be null")
    @Min(groups = UpdateValidation.class,value = 0,message = "Y Axis value must be greater than or equal to zero")
    private Integer y_Axis;

    public TTVehicleDefectLocationDTO() {
    }

    public TTVehicleDefectLocationDTO(Long id,Integer x_Axis, Integer y_Axis) {
        this.id=id;
        this.x_Axis = x_Axis;
        this.y_Axis = y_Axis;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
