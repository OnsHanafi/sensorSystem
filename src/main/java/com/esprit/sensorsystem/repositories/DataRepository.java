package com.esprit.sensorsystem.repositories;

import com.esprit.sensorsystem.entities.Data;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface DataRepository extends MongoRepository<Data,String> {

    @Query("{ $expr: { $and: [ { $gte: [ '$reading', { $multiply: [ '$threshold', 0.8 ] } ] }, { $lte: [ '$reading', { $multiply: [ '$threshold', 1.2 ] } ] } ] } }")
    List<Data> findCorrectReadings();
}
