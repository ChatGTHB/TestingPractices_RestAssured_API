import goRest.User;
import io.restassured.response.Response;
import model.Location;
import model.Place;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.List;

import static io.restassured.RestAssured.given;

public class PathAndJsonPath {

    @Test
    public void extractingPath() {

        String postCode =

                given()

                        .when()
                        .get("http://api.zippopotam.us/us/90210")

                        .then()
                        .log().body()
                        .extract().path("'post code'");

        System.out.println("postCode = " + postCode);
    }

    @Test
    public void extractingJsonPath() {

        // "post code" : "90210"

        int postCode =

                given()

                        .when()
                        .get("http://api.zippopotam.us/us/90210")

                        .then()
                        .log().body()
                        .extract().jsonPath().getInt("'post code'") // Type return is automatic, appropriate type should be given
                ;

        System.out.println("postCode = " + postCode);
    }

    @Test
    public void getUsers() {

        Response response =

                given()

                        .when()
                        .get("https://gorest.co.in/public/v2/users")

                        .then()
//                        .log().body()
                        .extract().response();
        ;

        int idPath = response.path("[2].id");
        int idJsonPath = response.jsonPath().getInt("[2].id");

        System.out.println("idPath = " + idPath);
        System.out.println("idJsonPath = " + idJsonPath);

        User[] usersPath = response.as(User[].class); // as; array supported in object transformation (POJO)
        List<User> usersJsonPath = response.jsonPath().getList("", User.class); // JsonPath can export as List

        System.out.println("usersPath = " + Arrays.toString(usersPath));
        System.out.println("usersJsonPath = " + usersJsonPath);
    }

    @Test
    public void getUsersV1() {
        Response body =
                given()

                        .when()
                        .get("https://gorest.co.in/public/v1/users")

                        .then()
                        //.log().body()
                        .extract().response();
        ;

        List<User> dataUsers = body.jsonPath().getList("data", User.class);
        // We can convert a fragment in a JSONPATH response into an object.
        System.out.println("dataUsers = " + dataUsers);

        // In the previous examples (as) for Class transformations, we used to write all the necessary classes
        // corresponding to the whole structure and reach the elements we want by transforming them.

        // Here (JsonPath) we used JSONPATH, which allows us to convert
        // an intermediate data into a class and get it as a list.
        // Thus, data was obtained with a single class without the need for other classes.

        // path : Returns direct data that cannot allow class or type conversion. like List<String>
        // jsonPath : Allows class conversion and type conversion, giving the data in the format we want.
    }

    @Test
    public void getZipCode() {

        Response response =

                given()

                        .when()
                        .get("http://api.zippopotam.us/us/90210")

                        .then()
                        //.log().body()
                        .extract().response();

        Location locPathAs = response.as(Location.class); // You have to write all the classes
        System.out.println("locPathAs = " + locPathAs);
        System.out.println("locPathAs.getPlaces() = " + locPathAs.getPlaces());

        List<Place> places = response.jsonPath().getList("places", Place.class); // We got the object we wanted to pinpoint.
        System.out.println("places = " + places);
    }
}
