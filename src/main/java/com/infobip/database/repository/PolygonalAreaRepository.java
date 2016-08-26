package com.infobip.database.repository;

import com.infobip.database.model.PolygonalArea;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.Repository;
import org.springframework.scheduling.annotation.Async;
import org.springframework.util.concurrent.ListenableFuture;

import java.util.List;

public interface PolygonalAreaRepository extends Repository<PolygonalArea, String> {

    @Async
    ListenableFuture<List<PolygonalArea>> findAll();

    @Async
    ListenableFuture<PolygonalArea> save(PolygonalArea area);

    @Async
    void delete(String id);
}
