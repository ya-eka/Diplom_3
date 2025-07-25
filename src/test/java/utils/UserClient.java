package utils;

import io.restassured.RestAssured;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserClient {
    private static final String BASE_URL = "https://stellarburgers.nomoreparties.site";

    public static String createUser(User user) {
        Response response = given()
                .contentType("application/json")
                .body(user)
                .when()
                .post(BASE_URL + "/api/auth/register")
                .then()
                .extract()
                .response();

        assertEquals(200, response.getStatusCode(), "Не удалось создать пользователя");

        String token = response.jsonPath().getString("accessToken");
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7); // убираем "Bearer " если есть
        }
        return token;
    }

    public static String loginAndGetToken(User user) {
        Response response = given()
                .contentType("application/json")
                .body("{ \"email\": \"" + user.getEmail() + "\", \"password\": \"" + user.getPassword() + "\" }")
                .when()
                .post(BASE_URL + "/api/auth/login")
                .then()
                .extract()
                .response();

        assertEquals(200, response.getStatusCode(), "Не удалось авторизоваться");

        String token = response.jsonPath().getString("accessToken");
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7); // убираем "Bearer " если есть
        }
        return token;
    }

    public static void deleteUser(String accessToken) {
        if (accessToken != null && !accessToken.isEmpty()) {
            String bearerToken = accessToken.startsWith("Bearer ") ? accessToken : "Bearer " + accessToken;

            RestAssured.given()
                    .header("Authorization", bearerToken)
                    .when()
                    .delete(BASE_URL + "/api/auth/user")
                    .then()
                    .statusCode(202);
        }
    }
}