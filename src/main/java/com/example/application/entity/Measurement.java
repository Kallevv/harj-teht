package com.example.application.entity;

import com.vaadin.flow.component.template.Id;
import jakarta.persistence.*;

import java.time.LocalDateTime;


@Entity
public class Measurement {
    @jakarta.persistence.Id
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime timestamp;
    private String type; // "blood_pressure", "weight", etc.
    @Column(name = "measurement_value")
    private String value;


    @ManyToOne
    @JoinColumn(name = "person_id")
    private Person person;

    // Oletuskonstruktori
    public Measurement() {
        this.timestamp = LocalDateTime.now();
    }

    public Measurement(String type, String value, Person person) {
        this();
        this.type = type;
        this.value = value;
        this.person = person;
    }


    // Konstruktori kaikilla kentillä ilman id:tä
    public Measurement(LocalDateTime timestamp, String type, String value, Long personId) {
        this.timestamp = timestamp;
        this.type = type;
        this.value = value;
        this.person = person;
    }

    // Getterit ja setterit
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    // Update getters and setters
    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }
}
