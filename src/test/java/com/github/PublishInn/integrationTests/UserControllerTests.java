package com.github.PublishInn.integrationTests;

import com.github.PublishInn.model.entity.enums.AppUserRole;
import io.restassured.RestAssured;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class UserControllerTests {
    private static String adminToken;

    @BeforeAll
    static void setup() {
        RestAssured.baseURI = "http://localhost:8080/api/users/";
        adminToken = AuthenticationHelpers.getToken("matz98", "zaq1@WSX");
    }

    @Test
    void successfulFindAllUsersTest() throws JSONException {
        var request = RestAssured.given();
        request.header("Content-Type", "application/json");
        request.header("Authorization", "Bearer " + adminToken);

        var result = request.get("admin");

        JSONArray array = new JSONArray(result.body().asString());
        JSONObject object = new JSONObject(array.get(0).toString());

        Assertions.assertEquals(3, array.length());
        Assertions.assertEquals(AppUserRole.ADMIN.toString(), object.get("appUserRole"));
        Assertions.assertEquals("matz98", object.get("username"));
        Assertions.assertEquals("matz98@edu.pl", object.get("email"));
        Assertions.assertEquals(false, object.get("locked"));
        Assertions.assertEquals(true, object.get("enabled"));
    }

    @Test
    void successfulGetUserInfoTest() throws JSONException {
        var request = RestAssured.given();
        request.header("Content-Type", "application/json");
        request.header("Authorization", "Bearer " + adminToken);

        var result = request.get("admin/moderator");
        result.then()
                .statusCode(200);
        JSONObject object = new JSONObject(result.body().asString());
        Assertions.assertEquals(AppUserRole.MODERATOR.toString(), object.get("appUserRole"));
        Assertions.assertEquals("moderator", object.get("username"));
        Assertions.assertEquals("matz@edu.pl", object.get("email"));
        Assertions.assertEquals(false, object.get("locked"));
        Assertions.assertEquals(true, object.get("enabled"));
    }
}
