package com.toyota.errorloginservice.service.abstracts;

import com.toyota.errorloginservice.dto.PaginationResponse;
import com.toyota.errorloginservice.dto.TTVehicleDefectDTO;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Sort;

import java.util.List;

/**
 * Interface for ttVehicleDefect's service class
 */
public interface TTVehicleDefectService {
    TTVehicleDefectDTO addDefect(HttpServletRequest request, Long vehicleId, TTVehicleDefectDTO defectDTO);
    void deleteDefect(Long defectId);

    TTVehicleDefectDTO update(Long id, TTVehicleDefectDTO defectDTO);

    PaginationResponse<TTVehicleDefectDTO> getAllFiltered(int page, int size, String type, String state, String reportTime,
                                                          String reportedBy, String vin, String sortOrder,
                                                          List<String> sortBy);
}
