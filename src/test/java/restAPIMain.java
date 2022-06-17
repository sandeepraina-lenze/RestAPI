import io.restassured.authentication.FormAuthConfig;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;

public class restAPIMain {
    public static String clientID = "MosaiqStore";
    public static String redirectUri = "https://machines.mosaiq.one/api/swagger-ui/oauth2-redirect.html";
    public static String username = "auto@test1.org";
    public static String password = "auto";

    public static HttpResponse<String> getTokenDirectUnirest() {
        HttpResponse<String> response = Unirest.post("https://auth.staging.mosaiq-test.de/auth/realms/MosaiqStore/protocol/openid-connect/token")
                .header("Content-Type", "application/x-www-form-urlencoded")
                .header("User-Agent", "PostmanRuntime/7.28.4")
                .field("grant_type", "password")
                .field("username", "auto@test1.org")
                .field("password", "auto")
                .field("client_id", "MosaiqStore")
                .asString();

        return response;
    }

    public static Response getTokenDirect() {
        return
                given()
                        .contentType(ContentType.URLENC)
                        .formParam("grant_type", "password")
                        .formParam("username", username)
                        .formParam("password", password)
                        .formParam("client_id", clientID)
                        //.formParam("redirect_uri", redirectUri)
                        .post("https://auth.staging.mosaiq-test.de/auth/realms/MosaiqStore/protocol/openid-connect/token")
                        .then()
                        .statusCode(200)
                        .extract()
                        .response();
    }

    public static Response GetMachineList() {
        return
                given().auth()
                        .oauth2(getTokenDirectUnirest().getBody())
                        .when()
                        .get("https://machines.mosaiq.one/api/v0/machines")
                        .then()
                        .statusCode(200)
                        .extract()
                        .response();
    }

    public static void main(String[] args) {
        System.out.println(GetMachineList().body().prettyPrint());
    }
}
