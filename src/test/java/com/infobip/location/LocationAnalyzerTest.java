package com.infobip.location;

import com.infobip.WebIntegrationTestBase;
import com.infobip.controllers.model.resource.LocationResource;
import com.infobip.controllers.model.resource.PhoneLocationResource;
import com.infobip.controllers.model.resource.PolygonResource;
import com.infobip.database.model.PhoneLocation;
import com.infobip.database.model.Subscriber;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class LocationAnalyzerTest extends WebIntegrationTestBase {

    @Autowired
    private LocationAnalyzer locationAnalyzer;

    /*
    @Test
    public void testEmpty() throws Exception {
        phoneLocationRepository.deleteAll();

        PolygonResource polygonResource = getTestPolygonResource();
        List<PhoneLocationResource> result = locationAnalyzer.getPersonsForPolygon(polygonResource);
        assertThat(result.size()).isEqualTo(0);
    }

    @Test
    public void testWithContent() throws Exception {
        phoneLocationRepository.deleteAll();
        phoneLocationRepository.save(getTestPhoneLocationList());

        PolygonResource polygonResource = getTestPolygonResource();
        List<PhoneLocationResource> result = locationAnalyzer.getPersonsForPolygon(polygonResource);
        assertThat(result.size()).isEqualTo(2);
    }
    */

    private List<PhoneLocation> getTestPhoneLocationList() {
        return Arrays.asList(
                getPhoneLocation(new Subscriber(Collections.emptyMap()), 385912121212L, new GeoJsonPoint(15, 15), new Date()),
                getPhoneLocation(new Subscriber(Collections.emptyMap()), 385992323232L, new GeoJsonPoint(1, 4), new Date()),
                getPhoneLocation(new Subscriber(Collections.emptyMap()), 385982424242L, new GeoJsonPoint(4, 1), new Date()),
                getPhoneLocation(new Subscriber(Collections.emptyMap()), 385952525252L, new GeoJsonPoint(12, 12), new Date())
        );
    }

    private PolygonResource getTestPolygonResource() {
        return new PolygonResource(Arrays.asList(
                getLocationResource(1, 4),
                getLocationResource(4, 1),
                getLocationResource(1, 1)
        ));
    }

    private PhoneLocation getPhoneLocation(Subscriber subscriber, Long number, GeoJsonPoint location, Date updated) {
        return new PhoneLocation(subscriber, number, location, updated);
    }

    private LocationResource getLocationResource(double x, double y) {
        return new LocationResource(x, y);
    }
}