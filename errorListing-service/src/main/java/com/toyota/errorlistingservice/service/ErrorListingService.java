package com.toyota.errorlistingservice.service;

import com.toyota.errorlistingservice.dao.ErrorsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ErrorListingService {
    private final ErrorsRepository repository;

//    public List<TTVehicle> getAll()
//    {
//        return repository.findAll();
//    }

}
