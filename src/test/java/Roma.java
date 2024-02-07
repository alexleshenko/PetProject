import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static io.restassured.RestAssured.given;

public class Roma {
    @BeforeClass
    public static void setup() {
        RestAssured.baseURI = "https://petstore.swagger.io/v2";
    }

    @Test
    public void createPet() throws IOException {

        String requestBody = new String(Files.readAllBytes(Paths.get("src/main/resources/Roma.json")));

        SoftAssert softAssert = new SoftAssert();

        Response response = given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post("/pet")
                .then()
                .log().all()
                .extract().response();

        softAssert.assertEquals(response.statusCode(), 200, "Status code should be 200");

        Long id = response.path("id");
        softAssert.assertNotNull(id, "Pet ID should not be null");
        softAssert.assertNotEquals(id.longValue(), 0L, "Pet ID should not be 0");

        softAssert.assertEquals(response.path("name"), "Roma", "Pet name should be Roma");
        softAssert.assertEquals(response.path("status"), "unavailable", "Pet status should be available");

        System.out.println("Created pet ID: " + id);

        softAssert.assertAll();
    }
}
