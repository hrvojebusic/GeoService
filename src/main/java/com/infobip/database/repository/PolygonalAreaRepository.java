package com.infobip.database.repository;

import com.infobip.database.model.PolygonalArea;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PolygonalAreaRepository extends MongoRepository<PolygonalArea, String> {

}
