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

    // Installation testi için bir Test metodu oluşturulur
    @Test
    public void installationTest() {
        given()
                // İstek öncesi ayarlamalar yapılır
                // Örneğin, header ayarlamaları, authentication vb.

                .when()
                // İstek gönderilir
                // Örneğin, GET, POST, PUT, DELETE vb. gibi istekler yapılabilir

                .then()
        // İstek sonucu kontrol edilir
        // Örneğin, yanıt kodu (status code) kontrolü, body kontrolü, zaman kontrolü vb.
        ;
    }

    // Single User Testi için bir Test metodu oluşturulur
    @Test
    public void getSingleUserTest() {
        baseURI = "https://reqres.in/";
        String eMail =

        given()
                .pathParam("list", "users")
                .pathParam("number", "2")

                .when()
                // API'dan kullanıcı bilgisi alınır
                // Örneğin, GET isteği yapılır ve belirli bir kullanıcının bilgileri alınır
                .log().body() // İstek gövdesini loglar
                .get("/api/{list}/{number}")


                .then()
                // İstek sonucu kontrol edilir
                // Örneğin, yanıt kodu (status code) kontrolü, body kontrolü, zaman kontrolü vb.
                .log().body() // Yanıt gövdesini loglar
                // .log().all() // Tüm yanıt bilgilerini loglar
                .statusCode(200) // Yanıt kodunun 200 (OK) olmasını kontrol eder
                .body("data.email", equalTo("janet.weaver@reqres.in")) // Yanıt gövdesindeki "data.email" alanının "janet.weaver@reqres.in" olmasını kontrol eder
                .time(lessThan(1500L)) // Yanıt süresinin 1500 milisaniyeden az olmasını kontrol eder
                //.time(greaterThan(1000L)) // Yanıt süresinin 1000 milisaniyeden fazla olmasını kontrol eder
                .extract().path("data.email"); // Yanıt gövdesindeki "data.email" alanını çıkarır

        System.out.println("eMail = " + eMail); // eMail değişkenini yazdırır
    }

    // Users List Testi için bir Test metodu oluşturulur
    @Test
    public void getUsersListTest() {

        // https://reqres.in/api/users?page=2

        baseURI = "https://reqres.in/";

        List<String> usersMailList =

        given()
                .param("page", 2)
                .log().uri() // İstek URI'sini loglar

                .when()
                // API'dan kullanıcı listesi alınır
                // Örneğin, GET isteği yapılır ve belirli bir sayfadaki kullanıcı listesi alınır
                .get("api/users")

                .then()
                // İstek sonucu kontrol edilir
                // Örneğin, yanıt kodu (status code) kontrolü, body kontrolü, zaman kontrolü vb.
                .log().body() // Yanıt gövdesini loglar
                .statusCode(200) // Yanıt kodunun 200 (OK) olmasını kontrol eder
                .body("data[3].email", equalTo("byron.fields@reqres.in")) // Yanıt gövdesindeki "data[3].email" alanının "byron.fields@reqres.in" olmasını kontrol eder
                //.body("data.email",hasItem("byron.fields@reqres.in"))
                .extract().path("data.email"); // Yanıt gövdesindeki "data.email" alanını çıkarır

        System.out.println("usersMailList = " + usersMailList); // usersMailList değişkenini yazdırır
    }

    // Create User Testi için bir Test metodu oluşturulur
    @Test
    public void createUserTest() {

        String user = "{\n" +
                " \"name\": \"morpheus\",\n" +
                " \"job\": \"leader\"\n" +
                "}";

        baseURI = "https://reqres.in/";

        Response incomingData=

        given()
                .pathParam("list","users")
                .body(user)
                .contentType(ContentType.JSON)

                .when()
                // Yeni bir kullanıcı oluşturulur
                // Örneğin, POST isteği yapılır ve yeni bir kullanıcı oluşturulur
                .post("/api/{list}")

                .then()
                // İstek sonucu kontrol edilir
                // Örneğin, yanıt kodu (status code) kontrolü, body kontrolü, zaman kontrolü vb.
                .log().body() // Yanıt gövdesini loglar
                .statusCode(201) // Yanıt kodunun 201 (Created) olmasını kontrol eder
                .body("name",equalTo("morpheus")) // Yanıt gövdesindeki "name" alanının "morpheus" olmasını kontrol eder
                .extract().response() ; // Yanıtı çıkarır

        String userName=incomingData.path("name"); // Yanıt gövdesindeki "name" alanını alır

        System.out.println("userName = " + userName); // userName değişkenini yazdırır
    }
}