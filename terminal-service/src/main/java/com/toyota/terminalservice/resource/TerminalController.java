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
     * @param depName Filter for the field department Name.
     * @param isActive Filter for the field isActive
     * @param sortBy Which field to sort by
     * @param sortDirection Sort Direction (ASC/DESC)
     * @return Page with terminals
     */
    @GetMapping("/getAll")
    public Page<TerminalDTO> getActiveTerminals(@RequestParam(defaultValue = "0") int page,
                                                @RequestParam(defaultValue = "5") int size,
                                                @RequestParam(defaultValue = "") String depName,
                                                @RequestParam(defaultValue = "") String depCode,
                                                @RequestParam(defaultValue = "") String shopCode,
                                                @RequestParam(defaultValue = "true") boolean isActive,
                                                @RequestParam(defaultValue = "depName")String sortBy,
                                                @RequestParam(defaultValue="ASC") String sortDirection)
    {
        return terminalService.getTerminals(page,size,depName,depCode,shopCode,isActive,sortBy,sortDirection);
    }

    /**
     * Creates Terminal in database
     * @param terminalDTO TerminalDTO used as input.
     * @return ResponseEntity with status.
     */
    @PostMapping("/add")
    public ResponseEntity<Entity> createTerminal(@RequestBody @Valid TerminalDTO terminalDTO)
    {

        terminalService.createTerminal(terminalDTO);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * @param depCode department code of terminal to be activated.
     * @return ResponseEntity with status
     */
    @PutMapping("/activate")
    public ResponseEntity<Entity> activateTerminal(@RequestBody String depCode)
    {

        terminalService.activateTerminal(depCode);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    /**
     * @param depCode department code of terminal to be disabled
     * @return ResponseEntity with status
     */
    @PutMapping("/disable")
    public ResponseEntity<Entity> disableTerminal(@RequestBody String depCode)
    {
        terminalService.disableTerminal(depCode);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    /**
     * @param depCode department code of terminal to be soft deleted
     * @return ResponseEntity with status
     */
    @PutMapping("/delete")
    public ResponseEntity<Entity> deleteTerminal(@RequestBody String depCode){
        terminalService.delete(depCode);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

}
