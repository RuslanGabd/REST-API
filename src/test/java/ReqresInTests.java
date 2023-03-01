import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static org.hamcrest.Matchers.is;

public class ReqresInTests {
    private final String BASE_URL = "https://reqres.in";

    @Test
    void loginTest() {
        String data = "{ \"email\": \"eve.holt@reqres.in\", \"password\": \"cityslicka\" }";

        given()
                .log().uri()
                .contentType(JSON)
                .body(data)
                .when()
                .post(BASE_URL + "/api/login")
                .then()
                .log().status()
                .log().body()
                .statusCode(200)
                .body("token", is("QpwL5tke4Pnpja7X4"));
    }

    @DisplayName("Registration user")
    @Test
    void userRegister() {
        String data = "{ \"email\": \"eve.holt@reqres.in\", \"password\": \"qwerty1234\" }";

        given()
                .log().uri()
                .contentType(JSON)
                .body(data)
                .when()
                .post(BASE_URL + "/api/register")
                .then()
                .log().status()
                .log().body()
                .statusCode(200)
                .body("id", is(4),
                        "token", is("QpwL5tke4Pnpja7X4"));
    }

    @DisplayName("Registration  undefined user")
    @Test
    void registerUndefinedUser() {
        String data = "{ \"email\": \"eve.holt2reqres.in\", \"password\": \"qwerty1234\" }";

        given()
                .log().uri()
                .contentType(JSON)
                .body(data)
                .when()
                .post(BASE_URL + "/api/register")
                .then()
                .log().status()
                .log().body()
                .statusCode(400)
                .body("error", is("Note: Only defined users succeed registration"));
    }


    @DisplayName("Update user's information")
    @Test
    void editUser() {

        String data = "{ \"name\": \"morpheus\", \"job\": \"zion resident\" }";

        given()
                .log().uri()
                .contentType(JSON)
                .body(data)
                .when()
                .patch(BASE_URL + "/api/users/2")
                .then()
                .log().status()
                .log().body()
                .statusCode(200)
                .body("name", is("morpheus"),
                        "job", is("zion resident"));
    }

    @DisplayName("Delete")
    @Test
    public void delete() {
        given()
                .when()
                .delete(BASE_URL + "/api/users/2").then().log().all()
                .statusCode(204);
    }

}