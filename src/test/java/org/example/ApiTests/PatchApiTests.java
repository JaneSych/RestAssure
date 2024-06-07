package org.example.ApiTests;

import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.example.Endpoints.Endpoints.ID;
import static org.example.Endpoints.Endpoints.USERS;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class PatchApiTests extends BaseTest {

    @Test(description = "Test PATCH Update User")
    public void TestPatchUpdateUser() {
        String newJob = "Dragon Warrior";
        String requestBody = "{" +
                "\"job\": \"" + newJob + "\"" +
                "}";

        given()
                .pathParam("id", 2)
                .body(requestBody)
        .when()
                .patch(USERS+ID)
        .then()
                .log().body()
                .assertThat()
                    .statusCode(200)
                    .body("job", equalTo(newJob))
                    .body("updatedAt", notNullValue());
    }
}
