package com.infobip.database.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Location {

    protected Location() {}

    private double x, y;
}
