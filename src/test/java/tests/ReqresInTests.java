package tests;

import model.UpdateResponseModel;
import model.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static io.qameta.allure.Allure.step;
import static scpecifications.SpecModel.*;


public class ReqresInTests {

    @Test
    void loginTest() {

        RegisterUserRequestModel requestModel = new RegisterUserRequestModel();
        requestModel.setEmail("eve.holt@reqres.in");
        requestModel.setPassword("cityslicka");
        RegisterUserResponseModel responseModel =
                step("Login to website successful", () ->
                        given(loginRequestSpec)
                                .body(requestModel)
                                .when()
                                .post("/login")
                                .then()
                                .spec(loginResponseSpec200)
                                .extract().as(RegisterUserResponseModel.class));

        assertThat(responseModel.getToken()).isEqualTo("QpwL5tke4Pnpja7X4");


    }

    @DisplayName("Registration user")
    @Test
    void userRegister() {

        RegisterUserRequestModel requestModel = new RegisterUserRequestModel();

        requestModel.setEmail("eve.holt@reqres.in");
        requestModel.setPassword("qwerty1234");

        RegisterUserResponseModel responseModel =
                step("Registration on site successful", () ->
                        given(loginRequestSpec)
                                .body(requestModel)
                                .when()
                                .post("register")
                                .then()
                                .spec(loginResponseSpec200)
                                .extract().as(RegisterUserResponseModel.class));

        assertThat(responseModel.getToken()).isNotNull();
        assertThat(responseModel.getId()).isEqualTo(4);

    }


    @DisplayName("Registration  undefined user")
    @Test
    void registerUndefinedUser() {

        RegisterUserRequestModel requestModel = new RegisterUserRequestModel();

        requestModel.setEmail("eve.holt2reqres.in");
        requestModel.setPassword("qwerty1234");

        RegisterUserErrorResponseModel responseModel =
                step("Registration  undefined user", () ->
                        given(loginRequestSpec)
                                .body(requestModel)
                                .when()
                                .post("/register")
                                .then()
                                .spec(loginResponseSpecError)
                                .extract().as(RegisterUserErrorResponseModel.class));

        assertThat(responseModel.getError()).isEqualTo("Note: Only defined users succeed registration");

    }


    @DisplayName("Update user's information")
    @Test
    void editUser() {

        UpdateRequestModel requestModel = new UpdateRequestModel();
        requestModel.setName("morpheus");
        requestModel.setJob("zion resident");


        UpdateResponseModel responseModel = step("Update user's information", () ->
                given(loginRequestSpec)
                        .body(requestModel)
                        .when()
                        .patch("/users/2")
                        .then()
                        .spec(loginResponseSpec200)
                        .extract().as(UpdateResponseModel.class));


        assertThat(responseModel.getName()).isEqualTo("morpheus");
        assertThat(responseModel.getJob()).isEqualTo("zion resident");
    }

    @DisplayName("Delete")
    @Test
    public void delete() {
        step("Check delete user", () ->
                given(loginRequestSpec)
                        .when()
                        .delete("/users/2")
                        .then()
                        .spec(loginResponseSpec200));
    }

}