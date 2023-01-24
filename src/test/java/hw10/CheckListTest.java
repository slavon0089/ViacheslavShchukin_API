package hw10;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.startsWith;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import org.example.entities.CheckListEntity;

public class CheckListTest extends AbstractApiTest{

    public String idCard = "63cbda41366ce301f4cabbbe";
    String endpoint = "https://api.trello.com/1/checklists";
    String endpointWithId = "https://api.trello.com/1/checklists/{id}";
    CheckListEntity checkList;

    @org.testng.annotations.Test(priority = 1)
    public void addNewCheckList() {
        //https://api.trello.com/1/checklists?idCard=&key=APIKey&token=APIToken
        String checkListName = "New API check-list";
        reqSpec = new RequestSpecBuilder()
            .addQueryParam("key", apiKey)
            .addQueryParam("token", apiToken)
            .addQueryParam("idCard", idCard)
            .addQueryParam("name", checkListName)
            .setContentType(ContentType.JSON)
            .build();

        checkList = given()
            .spec(reqSpec)
            .when()
            .post(endpoint)
            .then()
            .body("name", startsWith("New API check-list"))
            .log().all()
            .spec(respSpec)
            .extract().body().as(CheckListEntity.class);;
    }

    @org.testng.annotations.Test(priority = 2)
    public void deleteCheckList() {
        reqSpec = new RequestSpecBuilder()
            .addQueryParam("key", apiKey)
            .addQueryParam("token", apiToken)
            .addPathParams("id", checkList.id())
            .setContentType(ContentType.JSON)
            .build();

        given()
            .spec(reqSpec)
            .when()
            .delete(endpointWithId)
            .then()
            .log().all()
            .spec(respSpec);
    }
}
