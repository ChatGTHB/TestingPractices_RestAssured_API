package model;

import io.restassured.http.ContentType;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class ZippoTest {
    @Test
    public void test() {

        given()
                // Preparation procedures : (token, send body, parameters)


                .when()
                // Endpoint (url), method


                .then()
                // Assertion, test, data operations

        ;
    }

    @Test
    public void statusCodeTest() {

        given()


                .when()
                .get("http://api.zippopotam.us/us/90210")


                .then()
                .log().body()    // Returning body json data, log().all()
                .statusCode(200) // Is return code 200 ?
        ;
    }

    @Test
    public void contentTypeTest() {

        given()


                .when()
                .get("http://api.zippopotam.us/us/90210")


                .then()
                .log().body()    // Returning body json data, log().all()
                .statusCode(200) // Is return code 200?
                .contentType(ContentType.JSON) // Is the returned result JSON?
        ;
    }

    @Test
    public void checkCountryInResponseBody() {

        given()


                .when()
                .get("http://api.zippopotam.us/us/90210")


                .then()
                .log().body()    // Returning body json data, log().all()
                .statusCode(200) // Is return code 200?
                .body("country",equalTo("United States")) // Is body's country variable equal to "United States"?
        ;
    }


}
