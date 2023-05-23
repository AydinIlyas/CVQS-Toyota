package com.toyota.terminalservice.service.abstracts;

import com.toyota.terminalservice.dto.TerminalDTO;

import java.util.List;

public interface TerminalService {
    List<TerminalDTO> getActiveTerminals();
    void addActiveTerminal(String name);

    void activateTerminal(Long id);

    void disableTerminal(Long id);
}
