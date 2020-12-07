
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.ResponseSpecification;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import pojo.Location;

import java.util.List;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class ZippoTest {

    private ResponseSpecification responseSpecification;

    @BeforeClass
    public void setup() {

        baseURI = "http://api.zippopotam.us";      // static REST Assured un kendi degiskeni
        requestSpecification = new RequestSpecBuilder()
                .log(LogDetail.URI)
                .setAccept(ContentType.JSON)
                .build();

        responseSpecification = new ResponseSpecBuilder()
                .expectStatusCode(200)
                .expectContentType(ContentType.JSON)
                .log(LogDetail.BODY)
                .build();
    }


    @Test
    public void bodyArraySizeTestBaseUri() {

        given()
                .log().uri()
                .when()
                .get("/us/90210")       // todo http ile baslamiyorsa baseURI kullanacak
                // BeforeClass da tanimladik baseURI yi

                .then()
                .log().body()
                .body("places", hasSize(1))
                .statusCode(200)
        ;
    }

    @Test
    public void bodyArraySizeTestrequestSpecification() {

        given()
                .spec(requestSpecification)     // todo BeforeClass icinde yazilanlari ayniyla cekip sorgu kullandik
                .when()
                .get("/us/90210")       // todo http ile baslamiyorsa baseURI kullanacak
                // BeforeClass da tanimladik baseURI yi

                .then()
                .body("places", hasSize(1))
                .spec(responseSpecification)    // todo BeforeClass icinde tanimlanan butun islemleri bu satir ile cagirmis oluyoruz

        ;
    }

    @Test
    public void bodyArraySizeTestResponseSpecification() {

        given()
                .log().uri()
                .when()
                .get("/us/90210")       // todo http ile baslamiyorsa baseURI kullanacak
                // BeforeClass da tanimladik baseURI yi

                .then()
                .body("places", hasSize(1))
                .spec(responseSpecification)    // todo BeforeClass icinde tanimlanan butun islemleri bu satir ile cagirmis oluyoruz

        ;
    }

    @Test
    public void test() {
        given()     // Baslangic ayarlari (Setup) islemleri
                .when()     // Aksiyon
                .then()     // test kismi
        ;

    }

    @Test
    public void statusCodeTest() {

        given()
                .when()
                .get("http://api.zippopotam.us/us/90210")  // todo Postman da manuel olarak yaptiklarimizi burda automation yapiyoruz

                .then()
                .log().all()  // sorguyu ekrana yazdiriyor
                .statusCode(200)        // todo status islem kodunu assertion yapiyoruz. Dogru sonuc alinca farkedemeyebiliriz ama islem goruyor.
        // Hatali kod yazsak ( mesela .statusCode(201) ) bu islem icin  hata mesaji veriyor
        ;
    }

    @Test
    public void contentTypeTest() {

        given()
                .when()
                .get("http://api.zippopotam.us/us/90210")

                .then()
                .log().body()    // todo bu kisma all() gelirse tum bilgiler ekrana yazilmis oluyor. body() olunca sadece body kismi geliyor
                .contentType(ContentType.JSON)   // gelen formatin JSON formatinda olup olmadigini kontrol ediyor
        // normalde zaten JSON formatinda geliyor. Biz test etmis olduk.
        // contentType : Icerigin tipi
        ;
    }

    @Test
    public void logTest() {

        given()
                .log().all()        // gondermeden onceki bilgiler

                .when()
                .get("http://api.zippopotam.us/us/90210")

                .then()
                .log().body()       // gonderdikten sonra sadece body yazdirdik

        ;
    }

    @Test
    public void bodyJsonPathTest() {  // Path Json daki bir ogeyi almak

        given()
                .when()
                .get("http://api.zippopotam.us/us/90210")

                .then()
                .log().body()
                .body("country", equalTo("United States"))  // body den kodu almadan kontrol etmeye yardimci oluyor
                .statusCode(200)

        ;

    }


    @Test
    public void bodyJsonPathTest2() {  // Path Json daki bir ogeyi almak

        given()
                .when()
                .get("http://api.zippopotam.us/us/90210")

                .then()
                .log().body()
                .body("places[0].state", equalTo("California"))  // body den kodu almadan kontrol etmeye yardimci oluyor
                .statusCode(200)

        ;

    }

    @Test
    public void bodyJsonPathTest3() {  // Path Json daki bir ogeyi almak

        given()
                .when()
                .get("http://api.zippopotam.us/us/90210")

                .then()
                .log().body()
                .body("places[0].'place name'", equalTo("Beverly Hills"))  // body den kodu almadan kontrol etmeye yardimci oluyor
                // todo veri adinda arada bosluk varsa key basina '' konur. 'place name' seklinde
                .statusCode(200)

        ;

    }

    @Test
    public void bodyArraySizeTest() {

        given()
                .when()
                .get("http://api.zippopotam.us/us/90210")

                .then()
                .log().body()
                .body("places", hasSize(1))  // places dizisinin uzunlugunu sorguladik
                .statusCode(200)
        ;
    }

    @Test
    public void combiningTest() {

        given()
                .when()
                .get("http://api.zippopotam.us/us/90210")

                .then()
                .log().body()
                .body("places", hasSize(1))
                .body("places[0].state", equalTo("California"))
                .body("places[0].'place name'", equalTo("Beverly Hills"))
                .statusCode(200)
        ;
    }

    @Test
    public void pathParamTest() {

        String country = "us";
        String zipKod = "90210";

        given()
                .pathParam("country", country)
                .pathParam("zipKod", zipKod)
                .log().uri()   // calismadan once olusturulan URL verir
                .when()
                .get("http://api.zippopotam.us" + "/{country}/{zipKod}")

                .then()
                .body("places", hasSize(1))
                .log().body()

        ;
    }
    //  https://gorest.co.in/public-api/users?page=10

    @Test
    public void queryParamTest() {
        int page = 10;

        given()
                .param("page", page)
                .log().uri()

                .when()
                .get("https://gorest.co.in/public-api/users")

                .then()
                .log().body()
                .body("meta.pagination.page", equalTo(page))     // verilen yerdeki bilgi esit mi?

        ;
    }

    @Test
    public void queryParamTestCoklu() {
        for (int page = 1; page <= 10; page++) {      // page 1 olunca
            given()
                    .param("page", page)        // burasi da 1 uzantili olan sayfayi acacak
                    .log().uri()

                    .when()
                    .get("https://gorest.co.in/public-api/users")

                    .then()
                    .log().body()
                    .body("meta.pagination.page", equalTo(page))        // burda page karsiligi dongu devam ettikce 1,2,3....10 kadar devam
            ;
        }
    }

    @Test
    public void extractingJsonPath() {

        String extractValue =

                given()
                        .when()
                        .get("/us/90210")       // todo http ile baslamiyorsa baseURI kullanacak

                        .then()
                        .log().body()               // bu kisim olmak zorunda degil. Görmek icin yazdirdik
                        .extract().path("places[0].'place name'");
        System.out.println(extractValue);
        Assert.assertEquals(extractValue, "Beverly Hills");

    }

    @Test
    public void extractingJsonPathList() {

        List<String> liste =
        given()
                .when()
              // .get("/us/90210")
                .get(" /tr/01000")  // bu kisim (   /tr/01000  ->  adana ve ilceleri)  seklinde olunca daha fazla place name verirdi.

                .then()
                .log().body()
                .extract().path("places.'place name'")     // [0] dizinin bir eleman indexi verilmeyince
                                                             // dizidei butun 'place name' leri alir
                                                            // donus tipi String List olur
        ;
        System.out.println(liste);
        Assert.assertTrue(liste.contains("Çaputçu Köyü"));
    }

    @Test
    public void extractingJsonPojo(){  // todo bu islem pojo islemi. pojo package daki classlar yardimi ile bilgileri Nesneye donusturduk.
       Location location =
        given()
                .when()
                .get("/us/90210")
                .then()
               // .log().body()
                .extract().as(Location.class)
                ;
        System.out.println(location);
        System.out.println(location.getCountry());
        System.out.println(location.getPlaces());
        System.out.println(location.getPlaces().get(0).getState());
    }

}
