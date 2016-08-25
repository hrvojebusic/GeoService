package com.infobip.database.repository;

import com.infobip.database.model.PhoneLocation;
import org.springframework.data.geo.Polygon;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Repository;
import org.springframework.util.concurrent.ListenableFuture;

import java.util.List;

@Repository(value = "phones")
@EnableAsync
public interface PhoneLocationRepository extends MongoRepository<PhoneLocation, String> {

    @Async
    ListenableFuture<PhoneLocation> findById(String id);

    @Async
    ListenableFuture<List<PhoneLocation>> findByLocationWithin(Polygon polygon);
}
