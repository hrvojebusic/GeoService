package com.infobip;

import com.infobip.database.repository.PhoneLocationRepository;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

//ustedi nam anotacije za svaki test
@Profile("test")
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
public class IntegrationTestBase {

    @Autowired
    protected PhoneLocationRepository phoneLocationRepository;

    @Before
    public void setupIntegrationTestBase() {
        phoneLocationRepository.deleteAll();
    }
}
