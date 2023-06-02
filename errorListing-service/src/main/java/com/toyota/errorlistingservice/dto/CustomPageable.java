package com.toyota.errorlistingservice.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CustomPageable {
    private int pageNumber;
    private int pageSize;
    private long totalElements;
    private int totalPages;

    public CustomPageable(int pageNumber, int pageSize,int totalPages,long totalElements) {
        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
        this.totalPages=totalPages;
        this.totalElements = totalElements;
    }
}
