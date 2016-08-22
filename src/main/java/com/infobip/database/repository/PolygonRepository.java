package com.infobip.database.repository;

import com.infobip.database.model.PersonCoordinate;
import com.infobip.database.model.Polygon;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PolygonRepository extends JpaRepository<Polygon, Integer>{
}
