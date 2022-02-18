package com.github.PublishInn.integrationTests;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class WorkControllerTests {

    private static String token;

    @BeforeAll
    static void setup() {
        RestAssured.baseURI = "http://localhost:8080/api/works/";
        token = AuthenticationHelpers.getToken("moderator", "zaq1@WSX");
    }

    @Test
    void successfulFindAll() throws JSONException {
        Response result = RestAssured.get();
        result.then()
                .statusCode(200);

        JSONArray array = new JSONArray(result.body().asString());
        JSONObject object = new JSONObject(array.get(0).toString());

        Assertions.assertEquals(1, object.get("id"));
        Assertions.assertEquals("Przykład1", object.get("title"));
        Assertions.assertEquals("user", object.get("username"));
        Assertions.assertEquals("FANTASY", object.get("type"));
    }

    @Test
    void successfulGetById() throws JSONException {
        Response result = RestAssured.get("details/2");
        result.then()
                .assertThat()
                .statusCode(200);
        var obj = new JSONObject(result.body().asString());

        Assertions.assertEquals("user", obj.get("username"));
        Assertions.assertEquals(8.27, obj.get("rating"));
        Assertions.assertEquals("OTHER", obj.get("type"));
        Assertions.assertEquals("Przykład2", obj.get("title"));
        Assertions.assertEquals("ACCEPTED", obj.get("status"));
    }

    @Test
    void unsuccessfulGetById() {
        Response result = RestAssured.get("details/21");
        result.then()
                .assertThat()
                .statusCode(404);
    }

    @Test
    void successfulGetWorksByUsername() throws JSONException {
        Response result = RestAssured.get("user/user");
        result.then()
                .assertThat()
                .statusCode(200);
        JSONArray array = new JSONArray(result.body().asString());
        JSONObject object = new JSONObject(array.get(1).toString());

        Assertions.assertEquals(2, array.length());
        Assertions.assertEquals(2, object.get("id"));
        Assertions.assertEquals("Przykład2", object.get("title"));
        Assertions.assertEquals("user", object.get("username"));
        Assertions.assertEquals("OTHER", object.get("type"));
        Assertions.assertEquals(8.27, object.get("rating"));
    }

    @Test
    void successfulSaveWork() throws JSONException {
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
