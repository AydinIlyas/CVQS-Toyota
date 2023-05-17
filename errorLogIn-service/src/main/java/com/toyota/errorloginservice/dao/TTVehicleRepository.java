package com.toyota.errorloginservice.dao;

import com.toyota.errorloginservice.domain.TTVehicle;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TTVehicleRepository extends JpaRepository<TTVehicle,Long> {
    List<TTVehicle> findAllByDeletedIsFalse();
}
