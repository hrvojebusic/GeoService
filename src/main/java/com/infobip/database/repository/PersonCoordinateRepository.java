package com.infobip.database.repository;

import com.infobip.database.model.PersonCoordinate;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PersonCoordinateRepository extends MongoRepository<PersonCoordinate, String> {
}
