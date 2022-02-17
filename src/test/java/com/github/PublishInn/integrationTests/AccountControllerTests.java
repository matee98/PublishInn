package com.github.PublishInn.integrationTests;

import com.github.PublishInn.model.entity.enums.AppUserRole;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class AccountControllerTests {
    private static String token;

    @BeforeAll
    static void setup() {
        RestAssured.baseURI = "http://localhost:8080/api/account/";
        token = AuthenticationHelpers.getToken("user", "zaq1@WSX");
    }

    @Test
    void successfulGetOwnInfo() throws JSONException {
        RequestSpecification req = RestAssured.given();
        req.header("Authorization", "Bearer " + token);
        Response result = req.get("info");
        JSONObject obj = new JSONObject(result.body().asString());

        result.then().statusCode(200);
        Assertions.assertEquals(obj.get("username"), "user");
        Assertions.assertEquals(obj.get("email"), "matt@edu.pl");
        Assertions.assertEquals(obj.get("appUserRole"), AppUserRole.USER.toString());
        Assertions.assertEquals(obj.get("locked"), false);
        Assertions.assertEquals(obj.get("enabled"), true);
    }
}
