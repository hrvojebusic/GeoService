package com.infobip;

import com.infobip.database.model.Location;
import com.infobip.database.model.PersonCoordinate;
import com.infobip.database.repository.PersonCoordinateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import javax.annotation.PostConstruct;
import java.util.Calendar;

@SpringBootApplication
@EnableMongoRepositories
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class);
    }

    @Autowired
    private PersonCoordinateRepository personCoordinateRepository;

//    @PostConstruct
//    private void init(){
//        PersonCoordinate personCoordinate = new PersonCoordinate(233434L, new Location(2.00,3.00), Calendar.getInstance().getTime());
//        personCoordinateRepository.save(personCoordinate);
//        System.out.println(personCoordinateRepository.findAll());
//    }
}
