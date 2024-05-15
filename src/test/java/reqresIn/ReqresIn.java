package reqresIn;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import java.util.List;

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.lessThan;

public class ReqresIn {

    @Test
    public void installationTest() {

        given()

                .when()

                .then()
        ;
    }

    @Test
    public void getSingleUserTest() {

        baseURI = "https://reqres.in/";

        String eMail =

                given()
                        .pathParam("list", "users")
                        .pathParam("number", "2")

                        .when()
                        //.log().body()
                        .get("/api/{list}/{number}")

                        .then()
                        //.log().body()
                        // .log().all()
                        .statusCode(200)
                        .body("data.email", equalTo("janet.weaver@reqres.in"))
                        .time(lessThan(1500L))
                        //.time(greaterThan(1000L))
                        .extract().path("data.email");

        System.out.println("eMail = " + eMail);
    }

    @Test
    public void getUsersListTest() {

        // https://reqres.in/api/users?page=2

        baseURI = "https://reqres.in/";

        List<String> usersMailList =

                given()
                        .param("page", 2)
                        .log().uri()


                        .when()
                        .get("api/users")


                        .then()
                        .log().body()
                        .statusCode(200)
                        .body("data[3].email", equalTo("byron.fields@reqres.in"))
                        //.body("data.email",hasItem("byron.fields@reqres.in"))
                        .extract().path("data.email");

        System.out.println("usersMailList = " + usersMailList);

    }

    @Test
    public void createUserTest() {

        String user = "{\n" +
                "    \"name\": \"morpheus\",\n" +
                "    \"job\": \"leader\"\n" +
                "}";

        baseURI = "https://reqres.in/";

        Response incomingData=

        given()
                .pathParam("list","users")
                .body(user)
                .contentType(ContentType.JSON)


                .when()
                .post("/api/{list}")

                .then()
                .log().body()
                .statusCode(201)
                .body("name",equalTo("morpheus"))
                .extract().response()
        ;

        String userName=incomingData.path("name");
        System.out.println("userName = " + userName);

    }


}
