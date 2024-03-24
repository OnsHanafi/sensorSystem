package com.esprit.sensorsystem.statistics;

import com.esprit.sensorsystem.entities.Data;
import com.esprit.sensorsystem.repositories.DataRepository;
import com.esprit.sensorsystem.services.DataProcessingService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
@AllArgsConstructor
public class StatisticsJob {

    @Autowired
    private final DataRepository dataRepository;
    private static final Logger LOG = LoggerFactory.getLogger(StatisticsJob.class);
    private static final String NO_READINGS = "NO AVAILABLE READINGS ";

    @Scheduled(fixedDelay = 5000)
    public void correctReadingsStatistics(){
        List<Data> data = dataRepository.findAll();
        double total = data.size();

        // Get current timestamp
        LocalDateTime currentTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedTime = currentTime.format(formatter);
        double correct = data.stream().filter(this::isCorrect).count();
        if(total != 0){
            double pourcentage =(double) (correct/total)*100;
            System.out.printf("[%s] Pourcentage of correct readings is : %.2f%% \n ",formattedTime,pourcentage);
        }else {
            LOG.info(NO_READINGS);
            System.out.printf("[%s] Pourcentage of correct readings is : 0.00%% \n ",formattedTime);
        }




    }

    private boolean isCorrect(Data data) {
        double threshold = data.getThreshold();
        double range = threshold*0.2;
        //checking the correct range 20%
        return (data.getReading() >= threshold-range) && (data.getReading() <= threshold+range);
    }

}
