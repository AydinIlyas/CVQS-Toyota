package com.toyota.errorloginservice.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Where;

import java.time.LocalDateTime;
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
@Where(clause="deleted = FALSE")
public class TTVehicleDefect {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String type;
    private String description;
    @Enumerated(value=EnumType.STRING)
    private State state;
    @Column(columnDefinition = "TIMESTAMP")
    private LocalDateTime reportTime;

    private String reportedBy;

    @Lob
    private byte[] defectImage;

    private boolean deleted;
    @ManyToOne
    @JsonIgnore
    private TTVehicle tt_vehicle;

    @OneToMany(mappedBy="tt_vehicle_defect", cascade=CascadeType.ALL)
    private List<TTVehicleDefectLocation> location;
}
