package com.toyota.terminalservice.resource;

import com.toyota.terminalservice.dto.TerminalDTO;
import com.toyota.terminalservice.service.abstracts.TerminalService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.swing.text.html.parser.Entity;

/**
 * Controller class for Terminals. Handles requests related terminals.
 */
@RestController
@RequestMapping("/terminal")
@RequiredArgsConstructor
public class TerminalController {

    private final TerminalService terminalService;

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
    @GetMapping("/getAll")
    public Page<TerminalDTO> getActiveTerminals(@RequestParam(defaultValue = "0") int page,
                                                @RequestParam(defaultValue = "5") int size,
                                                @RequestParam(defaultValue = "") String name,
                                                @RequestParam(defaultValue = "true") boolean isActive,
                                                @RequestParam(defaultValue = "name")String sortBy,
                                                @RequestParam(defaultValue="ASC") String sortDirection)
    {
        return terminalService.getActiveTerminals(page,size,name,isActive,sortBy,sortDirection);
    }

    /**
     * Creates Terminal in database
     * @param terminalDTO Input for terminal.
     * @return ResponseEntity<Entity> with status.
     */
    @PostMapping("/addTerminal")
    public ResponseEntity<Entity> createTerminal(@RequestBody @Valid TerminalDTO terminalDTO)
    {

        terminalService.createTerminal(terminalDTO);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * @param id ID of terminal to be activated.
     * @return ResponseEntity<Entity> with status
     */
    @PutMapping("/activate")
    public ResponseEntity<Entity> activateTerminal(@RequestBody Long id)
    {

        terminalService.activateTerminal(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    /**
     * @param id ID of terminal to be disabled
     * @return ResponseEntity<Entity> with status
     */
    @PutMapping("/disable")
    public ResponseEntity<Entity> disableTerminal(@RequestBody Long id)
    {
        terminalService.disableTerminal(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    /**
     * @param id ID of terminal to be soft deleted
     * @return ResponseEntity<Entity> with status
     */
    @PutMapping("/delete")
    public ResponseEntity<Entity> deleteTerminal(@RequestBody Long id){
        terminalService.delete(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

}
