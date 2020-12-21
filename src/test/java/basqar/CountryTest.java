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
        randomGenCode = RandomStringUtils.randomAlphabetic(4);

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

    @Test(dependsOnMethods = "createCountry")
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
                .body("message", equalTo("The Country with Name \"" + randomGenName + "\" already exists."))
        ;
    }

    /**
     * Update yaparken gonderilen degerler
     * "id": "5fd7aacb146e3837d4905511",
     * "name": "Vanuatu 788",
     * "shortName": null,
     * "translateName": [],
     * "code": "CX 764"
     */

    @Test(dependsOnMethods = "createCountry")
    public void updateCountry() {

        Country country = new Country();
        country.setId(id);
        country.setName(RandomStringUtils.randomAlphabetic(8));
        country.setCode(RandomStringUtils.randomAlphabetic(4));

        given()
                .cookies(cookies)
                .body(country)
                .contentType(ContentType.JSON)
                .log().body()
                .when()
                .put("/school-service/api/countries")
                .then()
                .statusCode(200)
                .body("name", equalTo(country.getName()))
                .body("code", equalTo(country.getCode()))

        ;
    }

    @Test(dependsOnMethods = "updateCountry")
    public void deleteCountryById() {

        given()
                .cookies(cookies)
                .pathParam("countryID", id)
                .when()
                .delete("/school-service/api/countries/{countryID}")
                .then()
                .statusCode(201)
                .body(equalTo(""))      // todo silince body bos geldigi icin bu sekilde kontrol yaptik

        ;
    }

    /** ayni id yi 2.kez silmek istedigimizde body de bu mesaj geliyor
     *
     *  "type": "https://support.mersys.io/cloud/problem/problem-with-message",
     *     "status": 404,
     *     "path": "/api/countries/5fd7aacb146e3837d4905511",
     *     "code": null,
     *     "message": "Country not found",
     *     "lang": null,
     *     "uri": null
     */

    @Test(dependsOnMethods = "deleteCountryById")
    public void deleteCountryByIdNegativTest() {

        given()
                .cookies(cookies)
                .pathParam("countryID", id)
                .when()
                .delete("/school-service/api/countries/{countryID}")
                .then()
                .statusCode(404)
                .body("message", equalTo("Country not found"))

            ;
    }
}
