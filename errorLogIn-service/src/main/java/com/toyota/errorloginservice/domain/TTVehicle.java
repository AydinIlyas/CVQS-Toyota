package com.toyota.errorloginservice.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

/**
 * TTVehicle class represents a tt_vehicle entity in database.
 */
@Entity
@Table(name="tt_vehicle")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TTVehicle {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;
    private String name;
    private LocalDate introductionDate;
    private String color;
    private boolean deleted;

    @OneToMany(mappedBy = "tt_vehicle",cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<TTVehicleDefect> defect;

}
