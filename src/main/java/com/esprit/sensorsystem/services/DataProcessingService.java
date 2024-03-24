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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class DataProcessingService {

    @Autowired
    private final DataRepository dataRepository;
    private static final Logger LOG = LoggerFactory.getLogger(DataProcessingService.class);
    private  static  final String FILE_NOT_FOUND = "NO CSV FILES";
    private  static  final String NO_VALID_DATA = "NO VALID DATA";

    @Scheduled(cron = "*/14 * * * * *")
    public void processData(){
        try {
            File newDirectory = ResourceUtils.getFile("sensorData/new");
            File[] dataFiles = newDirectory.listFiles(((dir, name) -> name.endsWith(".csv")));
            if (dataFiles != null && dataFiles.length != 0){
                for (File file : dataFiles){
                    saveData(file);
                    moveFileToArchive(file);
                }
            }else {
                LOG.info(FILE_NOT_FOUND);
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    private void saveData(File file) {
        try(BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            List<Data> dataList = new ArrayList<>();
            // to skip the header
            br.readLine();

            while ((line = br.readLine())!= null){
                String[] attributes = line.split(",");
                if (attributes.length == 3){
                    String sensorId = attributes[0];
                    Double reading = Double.parseDouble(attributes[1]);
                    Double threshold = Double.parseDouble(attributes[2]);
                    //creating the new object data
                    Data newData = new Data(sensorId,reading,threshold);
                    dataList.add(newData);
                }else {
                    LOG.info(NO_VALID_DATA);
                }
            }
            //save to database
            dataRepository.saveAll(dataList);

        }catch (IOException e ){
            e.printStackTrace();
        }
    }
    private void moveFileToArchive(File file) {
        try {
            File archiveDirectory = ResourceUtils.getFile("sensorData/archive");
            Files.move(file.toPath(), archiveDirectory.toPath().resolve(file.getName()), StandardCopyOption.REPLACE_EXISTING);
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public boolean checkFilesinNewDirectory() throws Exception{
            File newDirectory = ResourceUtils.getFile("sensorData/new");
            File[] dataFiles = newDirectory.listFiles(((dir, name) -> name.endsWith(".csv")));
            if (dataFiles != null && dataFiles.length > 0){
                return true;
            }else {
                return false;
            }

    }


}
