package com.toyota.errorloginservice.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

/**
 * TTVehicleDefect represents tt_vehicle_defect entity in database.
 */
@Entity
@Table(name="tt_vehicle_defect")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TTVehicleDefect {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String type;
    @Lob
    private byte[] defectImage;

    private boolean deleted;
    @ManyToOne
    @JsonIgnore
    private TTVehicle tt_vehicle;
    @OneToMany(mappedBy="tt_vehicle_defect", cascade=CascadeType.ALL)
    private List<TTVehicleDefectLocation> location;
}
