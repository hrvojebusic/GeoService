package com.infobip.database.repository;

import com.infobip.database.model.PolygonalArea;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PolygonRepository extends MongoRepository<PolygonalArea, String> {
}
