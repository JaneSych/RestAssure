package org.example.ApiTests;

import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.example.Endpoints.Endpoints.LOGIN;
import static org.example.Endpoints.Endpoints.REGISTER;
import static org.example.Endpoints.Endpoints.USERS;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class PostApiTests extends BaseTest {

    @Test(description = "Test POST Create")
    public void testPostCreateUser() {
        String requestBody = "{" +
                "\"name\": \"Po\"," +
                "\"job\": \"waiter\"" +
                "}";

        Response res =
                given()
                        .body(requestBody)
                .when()
                        .post(USERS)
                .then()
                        .log().ifError()
                        .statusCode(201)
                        .extract().response();

        String id = res.jsonPath().getString("id");
        Assert.assertFalse(id.isEmpty(), "Id should not be empty!");
    }

    @Test(description = "Test POST Register Successful")
    public void testPostRegisterSuccessful() {
        String requestBody = "{" +
                "\"email\": \"eve.holt@reqres.in\"," +
                "\"password\": \"pistol\"" +
                "}";

        given()
                .body(requestBody)
        .when()
                .post(REGISTER)
        .then()
                .log().all()
                .assertThat()
                    .statusCode(200)
                    .body("id", notNullValue());

    }

    @Test(description = "Test POST Register Unsuccessful")
    public void testPostRegisterUnsuccessful() {
        String requestBody = "{\"email\": \"eve.holt@reqres.in\"}";

        given()
                .body(requestBody)
        .when()
                .post(REGISTER)
        .then()
                .log().all()
                .assertThat()
                    .statusCode(400)
                    .body("error", equalTo("Missing password"));
    }

    @Test(description = "Test POST Login Successful")
    public void testPostLoginSuccessful() {
        String requestBody = "{" +
                "\"email\": \"eve.holt@reqres.in\"," +
                "\"password\": \"cityslicka\"" +
                "}";
        String expectToken = "QpwL5tke4Pnpja7X4";

        given()
                .body(requestBody)
        .when()
                .post(LOGIN)
        .then()
                .log().all()
                .assertThat()
                    .statusCode(200)
                    .body("token", equalTo(expectToken));
    }

    @Test(description = "Test POST Login Unsuccessful")
    public void testPostLoginUnsuccessful() {
        String requestBody = "{\"email\": \"peter@klaven\"}";

        given()
                .body(requestBody)
        .when()
                .post(LOGIN)
        .then()
                .log().all()
                .assertThat()
                    .statusCode(400)
                    .body("error", equalTo("Missing password"));
    }
}
