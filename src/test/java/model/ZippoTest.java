package model;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.List;

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;
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
                .body("country", equalTo("United States")) // Is body's country variable equal to "United States"?
        ;
    }

//    PM                            RestAssured

//    body.country                  body("country")
//    body.'post code'              body("post code")
//    body.places[0].'place name'   body("places[0].'place name'")
//    body.places.'place name'      body("places.'place name'")
//    Returns all place names as an arraylist.
//    https://jsonpathfinder.com/

    @Test
    public void checkStateInResponseBody() {

        given()


                .when()
                .get("http://api.zippopotam.us/us/90210")


                .then()
                .log().body()    // Returning body json data, log().all()
                .statusCode(200) // Is return code 200?
                .body("places[0].state", equalTo("California")) // Is body's country variable equal to "United States"?
        ;
    }

    @Test
    public void checkHasItem() {

        given()


                .when()
                .get("http://api.zippopotam.us/tr/01000")


                .then()
                .log().body()    // Returning body json data, log().all()
                .statusCode(200) // Is return code 200?
                .body("places.'place name'", hasItem("Dörtağaç Köyü")) // Is there a "Dörtağaç Köyü" in the place names?
        ;
    }

    @Test
    public void bodyArrayHasSizeTest() {

        given()


                .when()
                .get("http://api.zippopotam.us/us/90210")


                .then()
                .log().body()    // Returning body json data, log().all()
                .statusCode(200) // Is return code 200?
                .body("places", hasSize(1))
        ;
    }

    @Test
    public void combinigTest() {

        given()


                .when()
                .get("http://api.zippopotam.us/us/90210")


                .then()
                .log().body()    // Returning body json data, log().all()
                .statusCode(200) // Is return code 200?
                .body("places", hasSize(1)) // Is size "1" ?
                .body("places.state", hasItem("California")) // Does the list in the given path have this item?
                .body("places[0].'place name'", equalTo("Beverly Hills")) // Is the value in the given path equal to this?
        ;
    }

    @Test
    public void pathParamTest() {

        given()
                .pathParam("country", "us")
                .pathParam("zipCode", 90210)
                .log().uri()


                .when()
                .get("http://api.zippopotam.us/{country}/{zipCode}")


                .then()
                //.log().body()    // Returning body json data, log().all()
                .statusCode(200) // Is return code 200?
        ;
    }

    @Test
    public void queryParamTest() {

        // https://gorest.co.in/public/v1/users?page=3

        given()
                .param("page", 1) // Adding ?page=1 to the link
                .log().uri() // Request link


                .when()
                .get("http://gorest.co.in/public/v1/users") // ?page=1


                .then()
                .log().body()    // Returning body json data, log().all()
                .statusCode(200) // Is return code 200?
        ;
    }

    @Test
    public void queryParamTest2() {

        // https:gorest.co.inpublicv1users?page=3
        // When you call pages from 1 to 10 on this link, check whether the returned page values
        // in the response are the same as the page number called.

        for (int i = 1; i < 10; i++) {
            given()
                    .param("page", i) // Adding ?page=1 to the link
                    .log().uri() // Request link


                    .when()
                    .get("http://gorest.co.in/public/v1/users") // ?page=1


                    .then()
                    // .log().body()    // Returning body json data, log().all()
                    .statusCode(200) // Is return code 200?
                    .body("meta.pagination.page", equalTo(i))
            ;
        }
    }

    RequestSpecification requestSpecification;
    ResponseSpecification responseSpecification;

    @BeforeClass
    public void setup() {

        baseURI = "https://gorest.co.in/public/v1";

        requestSpecification = new RequestSpecBuilder()
                .log(LogDetail.URI)
                .setContentType(ContentType.JSON)
                .build();

        responseSpecification = new ResponseSpecBuilder()
                .expectContentType(ContentType.JSON)
                .log(LogDetail.BODY)
                .build();

    }

    @Test
    public void requestResponseSpecification() {

        // https://gorest.co.in/public/v1/users?page=3

        given()
                .param("page", 1) // Adding ?page=1 to the link
                .spec(requestSpecification)


                .when()
                .get("/users") // ?page=1


                .then()
                .spec(responseSpecification)
        ;
    }

    @Test
    public void extractingJsonPath() {

        String countryName = given().when().get("http://api.zippopotam.us/us/90210").then().log().body().extract().path("country");

        System.out.println("countryName = " + countryName);
        Assert.assertEquals(countryName, "United States");
    }

    @Test
    public void extractingJsonPath2() {

        String placeName =
                given()

                        .when()
                        .get("http://api.zippopotam.us/us/90210")

                        .then()
                        .log().body()
                        .extract().path("places[0].'place name'"); // places[0]['place name']

        System.out.println("placeName = " + placeName);
        Assert.assertEquals(placeName, "Beverly Hills");
    }

    @Test
    public void extractingJsonPath3() {

        // https://gorest.co.in/public/v1/users --> print the limit information in the returned value.

        int limit =
                given()
                        .spec(requestSpecification)

                        .when()
                        .get("/users")

                        .then()
                        //.log().body()
                        .statusCode(200)
                        .extract().path("meta.pagination.limit");

        System.out.println("limit = " + limit);

    }

    @Test
    public void extractingJsonPath4() {

        // https://gorest.co.in/public/v1/users --> print all ID's in the return value

        List<Integer> IDs =
                given()
                        .spec(requestSpecification)

                        .when()
                        .get("/users")

                        .then()
                        //.log().body()
                        .statusCode(200)
                        .extract().path("data.id");

        System.out.println("IDs = " + IDs);
    }

    @Test
    public void extractingJsonPath5() {

        // https://gorest.co.in/public/v1/users --> print all ID's in the return value

        List<String> names =
                given()
                        .spec(requestSpecification)

                        .when()
                        .get("/users")

                        .then()
                        //.log().body()
                        .statusCode(200)
                        .extract().path("data.name");

        System.out.println("Names = " + names);
    }

    @Test
    public void extractingJsonPathResponseAll() {

        // https://gorest.co.in/public/v1/users --> print all ID's in the return value

        Response returningData =
                given()
                        .spec(requestSpecification)

                        .when()
                        .get("/users")

                        .then()
                        //.log().body()
                        .statusCode(200)
                        .extract().response(); // Returns all data returned

        List<Integer>IDs=returningData.path("data.id");
        List<String>names=returningData.path("data.name");
        int limit=returningData.path("meta.pagination.limit");

        System.out.println("IDs = " + IDs);
        System.out.println("names = " + names);
        System.out.println("limit = " + limit);

        Assert.assertTrue(names.contains("Deevakar Nehru Jr."));
        Assert.assertTrue(IDs.contains(1374004));
        Assert.assertEquals(limit,10,"Test result is incorrect");


    }
}

