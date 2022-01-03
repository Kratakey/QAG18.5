package api;

import io.qameta.allure.restassured.AllureRestAssured;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static filters.CustomLogFilter.customLogFilter;
import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.*;

public class Tests {

    String
            email = "email@test.gg",
            password = "1234Pass#";

    @Test
    void simpleLogin() {
        step("Login by API", () -> {
            given()
                    .filter(customLogFilter().withCustomTemplates())
                    .log().all()
                    .contentType("application/x-www-form-urlencoded; charset=UTF-8")
                    .formParam("Email", email)
                    .formParam("Password", password)
                    .when()
                    .post("http://demowebshop.tricentis.com/login")
                    .then()
                    .log().body()
                    .statusCode(302);
        });
    }

    @Test
    void noLogsTest() {
        given()
                .get("https://demoqa.com/BookStore/v1/Books")
                .then()
                .body("books", hasSize(greaterThan(0)));
    }

    @Test
    void withAllLogsTest() {
        given()
                .log().all()
                .get("https://demoqa.com/BookStore/v1/Books")
                .then()
                .log().all()
                .body("books", hasSize(greaterThan(0)));
    }

    @Test
    void withSomeLogsTest() {
        given()
                .log().uri()
                .log().body()
                .get("https://demoqa.com/BookStore/v1/Books")
                .then()
                .log().body()
                .body("books", hasSize(greaterThan(0)));
    }


    @Test
    void authorizeTest() {
        Map<String, String> data = new HashMap<>();
        data.put("userName", email);
        data.put("password", password);

        given()
                .contentType("application/json")
                .accept("application/json")
                .body(data)
                .when()
                .log().uri()
                .log().body()
                .post("https://demoqa.com/Account/v1/GenerateToken")
                .then()
                .log().body()
                .body("status", is("Success"))
                .body("result", is("User authorized successfully."));
    }

    @Test
    void authorizeWithListenerTest() {
        Map<String, String> data = new HashMap<>();
        data.put("userName", email);
        data.put("password", password);

        given()
                .filter(new AllureRestAssured())
                .contentType("application/json")
                .accept("application/json")
                .body(data)
                .when()
                .log().uri()
                .log().body()
                .post("https://demoqa.com/Account/v1/GenerateToken")
                .then()
                .log().body()
                .body("status", is("Success"))
                .body("result", is("User authorized successfully."));
    }

    @Test
    void authorizeWithTemplatesTest() {
        Map<String, String> data = new HashMap<>();
        data.put("userName", email);
        data.put("password", password);

        step("Generate token", () ->
                given()
                        .filter(customLogFilter().withCustomTemplates())
                        .contentType("application/json")
                        .accept("application/json")
                        .body(data)
                        .when()
                        .log().uri()
                        .log().body()
                        .post("https://demoqa.com/Account/v1/GenerateToken")
                        .then()
                        .log().body()
                        .body("status", is("Success"))
                        .body("result", is("User authorized successfully."))
        );
        step("Any UI action");
    }

    @Test
    void loginWithShema() {
        Map<String, String> data = new HashMap<>();
        data.put("userName", email);
        data.put("password", password);

        given()
                .filter(new AllureRestAssured())
                .contentType("application/json")
                .body(data)
                .when()
                .log().all()
                .post("https://demoqa.com/Account/v1/Authorized")
                .then()
                .log().body()
                .body(matchesJsonSchemaInClasspath("shemas/loginShema.json"));
    }
}
