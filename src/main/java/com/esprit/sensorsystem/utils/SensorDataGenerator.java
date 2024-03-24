package com.esprit.sensorsystem.utils;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.FileWriter;
import java.util.Random;

@Component //to make the class injectable and managed as a spring bean
public class SensorDataGenerator {

    @Scheduled(cron = "*/10 * * * * *")
    public void dataGenerator() throws Exception{
        final FileWriter csvWriter = new FileWriter("sensorData/new/sensor_data.csv");
        csvWriter.append("sensorId,reading,threshold\n");
        final Random random = new Random();

        final StringBuilder stringBuilder = new StringBuilder();
        String sensorID;
        Double reading = null;
        Double threshold = null;
        for (int i = 1; i <=100 ; i++) {
            sensorID = "hash_"+ i ;
            reading = random.nextDouble()*100;
            threshold = reading + random.nextDouble()*10;

            stringBuilder.setLength(0);
            stringBuilder.append(sensorID).append(",").append(reading).append(",").append(threshold).append("\n");
            csvWriter.append(stringBuilder);
        }

        csvWriter.flush();
        csvWriter.close();
        System.out.println("Sensor data generated and saved to sensor_data.csv");

    }
}
