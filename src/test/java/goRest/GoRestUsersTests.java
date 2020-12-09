package goRest;

import io.restassured.http.ContentType;
import org.apache.commons.lang3.RandomStringUtils;
import org.testng.annotations.Test;

import java.util.List;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class GoRestUsersTests {

    int userId;

    @Test(enabled = false)    // todo simdilik calismasin demek
    public void getUsers() {
        List<User> userList =
                given()
                        .when()
                        .get("https://gorest.co.in/public-api/users") // request linkini çalıştırdık.
                        .then()
                        //  .log().body()
                        .statusCode(200)   // dönenen durumun kontrolünü yaptık
                        .contentType(ContentType.JSON)  // burda dönen verinin type ni kontrol ettik.
                        .body("code", equalTo(200))  // dönen (respons) body nin ilk bölümündeki code un değeri kontorl edildi.
                        .body("data", not(empty()))   // data bölümünün bboş olmadığı kontrol edildi.
                        .extract().jsonPath().getList("data", User.class);  // en sona yazılır.

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
                        .body("{\"name\":\"create\", \"gender\":\"Male\", \"email\":\"" + getRandomEmail() + "\", \"status\":\"Active\"}")
                        .when()
                        .post("https://gorest.co.in/public-api/users")
                        .then()
                        .log().body()
                        .statusCode(200)
                        .contentType(ContentType.JSON)
                        .body("code", equalTo(201))
                        .extract().jsonPath().getInt(("data.id"))


        ;
    }

    private String getRandomEmail() {
        // todo  randomAlphabetic : emailde gecerli olmayan karakter uretirse hata olusturabilir.
        return RandomStringUtils.randomAlphabetic(8).toLowerCase() + "@gmail.com";
    }

    /**
     * Simdi Create edilen id bilgilerini listeleyip kontrol edecegiz
     */

    @Test(dependsOnMethods = "createUser")   // createUser calismadan bu test calismasin demek
    public void getUserById() {

        given()
                .pathParam("userId", userId)
                .when()
                .get("https://gorest.co.in/public-api/users/{userId}")
                .then()
                .log().body()
                .statusCode(200)
                .body("code", equalTo(200))
                .body("data.id", equalTo(userId))
        ;
    }

    /**
     * Simdi Create edilen id bilgilerini update edecegiz
     * Bearer 19a2009039e5c945cc19beaf601d053e37e1e8094418ea34cb8e1509a7ee196a
     * https://gorest.co.in/public-api/posts/{{userId}}
     * body -> {"name": "Edit User"}  JSON
     * normal kontrol ve name kontrol
     */

    @Test(dependsOnMethods = "createUser")
    public void updateUserById() {

        String newName = "Edit User";

        given()
                .header("Authorization", "Bearer 19a2009039e5c945cc19beaf601d053e37e1e8094418ea34cb8e1509a7ee196a")
                .contentType(ContentType.JSON)
                .body("{\"name\": \"" + newName + "\"}")
                .pathParam("userId", userId)
                .when()
                .put("https://gorest.co.in/public-api/users/{userId}")
                .then()
                .log().body()
                .statusCode(200)
                .body("code", equalTo(200))
                .body("data.name", equalTo(newName))

        ;

    }
    /**
     * https://gorest.co.in/public-api/users/{{userId}}
     * Bearer 19a2009039e5c945cc19beaf601d053e37e1e8094418ea34cb8e1509a7ee196a
     */

    @Test (dependsOnMethods = "createUser" , priority = 1)   // todo priority ile calisma sirasi belirlemis olduk.
                                                             // digerlerinde numara olmadigi icin en son delete calismis olacak
    public void deleteUserById(){

        given()
                .header("Authorization", "Bearer 19a2009039e5c945cc19beaf601d053e37e1e8094418ea34cb8e1509a7ee196a")
                .pathParam("userId",userId)
                .when()
                .delete("https://gorest.co.in/public-api/users/{userId}")
                .then()
                .statusCode(200)
                .body("code",equalTo(204))

                ;
    }

    @Test (dependsOnMethods = "deleteUserById" )
    public void deleteUserByIdNegative(){

        given()
                .header("Authorization", "Bearer 19a2009039e5c945cc19beaf601d053e37e1e8094418ea34cb8e1509a7ee196a")
                .pathParam("userId",userId)
                .when()
                .delete("https://gorest.co.in/public-api/users/{userId}")
                .then()
                .statusCode(200)
                .body("code",equalTo(404))

        ;
    }


}

