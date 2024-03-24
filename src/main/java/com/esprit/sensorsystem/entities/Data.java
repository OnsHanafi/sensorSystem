package com.esprit.sensorsystem.entities;

import lombok.AllArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

@lombok.Data
@AllArgsConstructor
@Document
public class Data implements Serializable {

    @Id
    private String sensorId;
    private Double reading;
    private Double threshold;

}
