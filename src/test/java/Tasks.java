
import io.restassured.http.ContentType;
import org.testng.Assert;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class Tasks {










    /**
     * Task 2
     * create a request to https://httpstat.us/203
     * expect status 203
     * expect content type TEXT
     */

    @Test
    public void task2()
    {
        given()

                .when()
                .get("https://httpstat.us/203")

                .then()
                .log().all()
                .statusCode(203)
                .contentType(ContentType.TEXT)
        ;
    }



}