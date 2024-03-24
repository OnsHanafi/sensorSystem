# Spring Boot Sensor Data Processing System 

This Spring Boot application is designed to process sensor data efficiently. It focuses on utilizing Java Stream API, Maps, and scheduled tasks to handle sensor data effectively using 
a mongoDB database 

****************** 
# System Description
The system simulates the reception of sensor data, which is stored in a CSV file with three essential columns: sensorId, reading, and threshold. The threshold value serves as a reference point for comparison with the actual sensor readings. This comparison helps determine whether a reading falls within the expected range or if it's considered an outlier.

If a reading deviates by 20 percent or more from the threshold value, it's flagged as incorrect. This threshold-based approach ensures that only valid sensor readings are processed further.

# Functionalities
- A directory "sensorData" directory containing subdirectories "new", "Archived", and "filtered" for storing csv data files .
- Spring cron job to process CSV files in the "new" folder, saving data to the database, and then moving the files to the "Archived" folder.
- A cron job to filter correct readings and store them in a new CSV file under the "filtered" directory.
  => Implemented using two methods: streams and direct database call .
- A statistics cron job to display the percentage of correct readings in the console.


