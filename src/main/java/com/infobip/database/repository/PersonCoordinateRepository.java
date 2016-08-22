package com.infobip.database.repository;

import com.infobip.database.model.PersonCoordinate;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonCoordinateRepository extends JpaRepository<PersonCoordinate, Integer>{
}
