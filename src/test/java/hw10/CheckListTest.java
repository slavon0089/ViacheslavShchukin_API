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

    @org.testng.annotations.Test
    public void addNewCheckList() {
        String checkListName = "New API check-list";

        checkList = given()
            .spec(reqSpec)
            .when()
            .queryParam("idCard", idCard)
            .queryParam("name", checkListName)
            .post(endpoint)
            .then()
            .body("name", startsWith("New API check-list"))
            .log().all()
            .spec(respSpec)
            .extract().body().as(CheckListEntity.class);;
    }

    @org.testng.annotations.Test
    public void deleteCheckList() {
        addNewCheckList();

        given()
            .spec(reqSpec)
            .when()
            .pathParam("id", checkList.id())
            .delete(endpointWithId)
            .then()
            .log().all()
            .spec(respSpec);
    }
}
