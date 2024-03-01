import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.testng.annotations.Test;

import static org.hamcrest.Matchers.equalTo;

public class GETIvyTest {

    @Test
    public void getIvy() {
        long ivyId = 9223372036854775807L;

        RestAssured.given()
                .contentType(ContentType.JSON)
                .when()
                .get("https://petstore.swagger.io/v2/pet/" + ivyId)
                .then()
                .statusCode(200)
                .body("id",equalTo(ivyId));
    }

}
