package com.restspringtest.Integration.Tests;

import static io.restassured.RestAssured.*;
import static org.junit.Assert.*;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.restspringtest.Configurations.TestConfiguration;
import com.restspringtest.Integration.TestContainers.AbstractIntegrationTest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class SwaggerIntegrationTest extends AbstractIntegrationTest {

    @Test
    void testShouldDisplaySwaggerUiPage() {
        var content = given()
            .basePath("/swagger-ui/index.html")
            .port(TestConfiguration.SERVER_PORT)
            .when()
                .get()
            .then()
                .statusCode(200)
            .extract()
                .body()
                    .asString();
        assertTrue(content.contains("Swagger UI"));
    }
    
}
