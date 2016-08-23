package com.infobip.database.repository;

import com.infobip.database.model.PhoneLocation;
import org.springframework.data.geo.Polygon;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface PhoneLocationRepository extends MongoRepository<PhoneLocation, String> {

    List<PhoneLocation> findByLocationWithin(Polygon polygon);
}
