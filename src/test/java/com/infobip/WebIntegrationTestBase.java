package com.infobip;

import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.io.IOException;

@WebIntegrationTest(randomPort = true)
public class WebIntegrationTestBase extends IntegrationTestBase {

    protected MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Before
    public void setupWebIntegrationTestBase() {
        this.mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .build();
    }

    protected String getTestResourceAsString(String relativeFilePath) {
        try {
            return IOUtils.toString(this.getClass().getResourceAsStream(relativeFilePath));
        } catch (IOException e) {
            return null;
        }
    }
}
