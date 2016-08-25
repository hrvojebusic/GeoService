package com.infobip.config;

import com.infobip.database.model.PhoneLocation;
import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import com.mongodb.async.client.MongoCollection;
import com.mongodb.async.client.MongoDatabase;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories
public class MongoConfiguration extends AbstractMongoConfiguration {

    @Override
    protected String getDatabaseName() {
        return "geoDB";
    }

    @Override
    public Mongo mongo() throws Exception {
        MongoClient mongoClient = new MongoClient("127.0.0.1", 27017);

        mongoClient.getDB("geoDB").getCollection("phones");

        return mongoClient;
    }

    @Override
    protected String getMappingBasePackage() {
        return "com.infobip";
    }
}
