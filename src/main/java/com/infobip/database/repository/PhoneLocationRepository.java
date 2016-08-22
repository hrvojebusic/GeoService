package com.infobip.database.repository;

import com.infobip.database.model.PhoneLocation;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PhoneLocationRepository extends MongoRepository<PhoneLocation, String> {
}
