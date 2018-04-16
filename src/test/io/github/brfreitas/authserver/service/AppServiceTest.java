package io.github.brfreitas.authserver.service;

import io.github.brfreitas.authserver.Application;
import io.github.brfreitas.authserver.domain.App;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
@RunWith(SpringRunner.class)
@EnableAutoConfiguration
@ContextConfiguration(classes = Application.class)
@PropertySource("classpath:application.properties")
@EnableWebSecurity
public class AppServiceTest {

    @Autowired
    private AppService AppService;

    @Autowired
    private ObjectMapper objectMapper;

    private JacksonTester<App> jsonTester;



    @Before
    public void setup() {
        JacksonTester.initFields(this, objectMapper);
    }

    @Test
    public void validateValidApp() {
        App App = new App();
        App.setName("Teste");
        Assert.assertTrue(AppService.isValid(App));
    }

    @Test
    public void validateNotValidApp() {
        App App = new App();
        Assert.assertFalse(AppService.isValid(App));
    }

    @Test
    public void generateAppId() {
        App App = new App();
        Assert.assertEquals(24, AppService.generateSecretKey().length());
    }
}
