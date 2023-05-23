package com.toyota.terminalservice.service.impl;

import com.toyota.terminalservice.dao.TerminalRepository;
import com.toyota.terminalservice.domain.Terminal;
import com.toyota.terminalservice.dto.TerminalDTO;
import com.toyota.terminalservice.service.abstracts.TerminalService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TerminalServiceImpl implements TerminalService {
    private final TerminalRepository terminalRepository;
    private final ModelMapper modelMapper;
    @Override
    public List<TerminalDTO> getActiveTerminals()
    {
        List<Terminal> terminals=terminalRepository.findAllByIsActiveIsTrue();
        return terminals.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    @Override
    public void addActiveTerminal(String name)
    {
        if(name!=null)
        {
            Terminal terminal=new Terminal();
            terminal.setActive(true);
            terminal.setName(name);
            terminalRepository.save(terminal);
        }
    }

    /**
     * @param id
     */
    @Override
    public void activateTerminal(Long id) {

        Optional<Terminal> optionalTerminal=terminalRepository.findById(id);

        if(optionalTerminal.isPresent())
        {
            Terminal terminal=optionalTerminal.get();
            terminal.setActive(true);
            terminalRepository.save(terminal);
        }
    }

    /**
     * @param id
     */
    @Override
    public void disableTerminal(Long id) {
        Optional<Terminal> optionalTerminal=terminalRepository.findById(id);

        if(optionalTerminal.isPresent())
        {
            Terminal terminal=optionalTerminal.get();
            terminal.setActive(false);
            terminalRepository.save(terminal);
        }
    }

    private TerminalDTO convertToDto(Terminal terminal)
    {
        return modelMapper.map(terminal,TerminalDTO.class);
    }
}
