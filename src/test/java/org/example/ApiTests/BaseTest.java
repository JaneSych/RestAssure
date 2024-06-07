package org.example.ApiTests;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.config.SSLConfig;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.testng.annotations.BeforeTest;

import static io.restassured.config.RestAssuredConfig.config;

public class BaseTest {
    private final RequestSpecification requestSpec = new RequestSpecBuilder()
            .setBaseUri("https://reqres.in/api")
            .setConfig(config().sslConfig(new SSLConfig().relaxedHTTPSValidation()))
            .setContentType(ContentType.JSON)
            .build();

    @BeforeTest
    public void setUP() {
        RestAssured.requestSpecification = requestSpec;
    }
}
