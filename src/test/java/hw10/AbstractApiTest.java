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

    static final String apiKey;
    static final String apiToken;
    static {
        try {
            apiKey = getApiKeyFromProperties();
            apiToken = getApiTokenFromProperties();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    static RequestSpecification reqSpec;
    static ResponseSpecification respSpec;

    @BeforeClass
    public static void setup() {
        reqSpec = new RequestSpecBuilder().addQueryParam("key", apiKey)
                                          .addQueryParam("token", apiToken)
                                          .setContentType(ContentType.JSON)
                                          .build();
        respSpec = new ResponseSpecBuilder().expectStatusCode(200)
                                            .expectContentType(ContentType.JSON)
                                            .build();

    }

    @AfterClass
    public void close(){
        reqSpec = null;
        respSpec = null;
    }
}
