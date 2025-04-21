package com.example.application.service;

import com.example.application.entity.Measurement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.application.repository.MeasurementRepository;

import java.util.List;

@Service
public class MeasurementService {
    private final MeasurementRepository measurementRepository;

    @Autowired
    public MeasurementService(MeasurementRepository measurementRepository) {
        this.measurementRepository = measurementRepository;
    }

    public List<Measurement> findByPersonId(Long personId) {
        return measurementRepository.findByPersonId(personId);
    }
}
