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
     * @param depName Filter for the field name.
     * @param isActive Filter for the field isActive
     * @param sortBy Which field to sort by
     * @param sortDirection Sort Direction (ASC/DESC)
     * @return Page of terminals
     */
    @Override
    public Page<TerminalDTO> getTerminals(int page,int size,String depName,String depCode,String shopCode
            ,boolean isActive,String sortBy,String sortDirection)
    {
        Pageable pageable= PageRequest.of(page,size,Sort.by(createSortOrder(sortBy,sortDirection)));
        Page<Terminal> terminals=terminalRepository.getTerminalsFiltered(depName,depCode,shopCode,isActive,pageable);
        logger.info("Fetched terminals. Page: {}, Size: {}, Sorted By: {}, Total Pages: {}," +
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
        logger.info("Creating Terminal. Department name: {}",terminalDTO.getDepName());
        Terminal terminal=convertToTerminal(terminalDTO);
        terminal.setActive(true);
        Terminal saved=terminalRepository.save(terminal);
        logger.info("Terminal created successfully! Department Code: {}",saved.getDepCode());
    }

    /**
     * Activates Terminal
     * @param depCode department code of terminal to be activated.
     */
    @Override
    public void activateTerminal(String depCode) {
        logger.info("Activating terminal. Department Code: {}",depCode);
        Optional<Terminal> optionalTerminal=terminalRepository.findByDepCode(depCode);
        if(optionalTerminal.isPresent())
        {
            Terminal terminal=optionalTerminal.get();
            terminal.setActive(true);
            terminalRepository.save(terminal);
            logger.info("Terminal activated successfully. Department Code: {}",depCode);
        }
        else{
            logger.warn("Terminal not found! Department Code: {}",depCode);
            throw new TerminalNotFoundException("Terminal Not Found! Department Code: "+depCode);
        }


    }

    /**
     * Disables Terminal
     * @param depCode department code of terminal to be disabled
     */
    @Override
    public void disableTerminal(String depCode) {
        logger.info("Disabling terminal. Department Code: {}",depCode);
        Optional<Terminal> optionalTerminal=terminalRepository.findByDepCode(depCode);

        if(optionalTerminal.isPresent())
        {
            Terminal terminal=optionalTerminal.get();
            terminal.setActive(false);
            terminalRepository.save(terminal);
            logger.info("Terminal disabled successfully");
        }
        else{
            logger.warn("Terminal not found! Department Code: {}",depCode);
            throw new TerminalNotFoundException("Terminal not found! Department Code: "+depCode);
        }
    }

    /**
     * Soft deletes Terminal
     * @param depCode department code of terminal to be softly deleted.
     */
    @Override
    public void delete(String depCode) {
        logger.info("Deleting terminal. Department Code: {}",depCode);
        Optional<Terminal> optionalTerminal=terminalRepository.findByDepCode(depCode);
        if(optionalTerminal.isPresent())
        {
            Terminal terminal=optionalTerminal.get();
            terminal.setDeleted(true);
            terminalRepository.save(terminal);
            logger.info("Terminal deleted successfully. Departmend Code: {}",depCode);
        }
        else{
            logger.warn("Terminal not found! Department Code: {}",depCode);
            throw new TerminalNotFoundException("Terminal Not Found! Department Code: "+depCode);
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
