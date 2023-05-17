package com.toyota.errorloginservice.dto;

import com.toyota.errorloginservice.domain.TTVehicleDefect;
import jakarta.persistence.ManyToOne;

public class TTVehicleDefectLocationDTO {

    private int x_Axis;
    private int y_Axis;

    public TTVehicleDefectLocationDTO() {
    }

    public TTVehicleDefectLocationDTO(int x_Axis, int y_Axis) {
        this.x_Axis = x_Axis;
        this.y_Axis = y_Axis;
    }

    public int getX_Axis() {
        return x_Axis;
    }

    public void setX_Axis(int x_Axis) {
        this.x_Axis = x_Axis;
    }

    public int getY_Axis() {
        return y_Axis;
    }

    public void setY_Axis(int y_Axis) {
        this.y_Axis = y_Axis;
    }
}
