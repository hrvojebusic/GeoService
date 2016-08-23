package com.infobip.controllers;

import com.infobip.database.model.PhoneLocation;
import org.junit.Before;
import org.junit.Test;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.http.MediaType;

import static org.junit.Assert.*;
import org.skyscreamer.jsonassert.JSONAssert;

import java.util.Calendar;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class PhoneLocationControllerTest extends WebIntegrationTestBase{

    private final static String GETALL_RESPONSE = "/getAllResponse.json";
    private final static String GETINAREA_RESPONSE = "/getUsersInAreaResponse.json";

    @Before
    public void setupUsers(){
        phoneLocationRepository.save(new PhoneLocation(385989923327L, new GeoJsonPoint(2,3), Calendar.getInstance().getTime()));
        phoneLocationRepository.save(new PhoneLocation(385981217232L, new GeoJsonPoint(12,31), Calendar.getInstance().getTime()));
        phoneLocationRepository.save(new PhoneLocation(385984444444L, new GeoJsonPoint(4,8), Calendar.getInstance().getTime()));
    }

    @Test
    public void testGetAll() throws Exception {
        String actual = mockMvc.perform(get("/coordinates"))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        System.out.println(actual);
        JSONAssert.assertEquals(getTestResourceAsString(GETALL_RESPONSE), actual, false);
    }

    @Test
    public void testGetUsersInArea() throws Exception {
        String actual = mockMvc.perform(put("/coordinates/users")
                .content("{\"coordinates\":[{\"x\":0,\"y\":0},{\"x\":0,\"y\":5},{\"x\":5,\"y\":0},{\"x\":5,\"y\":5}]}")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        System.out.println(actual);
        JSONAssert.assertEquals(getTestResourceAsString(GETINAREA_RESPONSE), actual, false);
    }

}