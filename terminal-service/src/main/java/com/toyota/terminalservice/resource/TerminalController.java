package com.toyota.terminalservice.resource;

import com.toyota.terminalservice.dto.TerminalDTO;
import com.toyota.terminalservice.service.abstracts.TerminalService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/terminal")
@RequiredArgsConstructor
public class TerminalController {

    private final TerminalService terminalService;
    @GetMapping("/getAll")
    public List<TerminalDTO> getActiveTerminals()
    {
        return terminalService.getActiveTerminals();
    }
    @PostMapping("/addTerminal")
    public void addActiveTerminal(@RequestBody String name)
    {
        terminalService.addActiveTerminal(name);
    }

    @PutMapping("/activate")
    public void activateTerminal(@RequestBody Long id)
    {
        terminalService.activateTerminal(id);
    }
    @PutMapping("/disable")
    public void disableTerminal(@RequestBody Long id)
    {
        terminalService.disableTerminal(id);
    }


}
