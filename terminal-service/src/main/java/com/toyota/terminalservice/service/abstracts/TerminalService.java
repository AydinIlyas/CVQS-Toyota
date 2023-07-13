package com.toyota.terminalservice.service.abstracts;

import com.toyota.terminalservice.dto.TerminalDTO;
import org.springframework.data.domain.Page;

/**
 * Interface for TerminalServiceImpl
 */
public interface TerminalService {
    /**
     * Lists Terminals with paging, sorting and filtering.
     * @param page Page to be displayed
     * @param size Size of the page
     * @param depCode Filter for the field depCode.
     * @param depName Filter for the field depName.
     * @param shopCode Filter for the field shopCode.
     * @param isActive Filter for the field isActive
     * @param sortBy Which field to sort by
     * @param sortDirection Sort Direction (ASC/DESC)
     * @return Page of terminals
     */
    Page<TerminalDTO> getTerminals(int page,int size,String depName,String depCode,String shopCode,boolean isActive,
                                         String sortBy,String sortDirection);

    /**
     * Creates terminal
     * @param terminalDTO Terminal to be created in database.
     */
    void createTerminal(TerminalDTO terminalDTO);

    /**
     * Activates Terminal
     * @param depCode department code of terminal to be activated.
     */
    void activateTerminal(String depCode);

    /**
     * Disables Terminal
     * @param depCode department code of terminal to be disabled
     */
    void disableTerminal(String depCode);

    /**
     * Soft deletes Terminal
     * @param depCode department code of terminal to be softly deleted.
     */
    void delete(String depCode);
}
