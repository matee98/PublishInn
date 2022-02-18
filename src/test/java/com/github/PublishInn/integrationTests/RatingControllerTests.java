package com.github.PublishInn.integrationTests;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class RatingControllerTests {
    private static String userToken;

    @BeforeAll
    static void setup() {
        RestAssured.baseURI = "http://localhost:8080/api/";
        userToken = AuthenticationHelpers.getToken("user", "zaq1@WSX");
    }

    @Test
    void successfulAddAndGetRatingTest() throws JSONException {
        var request = RestAssured.given();
        JSONObject content = new JSONObject();
        content.put("workId", 3);
        content.put("rate", 7);
        request.header("Content-Type", "application/json");
        request.header("Authorization", "Bearer " + userToken);
        request.body(content.toString());

        var response = request.post("ratings");
        response.then()
                .statusCode(201);

        RequestSpecification request2 = RestAssured.given();
        request2.header("Authorization", "Bearer " + userToken);
        Response result = request2.get("ratings?username=user&work_id=3");
        result.then()
                .statusCode(200);
        JSONObject obj = new JSONObject(result.body().asString());

        Assertions.assertEquals(3, obj.get("workId"));
        Assertions.assertEquals("user", obj.get("username"));
        Assertions.assertEquals(7, obj.get("rate"));
        Assertions.assertEquals("Przykład3", obj.get("title"));
        Assertions.assertEquals("moderator", obj.get("authorName"));
    }
}
