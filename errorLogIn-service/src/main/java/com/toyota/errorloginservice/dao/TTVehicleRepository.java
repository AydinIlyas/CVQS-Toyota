package com.toyota.errorloginservice.dao;

import com.toyota.errorloginservice.domain.TTVehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for accessing tt_vehicle table in database.
 */
@Repository
public interface TTVehicleRepository extends JpaRepository<TTVehicle,Long> {
    List<TTVehicle> findAllByDeletedIsFalse();
}
