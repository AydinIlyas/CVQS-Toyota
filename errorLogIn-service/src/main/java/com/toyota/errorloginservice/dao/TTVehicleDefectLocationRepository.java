package com.toyota.errorloginservice.dao;

import com.toyota.errorloginservice.domain.TTVehicleDefectLocation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for accessing tt_vehicle_defect_location table in database.
 */
@Repository
public interface TTVehicleDefectLocationRepository extends JpaRepository<TTVehicleDefectLocation,Long> {
}
