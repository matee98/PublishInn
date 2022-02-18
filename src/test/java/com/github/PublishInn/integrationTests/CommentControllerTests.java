package com.github.PublishInn.integrationTests;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class CommentControllerTests {
    private static String userToken;

    @BeforeAll
    static void setup() {
        RestAssured.baseURI = "http://localhost:8080/api/comments/";
        userToken = AuthenticationHelpers.getToken("user", "zaq1@WSX");
    }

    @Test
    void successfulFindAll() throws JSONException {
        Response result = RestAssured.get();
        result.then()
                .statusCode(200);

        JSONArray array = new JSONArray(result.body().asString());
        JSONObject object = new JSONObject(array.get(0).toString());

        Assertions.assertEquals(1, object.get("id"));
        Assertions.assertEquals("user", object.get("username"));
        Assertions.assertEquals(1, object.get("workId"));
        Assertions.assertEquals("Bardzo fajne. Polecam", object.get("text"));
        Assertions.assertEquals(true, object.get("visible"));
    }

    @Test
    void successfulFindById() throws JSONException {
        Response result = RestAssured.get("/find/2");
        result.then()
                .statusCode(200);
        JSONObject obj = new JSONObject(result.body().asString());

        Assertions.assertEquals(2, obj.get("id"));
        Assertions.assertEquals("matz98", obj.get("username"));
        Assertions.assertEquals(1, obj.get("workId"));
        Assertions.assertEquals("Całkiem w porządku", obj.get("text"));
        Assertions.assertEquals(true, obj.get("visible"));
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

        Assertions.assertEquals(3, obj.get("id"));
        Assertions.assertEquals("user", obj.get("username"));
        Assertions.assertEquals(1, obj.get("workId"));
        Assertions.assertEquals("Zmieniony tekst", obj.get("text"));
        Assertions.assertEquals(true, obj.get("visible"));

        //usunięcie
        Response deleteResponse = request.delete("/delete/3");
        deleteResponse.then().statusCode(204);
        Response result4 = RestAssured.get("/find/3");
        result4.then()
                .statusCode(404);
    }
}
