package com.toyota.terminalservice.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TerminalTest {
    Terminal terminal;
    @BeforeEach
    void setUp()
    {
        terminal=new Terminal(1L,"A","B","C",true,false);
    }

    @Test
    void getId() {
        assertEquals(1L,terminal.getId());
    }

    @Test
    void setId() {
        terminal.setId(2L);
        assertEquals(2L,terminal.getId());
    }
}