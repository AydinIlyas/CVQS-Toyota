package com.toyota.terminalservice.service.impl;

import com.toyota.terminalservice.dao.TerminalRepository;
import com.toyota.terminalservice.domain.Terminal;
import com.toyota.terminalservice.dto.TerminalDTO;
import com.toyota.terminalservice.exception.TerminalNotFoundException;
import com.toyota.terminalservice.service.abstracts.TerminalService;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import java.util.Optional;

/**
 * Service class for terminal related requests.
 */
@Service
@RequiredArgsConstructor
public class TerminalServiceImpl implements TerminalService {
    private final TerminalRepository terminalRepository;
    private final ModelMapper modelMapper;
    private final Logger logger= LogManager.getLogger(TerminalServiceImpl.class);

    /**
     * Lists Terminals with paging, sorting and filtering.
     * @param page Page to be displayed
     * @param size Size of the page
     * @param name Filter for the field name.
     * @param isActive Filter for the field isActive
     * @param sortBy Which field to sort by
     * @param sortDirection Sort Direction (ASC/DESC)
     * @return Page of terminals
     */
    @Override
    public Page<TerminalDTO> getActiveTerminals(int page,int size,String name,boolean isActive,
                                                String sortBy,String sortDirection)
    {
        Pageable pageable= PageRequest.of(page,size,Sort.by(createSortOrder(sortBy,sortDirection)));
        Page<Terminal> terminals=terminalRepository.getTerminalsFiltered(name,isActive,pageable);
        logger.info("Terminal Page created! Page: {}, Size: {}, Sorted By: {}, Total Pages: {}," +
                        "Total Elements: {}",
                terminals.getPageable().getPageNumber(),terminals.getNumberOfElements(),terminals.getPageable().getSort(),
                terminals.getTotalPages(),terminals.getTotalElements());
        return terminals.map(this::convertToDto);
    }

    /**
     * Creates Sort.Order for Pageable
     * @param sortBy Which field to sort by
     * @param direction Sort Direction (ASC/DESC)
     * @return Sort.Order
     */
    private Sort.Order createSortOrder(String sortBy, String direction)
    {
        if(direction.equalsIgnoreCase("Desc"))
            return new Sort.Order(Sort.Direction.DESC,sortBy);
        else
            return new Sort.Order(Sort.Direction.ASC,sortBy);
    }

    /**
     * Creates terminal
     * @param terminalDTO Terminal to be created in database.
     */
    @Override
    public void createTerminal(TerminalDTO terminalDTO)
    {
        Terminal terminal=convertToTerminal(terminalDTO);
        terminal.setActive(true);
        Terminal saved=terminalRepository.save(terminal);
        logger.info("CREATED Terminal Successfully! ID: {}",saved.getId());
    }

    /**
     * Activates Terminal
     * @param id ID of terminal to be activated.
     */
    @Override
    public void activateTerminal(Long id) {

        Optional<Terminal> optionalTerminal=terminalRepository.findById(id);
        if(optionalTerminal.isPresent())
        {
            Terminal terminal=optionalTerminal.get();
            terminal.setActive(true);
            terminalRepository.save(terminal);
            logger.info("Terminal CREATED successfully");
        }
        else{
            logger.warn("Terminal Not Found! ID: {}",id);
            throw new TerminalNotFoundException("Terminal Not Found! ID: "+id);
        }


    }

    /**
     * Disables Terminal
     * @param id ID of terminal to be disabled
     */
    @Override
    public void disableTerminal(Long id) {
        Optional<Terminal> optionalTerminal=terminalRepository.findById(id);

        if(optionalTerminal.isPresent())
        {
            Terminal terminal=optionalTerminal.get();
            terminal.setActive(false);
            terminalRepository.save(terminal);
            logger.info("Terminal ACTIVATED successfully");
        }
        else{
            logger.warn("Terminal Not Found! ID: {}",id);
            throw new TerminalNotFoundException("Terminal Not Found! ID: "+id);
        }
    }

    /**
     * Soft deletes Terminal
     * @param id ID of terminal to be softly deleted.
     */
    @Override
    public void delete(Long id) {
        Optional<Terminal> optionalTerminal=terminalRepository.findById(id);
        if(optionalTerminal.isPresent())
        {
            Terminal terminal=optionalTerminal.get();
            terminal.setDeleted(true);
            terminalRepository.save(terminal);
        }
        else{
            logger.warn("Terminal Not Found! ID: {}",id);
            throw new TerminalNotFoundException("Terminal Not Found! ID: "+id);
        }
    }

    /**
     * Converts to TerminalDTO
     * @param terminal Terminal entity
     * @return TerminalDTO
     */
    private TerminalDTO convertToDto(Terminal terminal)
    {
        return modelMapper.map(terminal,TerminalDTO.class);
    }

    /**
     * Converts To Terminal Entity
     * @param terminalDTO TerminalDTO
     * @return Terminal Entity
     */
    private Terminal convertToTerminal(TerminalDTO terminalDTO)
    {
        return modelMapper.map(terminalDTO,Terminal.class);
    }
}
