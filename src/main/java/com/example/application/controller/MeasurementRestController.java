package com.example.application.controller;

import com.example.application.entity.Measurement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.application.repository.MeasurementRepository;
import com.example.application.repository.PersonRepository;

import java.util.List;

@RestController
@RequestMapping("/api/measurements")
public class MeasurementRestController {

    @Autowired
    private MeasurementRepository measurementRepository;

    @Autowired
    private PersonRepository personRepository;

    @GetMapping
    public List<Measurement> getAll() {
        return measurementRepository.findAll();
    }

    @GetMapping("/person/{personId}")
    public List<Measurement> getByPerson(@PathVariable Long personId) {
        return measurementRepository.findByPersonId(personId);
    }

    // Muut CRUD-operaatiot samaan tapaan
}
