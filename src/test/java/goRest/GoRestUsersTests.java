package goRest;

import com.github.javafaker.Faker;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.*;


public class GoRestUsersTests {

    Faker randomGenerator = new Faker();
    int userID;

    RequestSpecification requestSpecification;

    @BeforeClass
    public void setup() {

        baseURI = "https://gorest.co.in/public/v2/users";  // baseURI must be defined before RequestSpecification.
        // baseURI ="https://test.gorest.co.in/public/v2/users/";

        requestSpecification = new RequestSpecBuilder()
                .addHeader("Authorization", "Bearer e4b22047188da067d3bd95431d94259f63896347f9864894a0a7013ee5f9c703")
                .setContentType(ContentType.JSON)
                .build();
    }

    @Test
    public void createUser() {

        /**
         POST https://gorest.co.in/public/v2/users
         "Authorization: Bearer 4b97b6d4f186b3272628a611715896f3ab8577ae2ea6ccb07b24ca2ae90d60fc"
         {"name":"{{$randomFullName}}", "gender":"male", "email":"{{$randomEmail}}", "status":"active"}
         */

        String randomFullName = randomGenerator.name().fullName();
        String randomEmail = randomGenerator.internet().emailAddress();

        int userID =

                given()
                        .header("Authorization", "Bearer 4b97b6d4f186b3272628a611715896f3ab8577ae2ea6ccb07b24ca2ae90d60fc")
                        .contentType(ContentType.JSON) // Data to be sent is JSON
                        .body("{\"name\":\"" + randomFullName + "\", \"gender\":\"male\", \"email\":\"" + randomEmail + "\", \"status\":\"active\"}")
                        .log().uri()
                        .log().body()

                        .when()
                        .post("https://gorest.co.in/public/v2/users")

                        .then()
                        .log().body()
                        .statusCode(201)
                        .contentType(ContentType.JSON)
                        .extract().path("id")
                ;
    }

    @Test
    public void getUserByID() {

    }

    @Test
    public void updateUser() {
    }

    @Test
    public void deleteUser() {
    }

    @Test
    public void deleteUserNegative() {
    }
}
