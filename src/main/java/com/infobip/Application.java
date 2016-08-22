package com.infobip;

import com.infobip.database.repository.PhoneLocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableMongoRepositories
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class);
    }

    @Autowired
    private PhoneLocationRepository phoneLocationRepository;

//    @PostConstruct
//    private void init(){
//        PhoneLocation personCoordinate = new PhoneLocation(233434L, new Location(2.00,3.00), Calendar.getInstance().getTime());
//        phoneLocationRepository.save(personCoordinate);
//        System.out.println(phoneLocationRepository.findAll());
//    }
}
