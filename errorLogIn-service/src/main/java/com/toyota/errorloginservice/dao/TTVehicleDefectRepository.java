package com.toyota.errorloginservice.dao;

import com.toyota.errorloginservice.domain.TTVehicleDefect;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for accessing tt_vehicle_defect table in database.
 */
@Repository
public interface TTVehicleDefectRepository extends JpaRepository<TTVehicleDefect,Long> {
}
