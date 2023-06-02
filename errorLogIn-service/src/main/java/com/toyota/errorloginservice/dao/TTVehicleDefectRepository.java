package com.toyota.errorloginservice.dao;

import com.toyota.errorloginservice.domain.TTVehicleDefect;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


/**
 * Repository interface for accessing tt_vehicle_defect table in database.
 */
@Repository
public interface TTVehicleDefectRepository extends JpaRepository<TTVehicleDefect,Long> {
    @Query("select d from TTVehicleDefect d where (UPPER(d.type) like concat('%',UPPER(?1),'%')" +
            "or d.type is null) and (UPPER(d.state) like concat('%',UPPER(?2),'%') or d.state" +
            " is null) and (cast(d.reportTime as string) like concat('%',?3,'%') or d.reportTime is null) " +
            "and (UPPER(d.reportedBy) like concat('%',UPPER(?4),'%') or d.reportedBy is null )" +
            "and (UPPER(d.tt_vehicle.vin) like concat('%',UPPER(?5),'%') or d.tt_vehicle is null)")
    Page<TTVehicleDefect> getDefectsFiltered(String type, String state, String reportTime,
                                         String reportedBy, String vin, Pageable pageable);
}
