package com.esprit.sensorsystem.services;

import com.esprit.sensorsystem.entities.Data;
import com.esprit.sensorsystem.repositories.DataRepository;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class DataFilteringService {

    @Autowired
    private DataRepository dataRepository;

    private static final Logger LOG = LoggerFactory.getLogger(DataProcessingService.class);
    private static final String NO_READINGS = "NO CORRECT READINGS ALL FALSE";

    @Scheduled(cron = "*/15 * * * * *")
    public void filterDataUsingStreams() throws Exception {
        List<Data> data = dataRepository.findAll();
        List<Data> correctReadings = data.stream().filter(this::isCorrect).collect(Collectors.toList());
        if (correctReadings != null && !(correctReadings.isEmpty())) {
            int fileNumber = 0;
            String fileName = String.format("filtered_data_stream_%d.csv", fileNumber);
            File filteredDirectory = ResourceUtils.getFile("sensorData/filtered");
            while (new File(filteredDirectory, fileName).exists()) {
                fileNumber++;
                fileName = String.format("filtered_data_stream_%d.csv", fileNumber);
            }
            System.out.println( fileName + " added to filtered directory");
            saveFilteredData(correctReadings,new File(filteredDirectory, fileName).getAbsolutePath());
        } else {
            LOG.info(NO_READINGS);
        }
    }


    @Scheduled(cron = "*/15 * * * * *")
    public void filterUsingDatabaseCall(){
        try {
        List<Data> correctReadings = dataRepository.findCorrectReadings();
        int fileNumber = 0;
        String fileName = String.format("filtered_data_DB_%d.csv", fileNumber);
        File filteredDirectory = ResourceUtils.getFile("sensorData/filtered");
        while (new File(filteredDirectory, fileName).exists()) {
            fileNumber++;
            fileName = String.format("filtered_data_DB_%d.csv", fileNumber);
        }
        System.out.println( fileName + " added to filtered directory");
        saveFilteredData(correctReadings,new File(filteredDirectory, fileName).getAbsolutePath());
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private boolean isCorrect(Data data) {
        double threshold = data.getThreshold();
        double range = threshold*0.2;
        //checking the correct range 20%
        return (data.getReading() >= threshold-range) && (data.getReading() <= threshold+range);
    }

    private void saveFilteredData(List<Data> correctReadings,String path) {

        try {
            FileWriter fw = new FileWriter(path);
            //adding header first
            fw.append("sensorId,reading,threshold\n");
            //adfing data
            for (Data reading : correctReadings) {
                fw.append(String.format("%s,%.2f,%.2f\n", reading.getSensorId(), reading.getReading(), reading.getThreshold()));
            }
            fw.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }




}
