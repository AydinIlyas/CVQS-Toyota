package com.toyota.terminalservice.service.impl;

import com.toyota.terminalservice.dao.TerminalRepository;
import com.toyota.terminalservice.domain.Terminal;
import com.toyota.terminalservice.dto.TerminalDTO;
import com.toyota.terminalservice.exception.TerminalAlreadyExistsException;
import com.toyota.terminalservice.exception.TerminalNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TerminalServiceImplTest {

    @Mock
    private TerminalRepository terminalRepository;
    TerminalServiceImpl terminalService;

    @BeforeEach
    void setUp() {
        terminalService=new TerminalServiceImpl(terminalRepository,new ModelMapper());
    }

    @Test
    void getTerminals_ASC() {
        //given
        int page=0;
        int size=5;
        String depName="depName";
        String depCode="depCode";
        String shopCode="shopCode";
        boolean isActive=true;
        String sortBy="depName";
        String sortDirection="ASC";
        List<Terminal> content=List.of(new Terminal());
        Sort.Order sort=new Sort.Order(Sort.Direction.ASC,sortBy);
        Pageable pageable= PageRequest.of(page,size,Sort.by(sort));
        Page<Terminal> pageMock=new PageImpl<>(content,pageable,1);

        //when
        when(terminalRepository.getTerminalsFiltered(anyString(),anyString(),anyString(),anyBoolean(),any()))
                .thenReturn(pageMock);
        Page<TerminalDTO> result=terminalService.getTerminals(page,size,depName,depCode,shopCode,isActive,sortBy
                ,sortDirection);

        //then
        assertEquals(page,result.getPageable().getPageNumber());
        assertEquals(size,result.getPageable().getPageSize());
        assertEquals(TerminalDTO.class,result.getContent().get(0).getClass());
    }
    @Test
    void getTerminals_DESC() {
        //given
        int page=0;
        int size=5;
        String depName="depName";
        String depCode="depCode";
        String shopCode="shopCode";
        boolean isActive=true;
        String sortBy="depName";
        String sortDirection="DESC";
        List<Terminal> content=List.of(new Terminal());
        Sort.Order sort=new Sort.Order(Sort.Direction.ASC,sortBy);
        Pageable pageable= PageRequest.of(page,size,Sort.by(sort));
        Page<Terminal> pageMock=new PageImpl<>(content,pageable,1);

        //when
        when(terminalRepository.getTerminalsFiltered(anyString(),anyString(),anyString(),anyBoolean(),any()))
                .thenReturn(pageMock);
        Page<TerminalDTO> result=terminalService.getTerminals(page,size,depName,depCode,shopCode,isActive,sortBy
                ,sortDirection);

        //then
        assertEquals(page,result.getPageable().getPageNumber());
        assertEquals(size,result.getPageable().getPageSize());
        assertEquals(TerminalDTO.class,result.getContent().get(0).getClass());
    }

    @Test
    void createTerminal() {
        //given
        TerminalDTO terminalDTO=new TerminalDTO("A","B","C");
        //when
        when(terminalRepository.existsByDepCodeAndDeletedIsFalse(anyString())).thenReturn(false);
        when(terminalRepository.save(any(Terminal.class)))
                .thenAnswer(invocationOnMock -> invocationOnMock.getArgument(0));
        terminalService.createTerminal(terminalDTO);
        //then
        verify(terminalRepository).save(any(Terminal.class));

    }

    @Test
    void createTerminal_AlreadyExists() {
        //given
        TerminalDTO terminalDTO=new TerminalDTO();
        terminalDTO.setDepCode("A");
        //when
        when(terminalRepository.existsByDepCodeAndDeletedIsFalse(anyString())).thenReturn(true);
        //then
        assertThrows(TerminalAlreadyExistsException.class,()->terminalService.createTerminal(terminalDTO));

    }
    @Test
    void activateTerminal() {
        //given
        String depCode="1";
        Terminal terminal=new Terminal();
        //when
        when(terminalRepository.findByDepCodeAndDeletedIsFalse(anyString())).thenReturn(Optional.of(terminal));
        when(terminalRepository.save(any(Terminal.class)))
                .thenAnswer(invocationOnMock -> invocationOnMock.getArgument(0));
        terminalService.activateTerminal(depCode);

        //then
        verify(terminalRepository).save(terminal);
        assertTrue(terminal.isActive());

    }
    @Test
    void activateTerminal_NotFound() {
        //when
        when(terminalRepository.findByDepCodeAndDeletedIsFalse(anyString())).thenReturn(Optional.empty());

        //then
        assertThrows(TerminalNotFoundException.class,()->terminalService.activateTerminal(anyString()));

    }

    @Test
    void disableTerminal() {
        //given
        String depCode="1";
        Terminal terminal=new Terminal();
        terminal.setActive(true);
        //when
        when(terminalRepository.findByDepCodeAndDeletedIsFalse(anyString())).thenReturn(Optional.of(terminal));
        when(terminalRepository.save(any(Terminal.class)))
                .thenAnswer(invocationOnMock -> invocationOnMock.getArgument(0));
        terminalService.disableTerminal(depCode);

        //then
        verify(terminalRepository).save(terminal);
        assertFalse(terminal.isActive());
    }
    @Test
    void disableTerminal_NotFound() {
        //when
        when(terminalRepository.findByDepCodeAndDeletedIsFalse(anyString())).thenReturn(Optional.empty());

        //then
        assertThrows(TerminalNotFoundException.class,()->terminalService.disableTerminal(anyString()));
    }

    @Test
    void delete() {
        //given
        String depCode="1";
        Terminal terminal=new Terminal();
        //when
        when(terminalRepository.findByDepCodeAndDeletedIsFalse(anyString())).thenReturn(Optional.of(terminal));
        when(terminalRepository.save(any(Terminal.class)))
                .thenAnswer(invocationOnMock -> invocationOnMock.getArgument(0));
        terminalService.delete(depCode);

        //then
        verify(terminalRepository).save(terminal);
        assertTrue(terminal.isDeleted());
    }
    @Test
    void delete_NotFound() {
        //when
        when(terminalRepository.findByDepCodeAndDeletedIsFalse(anyString())).thenReturn(Optional.empty());
        //then

        assertThrows(TerminalNotFoundException.class
                ,()->terminalService.delete(anyString()));
    }
}