package com.toyota.errorloginservice.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Where;
import org.springframework.format.annotation.DateTimeFormat;

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
@Where(clause="deleted = FALSE")
public class TTVehicle {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;
    private String model;
    private String vin;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDate yearOfProduction;
    @Enumerated(value=EnumType.STRING)
    private EngineType engineType;

    @Enumerated(value=EnumType.STRING)
    private TransmissionType transmissionType;
    private String color;
    private boolean deleted;

    @OneToMany(mappedBy = "tt_vehicle", fetch = FetchType.LAZY)
    private List<TTVehicleDefect> defect;

}
