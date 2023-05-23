package com.toyota.terminalservice.dto;

public class TerminalDTO {
    private String name;

    public TerminalDTO() {
    }

    public TerminalDTO(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
