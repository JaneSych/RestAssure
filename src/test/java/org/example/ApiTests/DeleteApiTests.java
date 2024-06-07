package org.example.ApiTests;

import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.example.Endpoints.Endpoints.USERS;
import static org.example.Endpoints.Endpoints.ID;
import static org.hamcrest.Matchers.emptyOrNullString;

public class DeleteApiTests extends BaseTest {

    @Test(description = "Test DELETE User")
    public void testDeleteUser() {
        given()
                .pathParam("id", 2)
        .when()
                .delete(USERS+ID)
        .then()
                .log().ifError()
                .assertThat()
                    .statusCode(204)
                    .body(emptyOrNullString());
    }

}
