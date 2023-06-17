package com.toyota.errorloginservice.service.impl;

import com.toyota.errorloginservice.dao.TTVehicleDefectRepository;
import com.toyota.errorloginservice.dao.TTVehicleRepository;
import com.toyota.errorloginservice.domain.TTVehicle;
import com.toyota.errorloginservice.domain.TTVehicleDefect;
import com.toyota.errorloginservice.domain.TTVehicleDefectLocation;
import com.toyota.errorloginservice.dto.PaginationResponse;
import com.toyota.errorloginservice.dto.TTVehicleDefectDTO;
import com.toyota.errorloginservice.dto.TTVehicleDefectLocationDTO;
import com.toyota.errorloginservice.exception.EntityNotFoundException;
import com.toyota.errorloginservice.service.abstracts.TTVehicleDefectService;
import com.toyota.errorloginservice.service.common.SortOrder;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service class for managing tt_vehicle_defect data.
 */
@Service
@RequiredArgsConstructor
public class TTVehicleDefectServiceImpl implements TTVehicleDefectService {
    private final TTVehicleDefectRepository ttVehicleDefectRepository;
    private final TTVehicleRepository ttVehicleRepository;
    private final ModelMapper modelMapper;
    private final Logger logger= LogManager.getLogger(TTVehicleDefectServiceImpl.class);




    /**
     * @param type desired type of defect
     * @param state desired state of defect
     * @param reportTime desired reportTime of defect
     * @param reportedBy desired reporter of defect
     * @param vin desired vin of defect
     * @param sortOrder desired sort direction
     * @param sortBy desired fields for sorting
     * @return Page of defects
     */
    @Override
    public PaginationResponse<TTVehicleDefectDTO> getAllFiltered(int page,int size, String type, String state, String reportTime,
                                                   String reportedBy, String vin, String sortOrder,
                                                   List<String> sortBy) {
        Pageable pageable= PageRequest.of(page,size,Sort.by(SortOrder.createSortOrder(sortBy,sortOrder)));
        Page<TTVehicleDefect> pageResponse=ttVehicleDefectRepository
                .getDefectsFiltered(type,state,reportTime,reportedBy,vin,pageable);
        List<TTVehicleDefectDTO> ttVehicleDefectDTOS=pageResponse.stream().map(this::convertAllToDTO).collect(Collectors.toList());

        return new PaginationResponse<>(ttVehicleDefectDTOS,pageResponse);
    }
    /**
     * Adds a defect to the vehicle if present.
     * @param vehicleId Vehicle id of the vehicle which has the defect.
     * @param defectDTO Defect object which will added to the vehicle.
     */
    @Override
    public TTVehicleDefectDTO addDefect(HttpServletRequest request,Long vehicleId,
                                             TTVehicleDefectDTO defectDTO) {
        Optional<TTVehicle> optionalTTVehicle = ttVehicleRepository.findById(vehicleId);
        if (optionalTTVehicle.isPresent()) {
            TTVehicleDefect defect = convertToEntity(defectDTO);
            String username=(String)request.getAttribute("Username");
            defect.setReportedBy(username);
            TTVehicle ttVehicle = optionalTTVehicle.get();
            ttVehicle.getDefect().add(defect);
            defect.setTt_vehicle(ttVehicle);
            if(defect.getLocation()!=null)
                for(TTVehicleDefectLocation location : defect.getLocation())
                    location.setTt_vehicle_defect(defect);
            TTVehicleDefect saved=ttVehicleDefectRepository.save(defect);
            logger.info("Created defect successfully! Vehicle ID: {}, Defect ID: {}",vehicleId,defect);
            return convertToDTO(saved);
        }
        else{
            logger.warn("Vehicle with id {} couldn't found!",vehicleId);
            throw new EntityNotFoundException("There is no Vehicle with the id: "+vehicleId);
        }

    }

    /**
     * Updates defect
     * @param id ID of defect
     * @param defectDTO Updated defect
     * @return TTVehicleDefectDTO updated
     */
    @Override
    public TTVehicleDefectDTO update(Long id, TTVehicleDefectDTO defectDTO) {
        Optional<TTVehicleDefect> optionalDefect=ttVehicleDefectRepository.findById(id);
        if(optionalDefect.isPresent())
        {
            TTVehicleDefect defect=optionalDefect.get();
            if(defectDTO.getType()!=null&&!defect.getType().equals(defectDTO.getType()))
            {
                defect.setType(defectDTO.getType());
            }

            if(defectDTO.getDescription()!=null&&!defect.getDescription().equals(defectDTO.getDescription()))
            {
                defect.setDescription(defectDTO.getDescription());
            }
            if(defectDTO.getState()!=null&&!defect.getState().equals(defectDTO.getState()))
            {
                defect.setState(defectDTO.getState());
            }
            ttVehicleDefectRepository.save(defect);
            logger.info("DEFECT UPDATED SUCCESSFULLY! DEFECT ID: {}",id);
            return convertToDTO(defect);
        }
        logger.warn("DEFECT WITH NOT FOUND! DEFECT ID: {}",id);
        throw new EntityNotFoundException("DEFECT WITH NOT FOUND! DEFECT ID: "+id);
    }




    /**
     * Soft deletes defect and associated locations, if present.
     * @param defectId Defect id of the defect which will be deleted.
     */
    @Override
    @Transactional
    public void deleteDefect(Long defectId) {
        Optional<TTVehicleDefect> optionalDefect = ttVehicleDefectRepository.findById(defectId);
        if (optionalDefect.isPresent()) {
            TTVehicleDefect defect = optionalDefect.get();
            List<TTVehicleDefectLocation> locations = defect.getLocation();
            locations.forEach(l -> l.setDeleted(true));
            defect.setDeleted(true);
            ttVehicleDefectRepository.save(defect);
            logger.info("Soft deleted defect successfully! DEFECT ID: {}",defectId);
        }
        else{
            logger.warn("Defect couldn't found! ID: {}",defectId);
            throw new EntityNotFoundException("Defect with id "+defectId+"couldn't found!");
        }
    }



    private TTVehicleDefect convertToEntity(TTVehicleDefectDTO defectDTO)
    {
        return modelMapper.map(defectDTO,TTVehicleDefect.class);
    }
    private TTVehicleDefectDTO convertToDTO(TTVehicleDefect defect)
    {
        return modelMapper.map(defect,TTVehicleDefectDTO.class);
    }
    private TTVehicleDefectDTO convertAllToDTO(TTVehicleDefect defect)
    {
        TTVehicleDefectDTO defectDto=modelMapper.map(defect,TTVehicleDefectDTO.class);
        if(defect.getLocation()!=null&&!defect.getLocation().isEmpty())
        {
            List<TTVehicleDefectLocationDTO> locationDto=defect.getLocation().stream().
                    map(this::convertToLocationDTO).collect(Collectors.toList());
            defectDto.setLocation(locationDto);
        }
        return defectDto;
    }
    private TTVehicleDefectLocationDTO convertToLocationDTO(TTVehicleDefectLocation location)
    {
        return modelMapper.map(location,TTVehicleDefectLocationDTO.class);
    }

}
