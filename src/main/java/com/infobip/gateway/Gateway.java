package com.infobip.gateway;

import com.infobip.controllers.model.MessageRequestReport;

import java.io.IOException;
import java.util.Collection;

public interface Gateway {

    MessageRequestReport push(Collection<GatewayRequest> requests);
}
