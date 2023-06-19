package com.toyota.terminalservice.dto;

import jakarta.validation.constraints.NotNull;

/**
 * Terminal for data transfer
 */
public class TerminalDTO {
    @NotNull
    private String depName;
    private String depCode;
    private String shopCode;

    public TerminalDTO() {
    }


    public TerminalDTO(String depName, String depCode, String shopCode) {
        this.depName = depName;
        this.depCode = depCode;
        this.shopCode = shopCode;
    }

    public String getDepName() {
        return depName;
    }

    public void setDepName(String depName) {
        this.depName = depName;
    }

    public String getDepCode() {
        return depCode;
    }

    public void setDepCode(String depCode) {
        this.depCode = depCode;
    }

    public String getShopCode() {
        return shopCode;
    }

    public void setShopCode(String shopCode) {
        this.shopCode = shopCode;
    }
}
