import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;

public class PathAndJsonPath {

    @Test
    public void extractingPath() {

        String postCode=

        given()

                .when()
                .get("http://api.zippopotam.us/us/90210")

                .then()
                .log().body()
                .extract().path("'post code'")
        ;

        System.out.println("postCode = " + postCode);
    }

    @Test
    public void extractingJsonPath() {

        // "post code" : "90210"

        int postCode=

                given()

                        .when()
                        .get("http://api.zippopotam.us/us/90210")

                        .then()
                        .log().body()
                        .extract().jsonPath().getInt("'post code'") // Type return is automatic, appropriate type should be given
                ;

        System.out.println("postCode = " + postCode);
    }
}
