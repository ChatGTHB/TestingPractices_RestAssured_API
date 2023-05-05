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

public class countryTest {

    Faker faker = new Faker();
    String countryID;
    String countryName;

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

        Map<String, String> country = new HashMap<>();

        countryName=faker.address().country() + faker.number().digits(5);
        country.put("name",countryName);
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

    }

    @Test(dependsOnMethods = "createCountry")
    public void updateCountry() {

    }

    @Test(dependsOnMethods = "updateCountry")
    public void deleteCountry() {

    }

    @Test(dependsOnMethods = "deleteCountry")
    public void deleteCountryNegative() {

    }
}
