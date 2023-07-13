package com.toyota.errorloginservice.dto;


import com.toyota.errorloginservice.domain.State;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO for tt_vehicle_defect used as input and response.
 */

public class TTVehicleDefectDTO {
    private Long id;

    @NotBlank(message = "Type must not be blank")
    private String type;

    private String description;
    @NotNull(message="State must not be null")
    private State state;
    private LocalDateTime reportTime;

    private String reportedBy;

    @Valid
    private List<TTVehicleDefectLocationDTO> location;

    public TTVehicleDefectDTO() {
    }

    public TTVehicleDefectDTO(Long id,String type, String description, State state,
                              List<TTVehicleDefectLocationDTO> location) {
        this.id=id;
        this.type = type;
        this.description = description;
        this.state = state;
        this.location = location;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public LocalDateTime getReportTime() {
        return reportTime;
    }

    public void setReportTime(LocalDateTime reportTime) {
        this.reportTime = reportTime;
    }

    public String getReportedBy() {
        return reportedBy;
    }

    public void setReportedBy(String reportedBy) {
        this.reportedBy = reportedBy;
    }

    public List<TTVehicleDefectLocationDTO> getLocation() {
        return location;
    }

    public void setLocation(List<TTVehicleDefectLocationDTO> location) {
        this.location = location;
    }
}

