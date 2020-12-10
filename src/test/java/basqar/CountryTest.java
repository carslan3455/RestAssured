package basqar;

import io.restassured.http.ContentType;
import io.restassured.http.Cookies;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class CountryTest {

    Cookies cookies ;

    @BeforeClass
    public void login() {

        baseURI = "https://test.basqar.techno.study";
        // {"username": "daulet2030@gmail.com", "password": "TechnoStudy123@", "rememberMe": "true"}

        Map<String, String> credentials = new HashMap<>();
        credentials.put("username", "daulet2030@gmail.com");
        credentials.put("password", "TechnoStudy123@");
        credentials.put("rememberMe", "true");

       cookies = given()
                .body(credentials)  // todo json i direk yazmak yerine MAp olarak da verebiliriz
                .contentType(ContentType.JSON) // verilen bilgileri Json formati ile gonder dedik
                .when()
                .post("/auth/login")  // todo http yoksa baseURI kullanacagini algiliyor
                .then()
                .statusCode(200)
                .extract().response().getDetailedCookies()

        ;
        System.out.println("cookies: "+cookies);

    }


    @Test
    public void createCountry() {

    }
}
