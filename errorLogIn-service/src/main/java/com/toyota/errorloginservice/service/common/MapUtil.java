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

public class MapUtil {
    private final ModelMapper modelMapper;

    public MapUtil(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

        /**
     * Converts ttVehicle to ttVehicleResponse.
     *
     * @param ttVehicle Vehicle object which will be converted to Response
     * @return TTVehicleResponse
     */
    public TTVehicleDTO convertToDTO(TTVehicle ttVehicle) {
        if(ttVehicle==null) return null;
        return modelMapper.map(ttVehicle, TTVehicleDTO.class);
    }
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
    public TTVehicleDefectLocationDTO convertLocationDTO(TTVehicleDefectLocation location)
    {
        return modelMapper.map(location,TTVehicleDefectLocationDTO.class);
    }
    public TTVehicle convertVehicleDTOToEntity(TTVehicleDTO ttVehicleDTO)
    {
        return modelMapper.map(ttVehicleDTO,TTVehicle.class);
    }
    public TTVehicleDefect convertDefectDTOToEntity(TTVehicleDefectDTO defectDTO)
    {
        return modelMapper.map(defectDTO,TTVehicleDefect.class);
    }

}
