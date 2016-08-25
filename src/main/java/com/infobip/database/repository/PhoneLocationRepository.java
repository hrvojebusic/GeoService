package com.infobip.database.repository;

import com.infobip.database.model.PhoneLocation;
import org.springframework.data.geo.Polygon;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.scheduling.annotation.Async;
import org.springframework.util.concurrent.ListenableFuture;

import java.util.List;

public interface PhoneLocationRepository extends MongoRepository<PhoneLocation, String> {

    @Async
    ListenableFuture<List<PhoneLocation>> findByLocationWithin(Polygon polygon);

    @Async
    ListenableFuture<PhoneLocation> findById(String id);
}
