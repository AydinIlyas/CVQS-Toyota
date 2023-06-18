package com.toyota.errorloginservice.dto;

import lombok.*;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PaginationResponse<T> {
    private List<T> content;
    private CustomPageable pageable;

    public PaginationResponse(List<T> content,Page page) {
        this.content = content;
        this.pageable = new CustomPageable(page.getPageable().getPageNumber(),page.getPageable().getPageSize(),
                page.getTotalPages(),page.getTotalElements());
    }
}
