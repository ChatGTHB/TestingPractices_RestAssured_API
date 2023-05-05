package goRest;

import com.github.javafaker.Faker;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;


public class GoRestUsersTests {

    Faker randomGenerator = new Faker();
    int userID;

    RequestSpecification requestSpecification;

    @BeforeClass
    public void setup() {

        baseURI = "https://gorest.co.in/public/v2/users";  // baseURI must be defined before RequestSpecification.
        // baseURI ="https://test.gorest.co.in/public/v2/users/";

        requestSpecification = new RequestSpecBuilder()
                .addHeader("Authorization", "Bearer 4b97b6d4f186b3272628a611715896f3ab8577ae2ea6ccb07b24ca2ae90d60fc")
                .setContentType(ContentType.JSON)
                .build();
    }

    @Test(enabled = false)
    public void createUserJson() {

        /**
         POST https://gorest.co.in/public/v2/users
         "Authorization: Bearer 4b97b6d4f186b3272628a611715896f3ab8577ae2ea6ccb07b24ca2ae90d60fc"
         {"name":"{{$randomFullName}}", "gender":"male", "email":"{{$randomEmail}}", "status":"active"}
         */

        String randomFullName = randomGenerator.name().fullName();
        String randomEmail = randomGenerator.internet().emailAddress();

        userID =

                given()
                        .header("Authorization", "Bearer 4b97b6d4f186b3272628a611715896f3ab8577ae2ea6ccb07b24ca2ae90d60fc")
                        .contentType(ContentType.JSON) // Data to be sent is JSON
                        .body("{\"name\":\"" + randomFullName + "\", \"gender\":\"male\", \"email\":\"" + randomEmail + "\", \"status\":\"active\"}")
//                        .log().uri()
//                        .log().body()

                        .when()
                        .post("")

                        .then()
                        .log().body()
                        .statusCode(201)
                        .contentType(ContentType.JSON)
                        .extract().path("id");
    }

    @Test(enabled = false)
    public void createUserMap() {

        String randomFullName = randomGenerator.name().fullName();
        String randomEmail = randomGenerator.internet().emailAddress();

        Map<String, String> newUser = new HashMap<>();
        newUser.put("name", randomFullName);
        newUser.put("gender", "male");
        newUser.put("email", randomEmail);
        newUser.put("status", "active");

        userID =

                given()
                        .header("Authorization", "Bearer 4b97b6d4f186b3272628a611715896f3ab8577ae2ea6ccb07b24ca2ae90d60fc")
                        .contentType(ContentType.JSON) // Data to be sent is JSON
                        .body(newUser)
//                        .log().uri()
//                        .log().body()

                        .when()
                        .post("")

                        .then()
                        .log().body()
                        .statusCode(201)
                        .contentType(ContentType.JSON)
                        .extract().path("id");
    }

    @Test
    public void createUserClass() {

        String randomFullName = randomGenerator.name().fullName();
        String randomEmail = randomGenerator.internet().emailAddress();

        User newUser = new User();
        newUser.name = randomFullName;
        newUser.gender = "male";
        newUser.email = randomEmail;
        newUser.status = "active";

        userID =

                given()
                        .header("Authorization", "Bearer 4b97b6d4f186b3272628a611715896f3ab8577ae2ea6ccb07b24ca2ae90d60fc")
                        .contentType(ContentType.JSON) // Data to be sent is JSON
                        .body(newUser)
                        .log().uri()
//                        .log().body()

                        .when()
                        .post("")

                        .then()
                        .log().body()
                        .statusCode(201)
                        .contentType(ContentType.JSON)
                        .extract().path("id");
    }


    @Test(dependsOnMethods = "createUserClass")
    public void getUserByID() {

        given()

                .spec(requestSpecification)

                .when()
                .get("" + userID)

                .then()
                .log().body()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("id", equalTo(userID))
        ;

    }

    @Test(dependsOnMethods = "createUserClass")
    public void updateUser() {

        Map<String, String> updateUser = new HashMap<>();
        updateUser.put("name", "Kerem Yigit");

        given()

                .spec(requestSpecification)
                .body(updateUser)
                .log().uri()

                .when()
                .put("" + userID)

                .then()
                 .log().body()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("id", equalTo(userID))
                .body("name", equalTo("Kerem Yigit"))
        ;
    }

    @Test(dependsOnMethods = "updateUser")
    public void deleteUser() {

        given()

                .spec(requestSpecification)
                .log().uri()

                .when()
                .delete("" + userID)

                .then()
//                .log().all()
                .statusCode(204)
        ;
    }

    @Test(dependsOnMethods = "deleteUser")
    public void deleteUserNegative() {

        given()

                .spec(requestSpecification)
                .log().uri()

                .when()
                .delete("" + userID)

                .then()
//                .log().all()
                .statusCode(404)
        ;
    }
}
