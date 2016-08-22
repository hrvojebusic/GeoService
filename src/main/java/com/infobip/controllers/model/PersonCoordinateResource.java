package com.infobip.controllers.model;

import com.infobip.database.model.Location;
import com.infobip.database.model.PersonCoordinate;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Setter;
import org.springframework.data.annotation.Id;

import java.util.Date;

@Data
@AllArgsConstructor
@Setter(AccessLevel.PRIVATE)
public class PersonCoordinateResource {

    protected PersonCoordinateResource() {}

    private String id;

    private Long number;

    private Location location;

    private Date updated;

}
