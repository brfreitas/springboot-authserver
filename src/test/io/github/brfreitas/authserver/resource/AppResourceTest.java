package io.github.brfreitas.authserver.resource;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import io.github.brfreitas.authserver.Application;
import io.github.brfreitas.authserver.domain.App;
import io.github.brfreitas.authserver.service.AppService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@RunWith(SpringRunner.class)
@EnableAutoConfiguration
@ContextConfiguration(classes = Application.class)
@PropertySource("classpath:application.properties")
@EnableWebSecurity
public class AppResourceTest {
    private MockMvc mockMvc;

    @Autowired
    private AppService AppService;

    @Autowired
    private ObjectMapper objectMapper;

    private JacksonTester<App> jsonTester;



    @Before
    public void setup() {
        this.mockMvc =  MockMvcBuilders.standaloneSetup(new AppResource()).build();
        JacksonTester.initFields(this, objectMapper);
    }

    @Test
    public void saveNewApp() throws Exception {
        App app = new App();
        app.setName("app test");
        app.setDescription("app description");

        this.mockMvc.perform(post("/app")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(app)))
        .andExpect(status().isOk());
    }
}
