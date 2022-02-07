package com.github.PublishInn.integrationTests;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.json.JSONException;

public class AuthenticationHelpers {
    private static final String baseUrl = "http://localhost:8080/api/";

    public static String getToken(String login, String password) {

        RequestSpecification request = RestAssured.given();
        request.param("username", login);
        request.param("password", password);

        Response response = request.post(baseUrl + "login");

        return response
                .jsonPath()
                .getString("access_token");
    }
}
