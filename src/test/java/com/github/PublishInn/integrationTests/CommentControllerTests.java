package com.github.PublishInn.integrationTests;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class CommentControllerTests {
    private static String userToken;
    private static String modToken;

    @BeforeAll
    static void setup() {
        RestAssured.baseURI = "http://localhost:8080/api/comments/";
        userToken = AuthenticationHelpers.getToken("user", "zaq1@WSX");
        modToken = AuthenticationHelpers.getToken("moderator", "zaq1@WSX");
    }

    @Test
    void successfulFindAll() throws JSONException {
        Response result = RestAssured.get();
        result.then()
                .statusCode(200);

        JSONArray array = new JSONArray(result.body().asString());
        JSONObject object = new JSONObject(array.get(0).toString());

        Assertions.assertEquals(object.get("id"), 1);
        Assertions.assertEquals(object.get("username"), "user");
        Assertions.assertEquals(object.get("workId"), 1);
        Assertions.assertEquals(object.get("text"), "Bardzo fajne. Polecam");
        Assertions.assertEquals(object.get("visible"), true);
    }

    @Test
    void successfulFindById() throws JSONException {
        Response result = RestAssured.get("/find/2");
        result.then()
                .statusCode(200);
        JSONObject obj = new JSONObject(result.body().asString());

        Assertions.assertEquals(obj.get("id"), 2);
        Assertions.assertEquals(obj.get("username"), "matz98");
        Assertions.assertEquals(obj.get("workId"), 1);
        Assertions.assertEquals(obj.get("text"), "Całkiem w porządku");
        Assertions.assertEquals(obj.get("visible"), true);
    }

    @Test
    void successfulAddEditAndDelete() throws JSONException {
        Response result = RestAssured.get();
        result.then()
                .statusCode(200);

        JSONArray array = new JSONArray(result.body().asString());

        //dodanie nowego komentarza
        var request = RestAssured.given();
        JSONObject content = new JSONObject();
        content.put("workId", 1);
        content.put("text", "Testowy tekst");
        request.header("Content-Type", "application/json");
        request.header("Authorization", "Bearer " + userToken);
        request.body(content.toString());

        var response = request.post();
        response.then()
                .statusCode(201);

        Response result2 = RestAssured.get();
        result2.then()
                .statusCode(200);

        JSONArray array2 = new JSONArray(result2.body().asString());
        Assertions.assertEquals(array2.length(), array.length() + 1);

        //edycja
        content.put("text", "Zmieniony tekst");
        content.put("commentId", 3);
        content.remove("workId");
        request.body(content.toString());
        var editResponse = request.put();

        editResponse.then().statusCode(200);

        Response result3 = RestAssured.get("/find/3");
        result3.then()
                .statusCode(200);
        JSONObject obj = new JSONObject(result3.body().asString());

        Assertions.assertEquals(obj.get("id"), 3);
        Assertions.assertEquals(obj.get("username"), "user");
        Assertions.assertEquals(obj.get("workId"), 1);
        Assertions.assertEquals(obj.get("text"), "Zmieniony tekst");
        Assertions.assertEquals(obj.get("visible"), true);

        //usunięcie
        Response deleteResponse = request.delete("/delete/3");
        deleteResponse.then().statusCode(204);
        Response result4 = RestAssured.get("/find/3");
        result4.then()
                .statusCode(404);
    }
}
