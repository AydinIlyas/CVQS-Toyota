package com.toyota.terminalservice.dto;

import jakarta.validation.constraints.NotNull;

/**
 * Terminal for data transfer
 */
public class TerminalDTO {
    private String id;
    @NotNull
    private String name;

    public TerminalDTO() {
    }

    public TerminalDTO(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
