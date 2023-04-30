import org.testng.annotations.Test;

import static io.restassured.RestAssured.*;

public class ZippoTest {
    @Test
    public void test(){

        given()
                // Preparation procedures : (token, send body, parameters)


                .when()
                // Endpoint (url), method


                .then()
               // Assertion, test, data operations

        ;
    }

    @Test
    public void statusCodeTest(){
        given()

                .when()
                .get("http://api.zippopotam.us/us/90210")

                .then()
                .log().body()    // returning body json data  , log().all()
                .statusCode(200) // return code 200?
        ;
    }
}
