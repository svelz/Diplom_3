package api;

import io.restassured.response.Response;
import static io.restassured.RestAssured.given;

public class UserClient {
    public static Response createUser(UserCredentials user) {
        return given()
                .header("Content-Type", "application/json")
                .body(user)
                .post(Endpoints.API_USER_CREATE);
    }

    public static Response loginUser(UserCredentials user) {
        return given()
                .header("Content-Type", "application/json")
                .body(user)
                .post(Endpoints.API_USER_LOGIN);
    }

    public static Response deleteUser(String accessToken) {
        return given()
                .header("Authorization", accessToken)
                .delete(Endpoints.API_USER_DELETE);
    }
}
