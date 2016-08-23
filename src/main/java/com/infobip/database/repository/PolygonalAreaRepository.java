package com.infobip.database.repository;

import com.infobip.database.model.PolygonalArea;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface PolygonalAreaRepository extends MongoRepository<PolygonalArea, String> {
    List<PolygonalArea> findByUserId(int userId);
}
