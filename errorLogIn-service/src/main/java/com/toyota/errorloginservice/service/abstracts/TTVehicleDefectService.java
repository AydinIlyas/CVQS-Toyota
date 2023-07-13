package com.toyota.errorloginservice.service.abstracts;

import com.toyota.errorloginservice.dto.ImageDTO;
import com.toyota.errorloginservice.dto.PaginationResponse;
import com.toyota.errorloginservice.dto.TTVehicleDefectDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * Interface for ttVehicleDefect's service class
 */
public interface TTVehicleDefectService {
    /**
     * Adds a defect to vehicle.
     * @param vehicleId ID of the vehicle.
     * @param defectDTO TTVehicleDefectDTO which will be added to the vehicle.
     */
    TTVehicleDefectDTO addDefect(String username, Long vehicleId, TTVehicleDefectDTO defectDTO);
    /**
     * Soft deletes defect and associated locations, if present.
     *
     * @param defectId Defect id of the defect which will be deleted.
     * @return TTVehicleDefectDTO which was deleted
     */
    TTVehicleDefectDTO deleteDefect(Long defectId);
    /**
     * Updates defect
     *
     * @param id ID of defect
     * @param defectDTO Updated defect
     * @return TTVehicleDefectDTO with the updated defect
     */

    TTVehicleDefectDTO update(Long id, TTVehicleDefectDTO defectDTO);
    /**
     * Gets defects with paging, filtering and sorting
     * @param page desired page
     * @param size size of page
     * @param type       desired type of defect
     * @param state      desired state of defect
     * @param reportTime desired reportTime of defect
     * @param reportedBy desired reporter of defect
     * @param vin        desired vin of defect
     * @param sortOrder  desired sort direction
     * @param sortBy     desired fields for sorting
     * @return PaginationResponse with a list of TTVehicleDefectDTO
     */

    PaginationResponse<TTVehicleDefectDTO> getAllFiltered(int page, int size, String type, String state, String reportTime,
                                                          String reportedBy, String vin, String sortOrder,
                                                          List<String> sortBy);
    /**
     * Adds image to defect
     * @param defectId ID of the defect.
     * @param image    Image to add.
     */
    void addImage(Long defectId, MultipartFile image);
    /**
     * Gets image with associated locations
     * @param defectId ID of the defect.
     * @return ImageDTO which contains image and locations
     */
    ImageDTO getImage(Long defectId);
    /**
     * Removes image from defect
     * @param defectId ID of defect
     */

    void removeImage(Long defectId);
}
