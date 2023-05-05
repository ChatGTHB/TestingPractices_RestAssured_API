package campus;

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

    RequestSpecification requestSpecification;

    @BeforeClass
    public void login() {

        baseURI = "https://test.mersys.io";

        Map<String, String> userCredential = new HashMap<>();
        userCredential.put("username", "turkeyts");
        userCredential.put("password", "TechnoStudy123");
        userCredential.put("rememberMe", "true");

        Cookies cookies=
        given()

                .contentType(ContentType.JSON)
                .body(userCredential)

                .when()
                .post("/auth/login")

                .then()
//                .log().all()
                .statusCode(200)
                .extract().response().getDetailedCookies()
        ;

        requestSpecification = new RequestSpecBuilder()
                .setContentType(ContentType.JSON)
                .addCookies(cookies)
                .build();
    }

    @Test
    public void createCountry() {

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
