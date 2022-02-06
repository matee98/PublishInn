package com.github.PublishInn.integrationTests;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class WorkControllerTests {


    @BeforeAll
    static void setup(){
        RestAssured.baseURI = "http://localhost:8080/api/";
    }

    @Test
    public void successfulFindAll() {
        var res = RestAssured
                .get("/works");
        System.out.println(res.asString());
        System.out.println(res.getStatusCode());
    }
}
