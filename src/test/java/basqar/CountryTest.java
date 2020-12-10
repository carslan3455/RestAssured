package basqar;

import basqar.model.Country;
import io.restassured.http.ContentType;
import io.restassured.http.Cookies;
import org.apache.commons.lang3.RandomStringUtils;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class CountryTest {

    Cookies cookies;
    private String randomGenName;
    private String randomGenCode;
    String id;

    @BeforeClass        // todo Background mantiginda olusturmus olduk. Her test ten once calismasi lazim
    public void login() {

        baseURI = "https://test.basqar.techno.study";
        // {"username": "daulet2030@gmail.com", "password": "TechnoStudy123@", "rememberMe": "true"}

        randomGenName = RandomStringUtils.randomAlphabetic(8);
        randomGenName = RandomStringUtils.randomAlphabetic(4);

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
        System.out.println("cookies: " + cookies);

    }


    @Test
    public void createCountry() {

        Country country = new Country();
        country.setName(randomGenName);
        country.setCode(randomGenCode);

        id = given()
                .body(country)    // todo JSON formatında vermek yerine NESNE olarak daha kolay formatta verdik.
                .contentType(ContentType.JSON)   // verilen bilgiyi JSON olarak gönder
                .cookies(cookies)    // aldığımız yetki bilgilerini barındıran bilgileri tekrar göndererek yetkili işlem yaptığımızı belirttik.

                .when()
                .post("/school-service/api/countries")
                .then()
                .log().body()
                .statusCode(201)
                .body("name", equalTo(randomGenName))
                .body("code", equalTo(randomGenCode))
                .extract().jsonPath().getString("id")
                //.extract().path("id")  // todo 2. yontem

                ;

        System.out.println("id: " + id);

    }

    @Test (dependsOnMethods = "createCountry")
    public void createCountryNegative() {

        Country country = new Country();
        country.setName(randomGenName);
        country.setCode(randomGenCode);

         given()
                .body(country)    // todo JSON formatında vermek yerine NESNE olarak daha kolay formatta verdik.
                .contentType(ContentType.JSON)   // verilen bilgiyi JSON olarak gönder
                .cookies(cookies)    // aldığımız yetki bilgilerini barındıran bilgileri tekrar göndererek yetkili işlem yaptığımızı belirttik.

                .when()
                .post("/school-service/api/countries")
                .then()
                .log().body()
                .statusCode(400)
                .body("message", equalTo("The Country with Name \""+randomGenName+"\" already exists."))

                 ;


    }
}
