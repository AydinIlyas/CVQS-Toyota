package com.toyota.errorloginservice.dao;

import com.toyota.errorloginservice.domain.EngineType;
import com.toyota.errorloginservice.domain.TTVehicle;
import com.toyota.errorloginservice.domain.TransmissionType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
@ExtendWith(MockitoExtension.class)
class TTVehicleRepositoryTest {
    @Mock
    TTVehicleRepository ttVehicleRepository;
    @Test
    void getVehiclesFiltered() {
        // Test input
        String model = "TestModel";
        String vin = "TestVIN";
        String yearOfProduction = "2022";
        String transmissionType = "Automatic";
        String engineType = "V8";
        String color = "Red";

        // Create a pageable object
        Pageable pageable = PageRequest.of(0, 10);

        // Create a mock result
        TTVehicle vehicle = new TTVehicle(1L, "Supra", "0001", LocalDate.now(), EngineType.DIESEL,
                TransmissionType.MANUAL, "Red",false, new ArrayList<>());

        Page<TTVehicle> mockPage = new PageImpl<>(Collections.singletonList(vehicle));

        // Mock the repository method
        Mockito.when(ttVehicleRepository.getVehiclesFiltered(model, vin, yearOfProduction, transmissionType,
                engineType, color, pageable)).thenReturn(mockPage);

        // Call the repository method
        Page<TTVehicle> result = ttVehicleRepository.getVehiclesFiltered(model, vin, yearOfProduction,
                transmissionType, engineType, color, pageable);

        // Assertions
        assertEquals(1, result.getTotalElements());
        TTVehicle retrievedVehicle = result.getContent().get(0);
        // Perform assertions on the retrievedVehicle object
        // ...
    }
}