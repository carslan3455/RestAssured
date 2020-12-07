package Task;

import io.restassured.http.ContentType;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class TaskSolution {

    /**
     * Task1
     * create a request to https://httpstat.us/203
     * expect status 203
     * expect content type TEXT
     */

    @Test
    public void task1() {

        given()
                .when()
                .get("https://httpstat.us/203")
                .then()
                .log().body()
                .statusCode(203)
                .contentType(ContentType.TEXT)
        ;

    }

    /**
     * Task2
     * create a request to https://httpstat.us/418
     * expect status 218
     * expect content type TEXT
     * expect body to be equel to "418 I'm a teapot"
     * todo Hocada kod calismadi 203 icin ayni islemleri yapti
     */

    @Test
    public void task2() {

        given()
                .when()
                .get("https://httpstat.us/418")

                .then()
                .log().body()
                .statusCode(418)
                .contentType(ContentType.TEXT)
                .body(equalTo("418 I'm a teapot"))

        ;
    }

    /**
     * Task 3
     * create a request to https://jsonplaceholder.typicode.com/todos/2
     * expect status 200
     * expect content type JSON
     * expect title in response body to be "quis ut nam facilis et officia qui"
     */

    @Test
    public void task3() {

        given()
                .when()
                .get("https://jsonplaceholder.typicode.com/todos/2")

                .then()
                .log().body()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("title", equalTo("quis ut nam facilis et officia qui"))

        ;
    }


    /**
     * Task 4
     * create a request to https://jsonplaceholder.typicode.com/todos/2
     * expect status 200
     * expect content type JSON
     * expect completed in response body to be "false"
     */

    @Test
    public void task4() {

        given()
                .when()
                .get("https://jsonplaceholder.typicode.com/todos/2")

                .then()
                .log().body()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("completed", equalTo(false))  // burada "false" seklinde yazamayiz. String degil boolean cevap
                //todo  .body("completed",equalTo(0>1))  (0>1) = false verdigi icin bu sekilde gelebilecek sartlar da olabilir
        ;
    }

    /** Task 5
     * create a request to https://jsonplaceholder.typicode.com/todos
     * expect status 200
     * expect content type JSON
     * expect third item have:
     *      title = "fugiat veniam minus"
     *      userId = 1
     */

    @Test
    public void task5() {

        given()
                .when()
                .get("https://jsonplaceholder.typicode.com/todos")

                .then()
                .log().body()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("userId[2]",equalTo(1))
                .body("title[2]",equalTo("fugiat veniam minus"))
                ;
    }

    @Test
    public void task6() {

        given()
                .when()
                .get("https://jsonplaceholder.typicode.com/todos")

                .then()
                .log().body()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("userId[2]",equalTo(1))
                .body("title[2]",equalTo("fugiat veniam minus"))
        ;
    }
}
