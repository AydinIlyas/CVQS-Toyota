package com.toyota.errorloginservice.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CustomPageable {
    private int pageNumber;
    private int pageSize;
    private int totalPages;
    private long totalElements;

    public CustomPageable(int pageNumber, int pageSize,int totalPages,long totalElements) {
        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
        this.totalPages=totalPages;
        this.totalElements = totalElements;
    }
}
