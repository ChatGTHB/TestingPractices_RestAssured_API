package campus;

import io.restassured.http.ContentType;
import io.restassured.http.Cookies;
import org.testng.annotations.BeforeClass;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.*;

public class Attestations {

//    environment  >> https://test.mersys.io
//    endpoint     >> /school-service/api/{...}
//    content type >> JSON (JavaScript Object Notation)
//    username     >> turkeyts
//    password     >> TechnoStudy123
//    role         >> Admin

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
                .statusCode(200).extract().response().getDetailedCookies();
    }



}
