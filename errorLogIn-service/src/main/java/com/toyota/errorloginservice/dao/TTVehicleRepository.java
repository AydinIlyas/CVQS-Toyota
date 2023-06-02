package com.toyota.errorloginservice.dao;

import com.toyota.errorloginservice.domain.TTVehicle;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


/**
 * Repository interface for accessing tt_vehicle table in database.
 */
@Repository
public interface TTVehicleRepository extends JpaRepository<TTVehicle,Long> {
    @Query("select v from TTVehicle v where (UPPER(v.model) like concat('%',UPPER(?1),'%') or v.model is null) " +
            "and (Upper(v.vin) like concat('%',Upper(?2),'%') or v.vin is null) and " +
            "(cast(v.yearOfProduction as string) like concat('%',?3,'%')or v.yearOfProduction is null)" +
            " and (upper(v.transmissionType) like concat('%',UPPER(?4),'%') or v.transmissionType is null)" +
            "and (upper(v.engineType) like concat('%',UPPER(?5),'%') or v.engineType is null) and (upper(v.color) " +
            "like concat('%',UPPER(?6),'%') or v.color is null)")
    Page<TTVehicle> getVehiclesFiltered(String model, String vin, String yearOfProduction, String transmissionType,
                                        String engineType, String color, Pageable pageable);

}
