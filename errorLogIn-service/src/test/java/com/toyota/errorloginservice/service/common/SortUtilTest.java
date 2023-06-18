package com.toyota.errorloginservice.service.common;

import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Sort;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SortUtilTest {

    @Test
    void createSortOrderAsc() {
        // given
        List<String> sortList = Arrays.asList("field1", "field2", "field3");
        String sortDirection = "Asc";

        // when
        List<Sort.Order> sortOrder = SortUtil.createSortOrder(sortList, sortDirection);

        // then
        assertEquals(sortList.size(), sortOrder.size());
        for (int i = 0; i < sortList.size(); i++) {
            assertEquals(sortList.get(i), sortOrder.get(i).getProperty());
            assertEquals(Sort.Direction.ASC, sortOrder.get(i).getDirection());
        }
    }
    @Test
    void createSortOrderDesc() {
        // given
        List<String> sortList = Arrays.asList("field1", "field2", "field3");
        String sortDirection = "Desc";

        // when
        List<Sort.Order> sortOrder = SortUtil.createSortOrder(sortList, sortDirection);

        // then
        assertEquals(sortList.size(), sortOrder.size());
        for (int i = 0; i < sortList.size(); i++) {
            assertEquals(sortList.get(i), sortOrder.get(i).getProperty());
            assertEquals(Sort.Direction.DESC, sortOrder.get(i).getDirection());
        }
    }
}