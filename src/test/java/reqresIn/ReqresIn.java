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

    // Kurulum testi için bir test metodu
    @Test
    public void installationTest() {
        // Burada temel bir test oluşturabilirsiniz. Bu test kurulumun doğru yapıldığını kontrol eder.
        given()
                // İstek öncesi ayarlamalar yapılabilir (örneğin, başlıklar, kimlik doğrulama vb.)

                .when()
                // İstek gönderilir (örneğin, GET, POST, PUT, DELETE vb.)

                .then()
        // İstek sonucunun kontrolü yapılır (örneğin, yanıt kodu, gövde içeriği, yanıt süresi vb.)
        ;
    }

    // Tek kullanıcı testi için bir test metodu
    @Test
    public void getSingleUserTest() {
        // API'nın temel URI'si ayarlanır
        baseURI = "https://reqres.in/";

        // Yanıttan email adresini almak için bir değişken
        String eMail =

                given()
                        .pathParam("list", "users")  // İstek için path parametreleri ayarlanır
                        .pathParam("number", "2")    // İstek için path parametreleri ayarlanır

                        .when()
                        // Belirtilen endpoint'e GET isteği gönderilir
                        .log().body() // İstek gövdesini loglar
                        .get("/api/{list}/{number}")

                        .then()
                        // Yanıtın doğruluğu kontrol edilir
                        .log().body() // Yanıt gövdesini loglar
                        .statusCode(200) // Yanıt kodunun 200 (OK) olduğunu kontrol eder
                        .body("data.email", equalTo("janet.weaver@reqres.in")) // Yanıttaki email adresinin beklenen değerle eşleştiğini kontrol eder
                        .time(lessThan(1500L)) // Yanıt süresinin 1500 milisaniyeden az olduğunu kontrol eder
                        .extract().path("data.email"); // Yanıttan email adresini çıkarır

        // Çıkarılan email adresini yazdır
        System.out.println("eMail = " + eMail);
    }

    // Kullanıcılar listesi testi için bir test metodu
    @Test
    public void getUsersListTest() {
        // API'nın temel URI'si ayarlanır
        baseURI = "https://reqres.in/";

        // Yanıttan email adresleri listesini almak için bir değişken
        List<String> usersMailList =

                given()
                        .param("page", 2) // İstek için query parametresi ayarlanır
                        .log().uri() // İstek URI'sini loglar

                        .when()
                        // Belirtilen endpoint'e GET isteği gönderilir
                        .get("api/users")

                        .then()
                        // Yanıtın doğruluğu kontrol edilir
                        .log().body() // Yanıt gövdesini loglar
                        .statusCode(200) // Yanıt kodunun 200 (OK) olduğunu kontrol eder
                        .body("data[3].email", equalTo("byron.fields@reqres.in")) // Yanıttaki belirli bir email adresinin beklenen değerle eşleştiğini kontrol eder
                        .extract().path("data.email"); // Yanıttan email adresleri listesini çıkarır

        // Çıkarılan email adresleri listesini yazdır
        System.out.println("usersMailList = " + usersMailList);
    }

    // Yeni kullanıcı oluşturma testi için bir test metodu
    @Test
    public void createUserTest() {
        // Oluşturulacak kullanıcıyı temsil eden JSON string'i
        String user = "{\n" +
                " \"name\": \"morpheus\",\n" +
                " \"job\": \"leader\"\n" +
                "}";

        // API'nın temel URI'si ayarlanır
        baseURI = "https://reqres.in/";

        // API'dan gelen yanıtı tutmak için bir değişken
        Response incomingData =

                given()
                        .pathParam("list", "users") // İstek için path parametreleri ayarlanır
                        .body(user) // İstek gövdesi ayarlanır
                        .contentType(ContentType.JSON) // İçerik tipi JSON olarak ayarlanır

                        .when()
                        // Belirtilen endpoint'e POST isteği gönderilir
                        .post("/api/{list}")

                        .then()
                        // Yanıtın doğruluğu kontrol edilir
                        .log().body() // Yanıt gövdesini loglar
                        .statusCode(201) // Yanıt kodunun 201 (Created) olduğunu kontrol eder
                        .body("name", equalTo("morpheus")) // Yanıttaki ismin beklenen değerle eşleştiğini kontrol eder
                        .extract().response(); // Yanıtı çıkarır

        // Yanıttan kullanıcı adını çıkarır
        String userName = incomingData.path("name");

        // Çıkarılan kullanıcı adını yazdır
        System.out.println("userName = " + userName);
    }
}
