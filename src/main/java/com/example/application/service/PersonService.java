package com.example.application.service;

import com.example.application.entity.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.application.repository.PersonRepository;

import java.util.List;

@Service
public class PersonService {
    private final PersonRepository personRepository;

    @Autowired
    public PersonService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    public List<Person> findAll() {
        return personRepository.findAll();
    }

    public List<Person> findByLastNameContainingIgnoreCase(String lastName) {
        return personRepository.findByLastNameContainingIgnoreCase(lastName);
    }

    public Person save(Person person) {
        return personRepository.save(person);
    }
}

