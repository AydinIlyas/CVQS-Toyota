package com.toyota.errorloginservice.service.abstracts;

import com.toyota.errorloginservice.dto.PaginationResponse;
import com.toyota.errorloginservice.dto.TTVehicleDefectDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * Interface for ttVehicleDefect's service class
 */
public interface TTVehicleDefectService {
    TTVehicleDefectDTO addDefect(String username, Long vehicleId, TTVehicleDefectDTO defectDTO);
    TTVehicleDefectDTO deleteDefect(Long defectId);

    TTVehicleDefectDTO update(Long id, TTVehicleDefectDTO defectDTO);

    PaginationResponse<TTVehicleDefectDTO> getAllFiltered(int page, int size, String type, String state, String reportTime,
                                                          String reportedBy, String vin, String sortOrder,
                                                          List<String> sortBy);

    void addImage(Long defectId, MultipartFile image);

    byte[] getImage(Long defectId,String format,int width,int height,String colorHex,boolean processed);
}
