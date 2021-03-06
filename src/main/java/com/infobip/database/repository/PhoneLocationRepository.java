package com.infobip.database.repository;

import com.infobip.database.model.PhoneLocation;
import org.springframework.data.geo.Polygon;
import org.springframework.data.repository.Repository;
import org.springframework.scheduling.annotation.Async;
import org.springframework.util.concurrent.ListenableFuture;

import java.util.List;

public interface PhoneLocationRepository extends Repository<PhoneLocation, String> {

    @Async
    ListenableFuture<PhoneLocation> findById(String id);

    @Async
    ListenableFuture<List<PhoneLocation>> findAll();

    @Async
    ListenableFuture<PhoneLocation> save(PhoneLocation phoneLocation);

    @Async
    ListenableFuture<List<PhoneLocation>> findByLocationWithin(Polygon polygon);

    @Async
    ListenableFuture<Void> delete(String id);

    @Async
    ListenableFuture<Void> deleteAll();
}
