package campus;

import com.github.javafaker.Faker;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.http.Cookies;
import io.restassured.specification.RequestSpecification;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;

public class countryTest {

    Faker faker = new Faker();
    String countryID;
    String countryName;
    Map<String, String> country;

    RequestSpecification requestSpecification;

    @BeforeClass
    public void login() {

        baseURI = "https://test.mersys.io";

        Map<String, String> userCredential = new HashMap<>();
        userCredential.put("username", "turkeyts");
        userCredential.put("password", "TechnoStudy123");
        userCredential.put("rememberMe", "true");

        Cookies cookies =

                given()

                        .contentType(ContentType.JSON)
                        .body(userCredential)

                        .when()
                        .post("/auth/login")

                        .then()
//                       .log().all()
                        .statusCode(200)
                        .extract().response().getDetailedCookies();

        requestSpecification = new RequestSpecBuilder()
                .setContentType(ContentType.JSON)
                .addCookies(cookies)
                .build();
    }

    @Test
    public void createCountry() {

        country = new HashMap<>();

        countryName = faker.address().country() + faker.number().digits(5);
        country.put("name", countryName);
        country.put("code", faker.address().countryCode() + faker.number().digits(5));

        countryID =

                given()

                        .spec(requestSpecification)
                        .body(country)
                        .log().body()

                        .when()
                        .post("/school-service/api/countries")

                        .then()
                        .log().body()
                        .statusCode(201)
                        .extract().path("id")
        ;

        System.out.println("countryID = " + countryID);
    }

    @Test(dependsOnMethods = "createCountry")
    public void createCountryNegative() {

        given()

                .spec(requestSpecification)
                .body(country)
                .log().body()

                .when()
                .post("/school-service/api/countries")

                .then()
                .log().body()
                .statusCode(400)
                .body("message", containsString("already"))
        ;
    }

    @Test(dependsOnMethods = "createCountry")
    public void updateCountry() {

        countryName = faker.address().country() + faker.number().digits(7);
        country.put("id", countryID);
        country.put("name", countryName);
        country.put("code", faker.address().countryCode() + faker.number().digits(5));

        given()
                .spec(requestSpecification)
                .body(country) // outgoing body
                //.log().body() // show outgoing body as log

                .when()
                .put("/school-service/api/countries")

                .then()
                .log().body() // show incoming body as log
                .statusCode(200)
                .body("name", equalTo(countryName))
        ;
    }

    @Test(dependsOnMethods = "updateCountry")
    public void deleteCountry() {

        given()
                .spec(requestSpecification)
                //.log().body() // show outgoing body as log

                .when()
                .delete("/school-service/api/countries/"+countryID)

                .then()
                .log().body() // show incoming body as log
                .statusCode(200)
        ;
    }

    @Test(dependsOnMethods = "deleteCountry")
    public void deleteCountryNegative() {

        given()
                .spec(requestSpecification)
                //.log().body() // show outgoing body as log

                .when()
                .delete("/school-service/api/countries/"+countryID)

                .then()
                .log().body() // show incoming body as log
                .statusCode(400)
        ;
    }
}
