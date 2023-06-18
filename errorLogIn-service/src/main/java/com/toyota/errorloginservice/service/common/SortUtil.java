package com.toyota.errorloginservice.service.common;

import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.List;

public class SortUtil {
    public static List<Sort.Order> createSortOrder(List<String> sortList, String sortDirection) {
        List<Sort.Order> sorts = new ArrayList<>();
        Sort.Direction direction;
        for (String sort : sortList) {
            if (sortDirection != null&&sortDirection.equalsIgnoreCase("Asc")) {
                direction = Sort.Direction.ASC;
            } else {
                direction = Sort.Direction.DESC;
            }
            sorts.add(new Sort.Order(direction, sort));
        }
        return sorts;
    }
}
