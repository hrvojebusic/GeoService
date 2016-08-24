package com.infobip.gateway.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
public class SingleMessageReport {

    private List<MessageReport> messages = new ArrayList<>();

    protected SingleMessageReport() {}
}
