package goRest;

import io.restassured.http.ContentType;
import org.apache.commons.lang3.RandomStringUtils;
import org.testng.annotations.Test;

import java.util.List;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class GoRestUsersTests {

    int userId;

    @Test
    public void getUsers() {
        List<User> userList =
                given()
                        .when()
                        .get("https://gorest.co.in/public-api/users")
                        .then()
                        //  .log().body()
                        .statusCode(200)
                        .contentType(ContentType.JSON)
                        .body("code", equalTo(200))
                        .body("data", not(empty()))
                        .extract().jsonPath().getList("data", User.class);

        for (User us : userList) {

            System.out.println(us.toString());
        }
    }


    /**
     * https://gorest.co.in/public-api/users -> post
     * Bearer 19a2009039e5c945cc19beaf601d053e37e1e8094418ea34cb8e1509a7ee196a
     * {"name":"{{$randomFullName}}", "gender":"Male", "email":"{{$randomEmail}}", "status":"Active"}
     * body content JSON
     * Islemin sonucunda Id almistik
     * genel status kontrol
     */


    @Test
    public void createUser() {
        userId =
                given()
                        .header("Authorization", "Bearer 19a2009039e5c945cc19beaf601d053e37e1e8094418ea34cb8e1509a7ee196a")
                        .contentType(ContentType.JSON)
                        .body("{\"name\":\"{{$randomFullName}}\", \"gender\":\"Male\", \"email\":\""+getRandomEmail()+"\", \"status\":\"Active\"}")
                        .when()
                        .post("https://gorest.co.in/public-api/users")
        .then()
        .log().body()
        .statusCode(200)
        .contentType(ContentType.JSON)
        .body("code",equalTo(201))
        .extract().jsonPath().getInt(("data.id"))


        ;
    }

    private String getRandomEmail(){

        return RandomStringUtils.randomAlphabetic(8).toLowerCase()+"@gmail.com";
    }

    /**
     * Simdi Create edilen id bilgilerini listeleyip kontrol edecegiz
     */

    @Test (dependsOnMethods = "createUser")
    public void getUserById(){

        given()
                .pathParam("userId",userId)
                .when()
                .get("https://gorest.co.in/public-api/users/{userId}")
                .then()
                .log().body()
                .statusCode(200)
                .body("code",equalTo(200))
                .body("data.id",equalTo(userId))
                ;
    }

}

