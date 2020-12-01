
import io.restassured.http.ContentType;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class ZippoTest {

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
                .statusCode(200)        // todo status islem kodunu assertion yapiyoruz. Dogr sonuc alinca farkedemeyebiliriz ama islem goruyor.
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
    public void logTest(){

        given()
                .log().all()        // gondermeden onceki bilgiler

                .when()
                .get("http://api.zippopotam.us/us/90210")

                .then()
                .log().body()       // gonderdikten sonra sadece body yazdirdik

                ;
    }

    @Test
    public void bodyJsonPathTest(){  // Path Json daki bir ogeyi almak

        given()
                .when()
                .get("http://api.zippopotam.us/us/90210")

                .then()
                .log().body()
                .body("country",equalTo("United States"))  // body den kodu almadan kontrol etmeye yardimci oluyor
                .statusCode(200)

                ;

    }


    @Test
    public void bodyJsonPathTest2(){  // Path Json daki bir ogeyi almak

        given()
                .when()
                .get("http://api.zippopotam.us/us/90210")

                .then()
                .log().body()
                .body("places[0].state",equalTo("California"))  // body den kodu almadan kontrol etmeye yardimci oluyor
                .statusCode(200)

        ;

    }

    @Test
    public void bodyJsonPathTest3(){  // Path Json daki bir ogeyi almak

        given()
                .when()
                .get("http://api.zippopotam.us/us/90210")

                .then()
                .log().body()
                .body("places[0].'place name'",equalTo("Beverly Hills"))  // body den kodu almadan kontrol etmeye yardimci oluyor
                // veri adinda arada bosluk varsa key basina '' konur. 'place name' seklinde
                .statusCode(200)

        ;

    }

    @Test
    public void bodyArraySizeTest(){

        given()
                .when()
                .get("http://api.zippopotam.us/us/90210")

                .then()
                .log().body()
                .body("places",hasSize(1))  // places dizisinin uzunlugunu sorguladik
                .statusCode(200)

        ;

    }

    @Test
    public void combiningTest(){

        given()
                .when()
                .get("http://api.zippopotam.us/us/90210")

                .then()
                .log().body()
                .body("places",hasSize(1))
                .body("places[0].state",equalTo("California"))
                .body("places[0].'place name'",equalTo("Beverly Hills"))
                .statusCode(200)

        ;

    }
}
