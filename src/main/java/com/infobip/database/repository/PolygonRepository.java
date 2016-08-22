package com.infobip.database.repository;

import com.infobip.database.model.Polygon;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PolygonRepository extends MongoRepository<Polygon, String> {
}
