package hw10;

import static org.example.utils.Config.getApiKeyFromProperties;
import static org.example.utils.Config.getApiTokenFromProperties;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import java.io.IOException;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;

public class AbstractApiTest {

    static final String apiKey = getApiKeyFromProperties();;
    static final String apiToken = getApiTokenFromProperties();;
    static final String baseUri = "https://api.trello.com/1";

    static RequestSpecification reqSpec;
    static ResponseSpecification respSpec;
    static ResponseSpecification negRespSpec;

    @BeforeClass
    public static void setup() {
        reqSpec = new RequestSpecBuilder().addQueryParam("key", apiKey)
                                          .addQueryParam("token", apiToken)
                                          .setContentType(ContentType.JSON)
                                          .setBaseUri(baseUri)
                                          .build();
        respSpec = new ResponseSpecBuilder().expectStatusCode(200)
                                            .expectContentType(ContentType.JSON)
                                            .build();
        negRespSpec = new ResponseSpecBuilder().expectStatusCode(404)
                                               //.expectContentType(ContentType.JSON)
                                               .build();

    }

    @AfterClass
    public void close(){
        reqSpec = null;
        respSpec = null;
    }
}
