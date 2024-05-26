package campus;

import com.github.javafaker.Faker; // Sahte veri oluşturmak için gerekli kütüphane

import io.restassured.builder.RequestSpecBuilder; // REST isteklerini yapılandırmak için gerekli sınıflar
import io.restassured.http.ContentType; // İstek ve yanıtların içeriğini belirlemek için kullanılan enum
import io.restassured.http.Cookies; // Çerezleri temsil etmek için kullanılan sınıf
import io.restassured.specification.RequestSpecification; // REST isteği yaparken kullanılan yapıyı temsil eden sınıf

import org.testng.annotations.BeforeClass; // TestNG çerçevesi için gerekli annotasyonlar
import org.testng.annotations.Test; // TestNG çerçevesi için gerekli annotasyonlar

import java.util.HashMap; // Anahtar-değer çiftlerini saklamak için kullanılan sınıf
import java.util.Map; // Anahtar-değer çiftlerini saklamak için kullanılan sınıf

import static io.restassured.RestAssured.*; // RestAssured kütüphanesinden bazı static metodlar
import static org.hamcrest.Matchers.containsString; // Hamcrest kütüphanesinden bazı eşleştirmeleri sağlayan sınıf
import static org.hamcrest.Matchers.equalTo; // Hamcrest kütüphanesinden bazı eşleştirmeleri sağlayan sınıf


public class Attestations {

    // Ortam bilgisi
    // Test ortamı için https://test.mersys.io kullanılacak
    // endpoint "/school-service/api/{...}" şeklinde
    // İçerik türü JSON (JavaScript Object Notation)
    // Kullanıcı adı turkeyts
    // Şifre TechnoStudy123
    // Rol Admin

    // İstek yapılandırması
    RequestSpecification requestSpecification; // RestAssured için istek yapılandırması
    Faker randomGenerator = new Faker(); // Sahte veri oluşturmak için kullanılan nesne
    Map<String, String> attestation; // Belgeler için anahtar-değer çiftlerini saklayan map
    String attestationName = ""; // Belgeler için ad
    String attestationID = ""; // Belgeler için ID

    // Giriş işlemi
    @BeforeClass
    public void login(){

        // Temel URI
        baseURI = "https://test.mersys.io";

//        {
//                "username": "turkeyts",
//                "password": "TechnoStudy123",
//                "rememberMe": "true"
//        }

        // Kullanıcı kimlik bilgileri
        String userCredentialString="{\n" +
                "    \"username\": \"turkeyts\",\n" +
                "    \"password\": \"TechnoStudy123\",\n" +
                "    \"rememberMe\": \"true\"\n" +
                "}";


        // Kullanıcı kimlik bilgileri için map
        Map<String, String> userCredentialMap = new HashMap<>();
        userCredentialMap.put("username", "turkeyts");
        userCredentialMap.put("password", "TechnoStudy123");
        userCredentialMap.put("rememberMe", "true");

        // Kullanıcı kimlik bilgileri sınıfı
        UserCredential userCredentialClass = new UserCredential();
        userCredentialClass.setUsername("turkeyts");
        userCredentialClass.setPassword("TechnoStudy123");
        userCredentialClass.setRememberMe("true");

        // Çerezler
        Cookies cookies =
                given() // Belirli bir istek yapma
                        .contentType(ContentType.JSON).body(userCredentialClass) // İstek gövdesi ve içerik türü belirleme

                        .when() // İstek yapma aşaması
                        .post("/auth/login") // Belirtilen yola bir POST isteği gönderme

                        .then() // Yanıtın değerlendirilmesi aşaması
                        .extract().response().getDetailedCookies(); // Giriş işleminden elde edilen çerezleri alıp saklama


        // İstek yapılandırması oluşturuluyor
        requestSpecification = new RequestSpecBuilder() // RequestSpecBuilder kullanarak isteği yapılandırma
                .setContentType(ContentType.JSON) // İstek tipini JSON olarak belirleme
                .addCookies(cookies) // Elde edilen çerezleri isteğe ekleme
                .build(); // Yapılandırmayı tamamlama
    }

