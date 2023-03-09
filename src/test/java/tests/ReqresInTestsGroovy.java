package tests;

import model.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static scpecifications.SpecModel.*;


public class ReqresInTestsGroovy {

    @Tag("Lombok")
    @Test
    void loginTest() {

        RegisterUserRequestModel requestModel = new RegisterUserRequestModel();
        requestModel.setEmail("eve.holt@reqres.in");
        requestModel.setPassword("cityslicka");
        RegisterUserResponseModel responseModel =
                step("Login to website", () ->
                        given(loginRequestSpec)
                                .body(requestModel)
                                .when()
                                .post("/login")
                                .then()
                                .spec(loginResponseSpec200)
                                .extract().as(RegisterUserResponseModel.class));

        assertThat(responseModel.getToken()).isEqualTo("QpwL5tke4Pnpja7X4");
    }

    @Tag("Lombok")
    @DisplayName("Registration user")
    @Test
    void userRegister() {

        RegisterUserRequestModel requestModel = new RegisterUserRequestModel();
        requestModel.setEmail("eve.holt@reqres.in");
        requestModel.setPassword("qwerty1234");

        RegisterUserResponseModel responseModel =
                given(loginRequestSpec)
                        .body(requestModel)
                        .when()
                        .post("register")
                        .then()
                        .spec(loginResponseSpec200)
                        .extract().as(RegisterUserResponseModel.class);

        assertThat(responseModel.getToken()).isNotNull();
        assertThat(responseModel.getId()).isEqualTo(4);
    }

    @Tag("Lombok")
    @DisplayName("Registration  undefined user")
    @Test
    void registerUndefinedUser() {

        RegisterUserRequestModel requestModel = new RegisterUserRequestModel();
        requestModel.setEmail("eve.holt2reqres.in");
        requestModel.setPassword("qwerty1234");

        RegisterUserErrorResponseModel responseModel =
                given(loginRequestSpec)
                        .body(requestModel)
                        .when()
                        .post("/register")
                        .then()
                        .spec(loginResponseSpecError)
                        .extract().as(RegisterUserErrorResponseModel.class);

        assertThat(responseModel.getError()).isEqualTo("Note: Only defined users succeed registration");
    }

    @Tag("Lombok")
    @DisplayName("Update user's  place of work")
    @Test
    void editUser() {
        String dateTime = DateTimeFormatter.ofPattern("yyyy-MM-dd").format(LocalDateTime.now());
        UpdateRequestModel requestModel = new UpdateRequestModel();
        requestModel.setName("morpheus");
        requestModel.setJob("zion resident");

        UpdateResponseModel responseModel =
                given(loginRequestSpec)
                        .body(requestModel)
                        .when()
                        .patch("/users/2")
                        .then()
                        .spec(loginResponseSpec200)
                        .extract().as(UpdateResponseModel.class);

        assertThat(responseModel.getUpdatedAt()).contains(dateTime);
    }

    @Tag("Lombok")
    @DisplayName("Delete user")
    @Test
    public void delete() {
        given(loginRequestSpec)
                .when()
                .delete("/users/2")
                .then()
                .spec(loginResponseSpec204);
    }

    @Tag("Groovy")
    @DisplayName("Verify user's email with groovy")
    @Test
    void emailTestGroovy() {
        given()
                .spec(loginRequestSpec)
                .when()
                .get("/users?page=2")
                .then()
                .spec(loginResponseSpec200)
                .body("data.findAll{it.email =~/.*?@reqres.in/}.email.flatten()",
                        hasItem("lindsay.ferguson@reqres.in"));
    }
}