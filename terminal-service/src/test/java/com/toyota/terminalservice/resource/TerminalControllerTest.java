package com.toyota.terminalservice.resource;

import com.toyota.terminalservice.dto.TerminalDTO;
import com.toyota.terminalservice.service.abstracts.TerminalService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.swing.text.html.parser.Entity;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TerminalControllerTest {

    @Mock
    TerminalService terminalService;

    @InjectMocks
    TerminalController terminalController;
    @Test
    void getActiveTerminals() {
        //given
        int page=0;
        int size=5;
        String depName="depName";
        String depCode="depCode";
        String shopCode="shopCode";
        boolean isActive=true;
        String sortBy="depName";
        String sortDirection="ASC";
        List<TerminalDTO> content=List.of(new TerminalDTO());
        //when
        Page<TerminalDTO> pageMock=new PageImpl<>(content);
        when(terminalService.getTerminals(anyInt(),anyInt(),anyString(),anyString(),anyString(),anyBoolean()
                ,anyString(),anyString()))
                .thenReturn(pageMock);
        Page<TerminalDTO> result=terminalController.getActiveTerminals
                (page,size,depName,depCode,shopCode,isActive,sortBy,sortDirection);
        //then
        verify(terminalService).getTerminals(anyInt(),anyInt(),anyString(),anyString(),anyString(),anyBoolean()
                ,anyString(),anyString());
        assertEquals(result,pageMock);
        assertEquals(content,pageMock.getContent());

    }

    @Test
    void createTerminal() {
        //given
        TerminalDTO terminalDTO=new TerminalDTO();

        //when
        ResponseEntity<Entity> response=terminalController.createTerminal(terminalDTO);

        //then
        assertEquals(HttpStatus.CREATED,response.getStatusCode());
    }

    @Test
    void activateTerminal() {
        //given
        String depCode="depCode";

        //when
        ResponseEntity<Entity> response=terminalController.activateTerminal(depCode);

        //then
        assertEquals(HttpStatus.OK,response.getStatusCode());
    }

    @Test
    void disableTerminal() {
        //given
        String depCode="depCode";

        //when
        ResponseEntity<Entity> response=terminalController.disableTerminal(depCode);

        //then
        assertEquals(HttpStatus.OK,response.getStatusCode());
    }

    @Test
    void deleteTerminal() {
        //given
        String depCode="depCode";

        //when
        ResponseEntity<Entity> response=terminalController.deleteTerminal(depCode);

        //then
        assertEquals(HttpStatus.OK,response.getStatusCode());
    }
}