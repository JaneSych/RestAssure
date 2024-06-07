package org.example.ApiTests;

import com.google.gson.Gson;
import io.restassured.response.Response;
import org.example.pojo.Root;
import org.example.pojo.UserData;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.example.Endpoints.Endpoints.DELAY;
import static org.example.Endpoints.Endpoints.PAGE;
import static org.example.Endpoints.Endpoints.RESOURCE;
import static org.example.Endpoints.Endpoints.ID;
import static org.example.Endpoints.Endpoints.USERS;
import static org.hamcrest.Matchers.emptyOrNullString;
import static org.hamcrest.Matchers.equalTo;


public class GetApiTests extends BaseTest {
    @DataProvider(name = "valuesForGetSingle")
    private Object[][] valuesForGetSingle() {
        return new Object[][]{
                {2, "Janet"},
                {3, "Emma"}
        };
    }

    @DataProvider(name = "valuesForNotFoundID")
    private Object[] valuesForNotFoundID() {
        return new Object[]{
                23,
                666
        };
    }

    @Test(description = "Test GET List Users")
    public void testGetUsers() {
        int expectedTotal = 12;
        Response response =
                given()
                        .pathParam("page", 2)
                .when()
                        .get(USERS+PAGE)
                .then()
                        .log().ifError()
                        .statusCode(200)
                        .extract().response();

        int total = response.jsonPath().getInt("total");
        Assert.assertEquals(total, expectedTotal, "Total is not " + expectedTotal +"!");
    }

    @Test(description = "Test GET Single User",
            dataProvider = "valuesForGetSingle")
    public void testGetSingleUser(int id, String expectedFirstName) {
        String response = given()
                .pathParam("id", id)
        .when()
                .get(USERS+ID)
        .then()
                .log().ifError()
                .statusCode(200)
                .extract().body().asString();

        Gson gson = new Gson();
        Root root = gson.fromJson(response, Root.class);
        UserData userData = root.getData();
        Assert.assertEquals(userData.getId(), id, "Id is not " + id + "!");
    }

    @Test(description = "Test GET Single User Not Found",
    dataProvider = "valuesForNotFoundID")
    public void testGetSingleUserNotFound(int id) {
        given()
                .pathParam("id", id)
        .when()
                .get(USERS+ID)
        .then()
                .log().body()
                .assertThat()
                    .statusCode(404)
                    .body("data", emptyOrNullString());
    }

    @Test(description = "Test GET List Resource")
    public void testGetListResource() {
        int expectedTotal = 12;
        int expectedTotalPages = 2;

        Map responseAsMap = given()
        .when()
                .get(RESOURCE)
        .then()
                .log().body()
                .statusCode(200)
                .extract().as(Map.class);
        int total = (int)responseAsMap.get("total");
        int totalPages = (int)responseAsMap.get("total_pages");
        Assert.assertEquals(total, expectedTotal, "Total is not " + expectedTotal +"!");
        Assert.assertEquals(totalPages, expectedTotalPages, "Total pages is not " + expectedTotalPages +"!");
    }

    @Test(description = "Test GET Single Resource")
    public void testGetSingleResource() {
        String expectedName = "fuchsia rose";

        given()
                .pathParam("id", 2)
        .when()
                .get(RESOURCE+ID)
        .then()
                .log().ifError()
                .assertThat()
                    .statusCode(200)
                    .body("data.name", equalTo(expectedName));
    }

    @Test(description = "Test GET Single Resource Not Found",
    dataProvider = "valuesForNotFoundID")
    public void testGetSingleResourceNotFound(int id) {
        given()
                .pathParam("id", id)
        .when()
                .get(RESOURCE+ID)
        .then()
                .log().body()
                .assertThat()
                    .statusCode(404)
                    .body("data", emptyOrNullString());
    }

    @Test(description = "Test GET Delayed Response")
    public void testGetDelayedResponse() {
        int expectedPage = 1;
        given()
                .pathParam("delay", 3)
        .when()
                .get(USERS+DELAY)
        .then()
                .log().ifError()
                .assertThat()
                    .statusCode(200)
                    .body("page", equalTo(expectedPage));
    }
}
