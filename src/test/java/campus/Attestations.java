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

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;

public class Attestations {

//    environment  >> https://test.mersys.io
//    endpoint     >> /school-service/api/{...}
//    content type >> JSON (JavaScript Object Notation)
//    username     >> turkeyts
//    password     >> TechnoStudy123
//    role         >> Admin

    RequestSpecification requestSpecification;
    Faker randomGenerator=new Faker();
    Map<String,String>attestation;
    String attestationName="";
    String attestationID="";

    @BeforeClass
    public void login(){

        baseURI="https://test.mersys.io";

//        {
//                "username": "turkeyts",
//                "password": "TechnoStudy123",
//                "rememberMe": "true"
//        }

        String userCredentialString="{\n" +
                "    \"username\": \"turkeyts\",\n" +
                "    \"password\": \"TechnoStudy123\",\n" +
                "    \"rememberMe\": \"true\"\n" +
                "}";

        Map<String,String> userCredentialMap=new HashMap<>();
        userCredentialMap.put("username","turkeyts");
        userCredentialMap.put("password","TechnoStudy123");
        userCredentialMap.put("rememberMe","true");

        UserCredential userCredentialClass =new UserCredential();
        userCredentialClass.setUsername("turkeyts");
        userCredentialClass.setPassword("TechnoStudy123");
        userCredentialClass.setRememberMe("true");

        Cookies cookies=

        given()

                .contentType(ContentType.JSON).body(userCredentialClass)

                .when()
                .post("/auth/login")


                .then()
                .extract().response().getDetailedCookies();

        requestSpecification=new RequestSpecBuilder()
                .setContentType(ContentType.JSON)
                .addCookies(cookies)
                .build();
    }

    @Test
    public void createAttestation() {

        attestation = new HashMap<>();

        attestationName = "Degree Certificates Attestation - " + randomGenerator.number().digits(5);
        attestation.put("name", attestationName);


        attestationID =

                given()

                        .spec(requestSpecification)
                        .body(attestation)
                        .log().body()

                        .when()
                        .post("/school-service/api/attestation")

                        .then()
                        .log().body()
                        .statusCode(201)
                        .extract().path("id")
        ;

        System.out.println("attestationID = " + attestationID);
    }

    @Test(dependsOnMethods = "createAttestation")
    public void createAttestationNegative() {

        given()

                .spec(requestSpecification)
                .body(attestation)
                .log().body()

                .when()
                .post("/school-service/api/attestation")

                .then()
                .log().body()
                .statusCode(400)
                .body("message", containsString("already"))
        ;
    }

    @Test(dependsOnMethods = "createAttestation")
    public void updateAttestation() {

        attestationName = "Post Graduation Certificates Attestation - " + randomGenerator.number().digits(5);

        attestation.put("id", attestationID);
        attestation.put("name", attestationName);

        given()

                .spec(requestSpecification)
                .body(attestation)
                // .log().body()

                .when()
                .put("/school-service/api/attestation")

                .then()
                .log().body() // show incoming body as log
                .statusCode(200)
                .body("name", equalTo(attestationName))
        ;
    }

    @Test(dependsOnMethods = "updateAttestation")
    public void deleteAttestation() {

        given()

                .spec(requestSpecification)
                .log().uri()

                .when()
                .delete("/school-service/api/attestation/" + attestationID)

                .then()
                .log().body()
                .statusCode(204)
        ;
    }

    @Test(dependsOnMethods = "deleteAttestation")
    public void deleteAttestationNegative() {

        given()

                .spec(requestSpecification)
                .pathParam("attestationID", attestationID)
                .log().uri()

                .when()
                .delete("/school-service/api/attestation/{attestationID}")

                .then()
                .log().body()
                .statusCode(400)
                .body("message", equalTo("attestation not found"))
        ;
    }
}

