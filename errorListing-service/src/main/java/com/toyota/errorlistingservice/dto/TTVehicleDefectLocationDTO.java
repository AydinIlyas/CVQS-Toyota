package com.toyota.errorlistingservice.dto;


/**
 * DTO for tt_vehicle_defect_location for receiving locations.
 */
public class TTVehicleDefectLocationDTO {
    private Long id;

    private Integer x_Axis;

    private Integer y_Axis;

    private Integer width;
    private Integer height;
    private String colorHex;
    public TTVehicleDefectLocationDTO() {
    }

    public TTVehicleDefectLocationDTO(Long id, Integer x_Axis, Integer y_Axis, Integer width, Integer height, String colorHex) {
        this.id = id;
        this.x_Axis = x_Axis;
        this.y_Axis = y_Axis;
        this.width = width;
        this.height = height;
        this.colorHex = colorHex;
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

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public String getColorHex() {
        return colorHex;
    }

    public void setColorHex(String colorHex) {
        this.colorHex = colorHex;
    }
}