    @Test
    public void createAttestation() {

        attestation = new HashMap<>(); // Yeni bir HashMap oluşturuluyor

        attestationName = "Degree Certificates Attestation - " + randomGenerator.number().digits(5); // Rastgele bir ad oluşturuluyor
        attestation.put("name", attestationName); // Oluşturulan ad, "name" anahtarıyla HashMap'e ekleniyor

        attestationID = // Belgelendirme kimliği

                given()

                        .spec(requestSpecification) // Önceden belirlenmiş istek yapılandırmasını kullanarak istek yapma
                        .body(attestation) // İstek gövdesini HashMap olarak belirleme
                        .log().body() // İstek gövdesini günlüğe yazma

                        .when()
                        .post("/school-service/api/attestation") // Belirtilen yola bir POST isteği gönderme

                        .then() // Yanıtın değerlendirilmesi aşaması
                        .log().body() // Yanıt gövdesini günlüğe yazma
                        .statusCode(201) // Yanıt durum kodunun 201 (Başarıyla Oluşturuldu) olmasını kontrol etme
                        .extract().path("id"); // Yanıttan "id" alanını çıkarma

        System.out.println("attestationID = " + attestationID); // Belgelendirme kimliğini konsola yazdırma
    }

    @Test(dependsOnMethods = "createAttestation")
    public void createAttestationNegative() {

        given()

                .spec(requestSpecification) // Önceden belirlenmiş istek yapılandırmasını kullanarak istek yapma
                .body(attestation) // İstek gövdesini HashMap olarak belirleme
                .log().body() // İstek gövdesini günlüğe yazma

                .when()
                .post("/school-service/api/attestation") // Belirtilen yola bir POST isteği gönderme

                .then() // Yanıtın değerlendirilmesi aşaması
                .log().body() // Yanıt gövdesini günlüğe yazma
                .statusCode(400) // Yanıt durum kodunun 400 (Hatalı İstek) olmasını kontrol etme
                .body("message", containsString("already")); // Yanıt gövdesindeki "message" alanının belirli bir metni içermesini kontrol etme
    }


    @Test(dependsOnMethods = "createAttestation")
    public void updateAttestation() {

        attestationName = "Post Graduation Certificates Attestation - " + randomGenerator.number().digits(5); // Yeni bir ad oluşturuluyor

        attestation.put("id", attestationID); // Güncellenecek belgenin kimliği ekleniyor
        attestation.put("name", attestationName); // Yeni ad, "name" anahtarıyla HashMap'e ekleniyor

        given()

                .spec(requestSpecification) // Önceden belirlenmiş istek yapılandırmasını kullanarak istek yapma
                .body(attestation) // İstek gövdesini HashMap olarak belirleme

                .when()
                .put("/school-service/api/attestation") // Belirtilen yola bir PUT isteği gönderme

                .then() // Yanıtın değerlendirilmesi aşaması
                .log().body() // Yanıt gövdesini günlüğe yazma
                .statusCode(200) // Yanıt durum kodunun 200 (Başarılı) olmasını kontrol etme
                .body("name", equalTo(attestationName)); // Yanıt gövdesindeki "name" alanının yeni ad ile eşleşmesini kontrol etme
    }


    @Test(dependsOnMethods = "updateAttestation")
    public void deleteAttestation() {

        given()

                .spec(requestSpecification) // Önceden belirlenmiş istek yapılandırmasını kullanarak istek yapma
                .log().uri() // İstek URI'sini günlüğe yazma

                .when()
                .delete("/school-service/api/attestation/" + attestationID) // Belirtilen yoldaki belgeyi silme isteği gönderme

                .then() // Yanıtın değerlendirilmesi aşaması
                .log().body() // Yanıt gövdesini günlüğe yazma
                .statusCode(204); // Yanıt durum kodunun 204 (Başarılı) olmasını kontrol etme
    }


    @Test(dependsOnMethods = "deleteAttestation")
    public void deleteAttestationNegative() {

        given()

                .spec(requestSpecification) // Önceden belirlenmiş istek yapılandırmasını kullanarak istek yapma
                .pathParam("attestationID", attestationID) // Yol parametresi olarak belge kimliğini belirleme
                .log().uri() // İstek URI'sini günlüğe yazma

                .when()
                .delete("/school-service/api/attestation/{attestationID}") // Belirtilen yoldaki belgeyi silme isteği gönderme

                .then() // Yanıtın değerlendirilmesi aşaması
                .log().body() // Yanıt gövdesini günlüğe yazma
                .statusCode(400) // Yanıt durum kodunun 400 (Hatalı İstek) olmasını kontrol etme
                .body("message", equalTo("attestation not found")); // Yanıt gövdesindeki "message" alanının belirli bir metni içermesini kontrol etme
    }
}

