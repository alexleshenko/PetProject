import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import java.util.Arrays;


import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class Pet {
    private static long petId;

    @BeforeClass
    public void setUp() {
        RestAssured.baseURI = "https://petstore.swagger.io/v2";
    }

    @Test(priority = 1)
    public void createPetTest() {
        String newPetJson =
                "{\n" +
                        "  \"id\": 0,\n" +
                        "  \"category\": {\n" +
                        "    \"id\": 0,\n" +
                        "    \"name\": \"Dog\"\n" +
                        "  },\n" +
                        "  \"name\": \"Ivy\",\n" +
                        "  \"photoUrls\": [\n" +
                        "    \"https://images.rawpixel.com/image_1100/cHJpdmF0ZS9zdGF0aWMvaW1hZ2Uvd2Vic2l0ZS8yMDIyLTA0L2xyL3B4MTQxODc1MC1pbWFnZS1rd3Z3MTBwZy5qcGc.jpg\"\n" +
                        "  ],\n" +
                        "  \"tags\": [\n" +
                        "    {\n" +
                        "      \"id\": 0,\n" +
                        "      \"name\": \"Corgi\"\n" +
                        "    },\n" +
                        "    {\n" +
                        "      \"id\": 1,\n" +
                        "      \"name\": \"Welsh\"\n" +
                        "    }\n" +
                        "  ],\n" +
                        "  \"status\": \"sold\"\n" +
                        "}";

        petId = given()
                .contentType(ContentType.JSON)
                .body(newPetJson)
                .when()
                .post("/pet")
                .then()
                .statusCode(200)
                .extract()
                .jsonPath()
                .getLong("id");

    }
    @Test(priority = 2)
    public void getPetById() {
        SoftAssert softAssert = new SoftAssert();

        String responseBody = given()
                .contentType(ContentType.JSON)
                .pathParam("petId", petId)
                .when()
                .get("/pet/{petId}")
                .then()
                .statusCode(200)
                .extract()
                .response()
                .asString();

        io.restassured.path.json.JsonPath jsonPath = new io.restassured.path.json.JsonPath(responseBody);
        softAssert.assertEquals(jsonPath.getLong("id"), petId, "Pet ID does not match");
        softAssert.assertEquals(jsonPath.getString("name"), "Ivy", "Pet name does not match");
        softAssert.assertEquals(jsonPath.getString("category.name"), "Dog", "Category name does not match");
        softAssert.assertTrue(jsonPath.getList("tags.name", String.class).contains("Corgi"), "Tag 'Corgi' is missing");
        softAssert.assertTrue(jsonPath.getList("tags.name", String.class).contains("Welsh"), "Tag 'Welsh' is missing");
        softAssert.assertEquals(jsonPath.getString("status"), "sold", "Pet status is not 'sold'");


        softAssert.assertAll();
    }

    @Test(priority = 3)
    public void updatePetTest() {
        SoftAssert softAssert = new SoftAssert();

        String updatedPetJson =
                "{\n" +
                        "  \"id\": " + petId + ",\n" +
                        "  \"category\": {\n" +
                        "    \"id\": 1,\n" +
                        "    \"name\": \"Dog1\"\n" +
                        "  },\n" +
                        "  \"name\": \"Ivy1\",\n" +
                        "  \"photoUrls\": [\n" +
                        "    \"https://images.rawpixel.com/image_1100/cHJpdmF0ZS9zdGF0aWMvaW1hZ2Uvd2Vic2l0ZS8yMDIyLTA0L2xyL3B4MTQxODc1MC1pbWFnZS1rd3Z3MTBwZy5qcGc1.jpg\"\n" +
                        "  ],\n" +
                        "  \"tags\": [\n" +
                        "    {\n" +
                        "      \"id\": 2,\n" +
                        "      \"name\": \"Corgi1\"\n" +
                        "    },\n" +
                        "    {\n" +
                        "      \"id\": 3,\n" +
                        "      \"name\": \"Welsh1\"\n" +
                        "    }\n" +
                        "  ],\n" +
                        "  \"status\": \"available1\"\n" +
                        "}";

        String responseBody = given()
                .contentType(ContentType.JSON)
                .body(updatedPetJson)
                .when()
                .put("/pet")
                .then()
                .statusCode(200)
                .extract()
                .asString();

        io.restassured.path.json.JsonPath jsonPath = new io.restassured.path.json.JsonPath(responseBody);
        softAssert.assertEquals(jsonPath.getLong("id"), petId, "Pet ID does not match the expected value.");
        softAssert.assertEquals(jsonPath.getString("name"), "Ivy1", "Pet name has not been updated correctly.");
        softAssert.assertEquals(jsonPath.getString("category.name"), "Dog1", "Category name has not been updated correctly.");
        softAssert.assertEquals(jsonPath.getString("status"), "available1", "Pet status has not been updated correctly.");
        softAssert.assertTrue(jsonPath.getList("photoUrls", String.class).contains("https://images.rawpixel.com/image_1100/cHJpdmF0ZS9zdGF0aWMvaW1hZ2Uvd2Vic2l0ZS8yMDIyLTA0L2xyL3B4MTQxODc1MC1pbWFnZS1rd3Z3MTBwZy5qcGc1.jpg"), "Photo URL has not been updated correctly.");
        softAssert.assertTrue(jsonPath.getList("tags.name", String.class).containsAll(Arrays.asList("Corgi1", "Welsh1")), "Tags have not been updated correctly.");

        softAssert.assertAll();
    }

    @Test(priority = 4)
    public void deletePetTest() {
        given()
                .contentType(ContentType.JSON)
                .pathParam("petId", petId)
                .when()
                .delete("/pet/{petId}")
                .then()
                .statusCode(200);

        given()
                .contentType(ContentType.JSON)
                .pathParam("petId", petId)
                .when()
                .get("/pet/{petId}")
                .then()
                .statusCode(404);
    }


}