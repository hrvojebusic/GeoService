package com.infobip.database.repository;

import com.infobip.database.model.PolygonEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PolygonRepository extends MongoRepository<PolygonEntity, String> {
}
