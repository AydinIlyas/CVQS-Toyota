package com.toyota.errorloginservice.dao;

import com.toyota.errorloginservice.domain.TTVehicle;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * Repository interface for accessing tt_vehicle table in database.
 */
@Repository
public interface TTVehicleRepository extends JpaRepository<TTVehicle,Long> {
    @Query("select v from TTVehicle v where UPPER(v.model) like concat('%',UPPER(?1),'%') " +
            "and Upper(v.vin) like concat('%',Upper(?2),'%') and cast(v.yearOfProduction as string) like " +
            "concat('%',?3,'%') and upper(v.transmissionType) like concat('%',UPPER(?4),'%') " +
            "and upper(v.engineType) like concat('%',UPPER(?5),'%') and upper(v.color) " +
            "like concat('%',UPPER(?6),'%')")
    List<TTVehicle> getVehiclesFiltered(String model, String vin, String yearOfProduction, String transmissionType,
                                        String engineType, String color, Sort sort);

}
