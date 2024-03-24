package com.esprit.sensorsystem.controllers;

import com.esprit.sensorsystem.entities.Data;
import com.esprit.sensorsystem.repositories.DataRepository;
import com.esprit.sensorsystem.services.DataFilteringService;
import com.esprit.sensorsystem.services.DataProcessingService;
import com.esprit.sensorsystem.statistics.StatisticsJob;
import com.esprit.sensorsystem.utils.SensorDataGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/sensorData")
public class sensorDataController {

    @Autowired
    private SensorDataGenerator sensorDataGenerator;
    @Autowired
    private DataProcessingService dataProcessingService;
    @Autowired
    private DataRepository dataRepository;
    @Autowired
    private DataFilteringService dataFilteringService;
    @Autowired
    private StatisticsJob statisticsJob;

    @GetMapping("/generate")
    public ResponseEntity<String> generateData() {
        try {
            sensorDataGenerator.dataGenerator();
            return new ResponseEntity<>("Sensor data generated successfully!!", HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Failed to generate sensor data.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/process")
    public ResponseEntity<?> processData() {
        try {
            boolean foundFiles =dataProcessingService.checkFilesinNewDirectory();
            if(foundFiles){
                dataProcessingService.processData();
                return new ResponseEntity<>(dataRepository.findAll(), HttpStatus.OK);
            }else {
                return new ResponseEntity<>("No files found in the 'new' directory", HttpStatus.NOT_FOUND);
            }

        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/filterStream")
    public ResponseEntity<?> filterStream() {
        try {
            dataFilteringService.filterDataUsingStreams();
            return new ResponseEntity<>("data filtered with stream successfully!!", HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Failed to filter data.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/filterDatabase")
    public ResponseEntity<?> filterDatabaseCall() {
        try {
            dataFilteringService.filterUsingDatabaseCall();
            return new ResponseEntity<>("data filtered with database call successfully!!", HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Failed to filter data.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/stats")
    public ResponseEntity<?> stats() {
        try {
            statisticsJob.correctReadingsStatistics();
            return new ResponseEntity<>("stats read successfully!!", HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Failed to get stats.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
