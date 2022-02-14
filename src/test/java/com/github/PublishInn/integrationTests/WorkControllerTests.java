package com.github.PublishInn.integrationTests;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class WorkControllerTests {

    private static String token;

    @BeforeAll
    static void setup() {
        RestAssured.baseURI = "http://localhost:8080/api/works/";
        token = AuthenticationHelpers.getToken("moderator", "zaq1@WSX");
    }

    @Test
    public void successfulFindAll() throws JSONException {
        Response result = RestAssured.get();
        result.then()
                .statusCode(200);

        JSONArray array = new JSONArray(result.body().asString());
        JSONObject object = new JSONObject(array.get(0).toString());

        Assertions.assertEquals(object.get("id"), 1);
        Assertions.assertEquals(object.get("title"), "Przykład1");
        Assertions.assertEquals(object.get("username"), "user");
        Assertions.assertEquals(object.get("type"), "FANTASY");
    }

    @Test
    public void successfulGetById() throws JSONException {
        Response result = RestAssured.get("details/2");
        result.then()
                .assertThat()
                .statusCode(200);
        var obj = new JSONObject(result.body().asString());

        Assertions.assertEquals(obj.get("username"), "user");
        Assertions.assertEquals(obj.get("rating"), 8.27);
        Assertions.assertEquals(obj.get("type"), "OTHER");
        Assertions.assertEquals(obj.get("title"), "Przykład2");
        Assertions.assertEquals(obj.get("status"), "ACCEPTED");
    }

    @Test
    public void unsuccessfulGetById() {
        Response result = RestAssured.get("details/21");
        result.then()
                .assertThat()
                .statusCode(404);
    }

    @Test
    public void successfulGetWorksByUsername() throws JSONException {
        Response result = RestAssured.get("user/user");
        result.then()
                .assertThat()
                .statusCode(200);
        JSONArray array = new JSONArray(result.body().asString());
        JSONObject object = new JSONObject(array.get(1).toString());

        Assertions.assertEquals(array.length(), 2);
        Assertions.assertEquals(object.get("id"), 2);
        Assertions.assertEquals(object.get("title"), "Przykład2");
        Assertions.assertEquals(object.get("username"), "user");
        Assertions.assertEquals(object.get("type"), "OTHER");
        Assertions.assertEquals(object.get("rating"), 8.27);
    }

    @Test
    public void successfulSaveWork() throws JSONException {
        Response result = RestAssured.get();
        result.then()
                .statusCode(200);

        JSONArray array = new JSONArray(result.body().asString());

        var request = RestAssured.given();
        JSONObject content = new JSONObject();
        content.put("title", "Test");
        content.put("type", "OTHER");
        content.put("text", "Testowy tekst");
        request.header("Content-Type", "application/json");
        request.header("Authorization", "Bearer " + token);
        request.body(content.toString());

        var response = request.post();
        response.then()
                .statusCode(200);

        Response result2 = RestAssured.get();
        result2.then()
                .statusCode(200);

        JSONArray array2 = new JSONArray(result2.body().asString());
        Assertions.assertEquals(array2.length(), array.length() + 1);
    }
}
