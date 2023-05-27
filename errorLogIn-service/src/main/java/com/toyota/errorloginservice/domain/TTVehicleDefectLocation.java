package com.toyota.errorloginservice.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

/**
 * TTVehicleDefectLocation represents tt_vehicle_defect_location entity from database.
 */
@Entity
@Table(name="tt_vehicle_defect_location")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TTVehicleDefectLocation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int x_Axis;
    private int y_Axis;

    private boolean deleted;
    @ManyToOne
    @JsonIgnore
    private TTVehicleDefect tt_vehicle_defect;
}
