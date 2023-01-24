package hw10;

import static org.example.utils.Config.getUserNameFromProperties;
import static org.example.utils.Config.getUserPasswordFromProperties;

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
            apiKey = getUserNameFromProperties();
            apiToken =getUserPasswordFromProperties();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    static RequestSpecification reqSpec;
    static ResponseSpecification respSpec;

    @BeforeClass
    public static void setup() {

        respSpec = new ResponseSpecBuilder().expectStatusCode(200).expectContentType(ContentType.JSON).build();
    }

    @AfterClass
    public void close(){
        reqSpec = null;
        respSpec = null;
    }
}
