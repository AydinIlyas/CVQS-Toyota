package com.toyota.errorloginservice.service.common;

import com.toyota.errorloginservice.domain.TTVehicle;
import com.toyota.errorloginservice.domain.TTVehicleDefect;
import com.toyota.errorloginservice.domain.TTVehicleDefectLocation;
import com.toyota.errorloginservice.dto.TTVehicleDTO;
import com.toyota.errorloginservice.dto.TTVehicleDefectDTO;
import com.toyota.errorloginservice.dto.TTVehicleDefectLocationDTO;
import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Class for managing mappings
 */
public class MapUtil {
    private final ModelMapper modelMapper;

    public MapUtil(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

        /**
     * Converts ttVehicle to ttVehicleDTO.
     * @param ttVehicle Vehicle object which will be converted to DTO
     * @return TTVehicleDTO
     */
    public TTVehicleDTO convertToDTO(TTVehicle ttVehicle) {
        return modelMapper.map(ttVehicle, TTVehicleDTO.class);
    }

    /**
     * Converts defects and locations too in a dto
     * @param ttVehicle TTVehicle which will be converted
     * @return TTVehicleDTO
     */
    public TTVehicleDTO convertVehicleWithAllToDTO(TTVehicle ttVehicle)
    {
        TTVehicleDTO vehicleResponse=modelMapper.map(ttVehicle, TTVehicleDTO.class);
        if(ttVehicle.getDefect()!=null&&!ttVehicle.getDefect().isEmpty())
        {
            List<TTVehicleDefectDTO> defectResponses=ttVehicle.getDefect()
                    .stream().map(this::convertDefectToDTO).collect(Collectors.toList());
            vehicleResponse.setDefect(defectResponses);
        }
        return vehicleResponse;
    }

    /**
     * Converts Defect with locations to dto
     * @param defect Defect which will be converted to dto
     * @return TTVehicleDefectDTO
     */
    public TTVehicleDefectDTO convertDefectToDTO(TTVehicleDefect defect)
    {
        TTVehicleDefectDTO defectDTO=modelMapper.map(defect, TTVehicleDefectDTO.class);
        if(defect.getLocation()!=null&&!defect.getLocation().isEmpty())
        {
            List<TTVehicleDefectLocationDTO> locationDTO=defect.getLocation().stream()
                    .map(this::convertLocationDTO).collect(Collectors.toList());
            defectDTO.setLocation(locationDTO);
        }
        return defectDTO;
    }

    /**
     * Converts location to dto
     * @param location location which will be converted
     * @return TTVehicleDefectLocationDTO
     */
    public TTVehicleDefectLocationDTO convertLocationDTO(TTVehicleDefectLocation location)
    {
        return modelMapper.map(location,TTVehicleDefectLocationDTO.class);
    }

    /**
     * Converts Vehicle dto to entity
     * @param ttVehicleDTO Dto which will be converted
     * @return TTVehicle
     */
    public TTVehicle convertVehicleDTOToEntity(TTVehicleDTO ttVehicleDTO)
    {
        return modelMapper.map(ttVehicleDTO,TTVehicle.class);
    }
    /**
     * Converts defect dto to entity
     * @param defectDTO  TTVehicleDefectDTO which will be converted
     * @return TTVehicleDefect
     */
    public TTVehicleDefect convertDefectDTOToEntity(TTVehicleDefectDTO defectDTO)
    {
        return modelMapper.map(defectDTO,TTVehicleDefect.class);
    }

}
