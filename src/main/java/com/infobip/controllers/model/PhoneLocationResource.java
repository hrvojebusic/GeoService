package com.infobip.controllers.model;

import com.infobip.database.model.Location;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Setter;

import java.util.Date;

@Data
@AllArgsConstructor
@Setter(AccessLevel.PRIVATE)
public class PhoneLocationResource {

    protected PhoneLocationResource() {}

    private String id;

    private Long number;

    private Location location;

    private Date updated;

}
