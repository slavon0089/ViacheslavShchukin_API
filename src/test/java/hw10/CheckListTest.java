package hw10;

import static hw10.BoardApiTest.endpointBoard;
import static hw10.BoardApiTest.endpointBoardWithID;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.startsWith;

import org.apache.commons.lang3.RandomStringUtils;
import org.example.entities.BoardEntity;
import org.example.entities.CardEntity;
import org.example.entities.CheckListEntity;
import org.example.entities.ListEntity;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class CheckListTest extends AbstractApiTest{

    String endpoint = "https://api.trello.com/1/checklists";
    String endpointWithId = "https://api.trello.com/1/checklists/{id}";
    String endpointLists = "/lists";
    String endpointCards = "/cards";
    CardEntity card;
    ListEntity listEntity;
    CheckListEntity checkListEntity;
    String expectedListName;
    String expectedCardName;
    String expectedChecklistName;
    @BeforeClass
    public void createBoardWithCard(){
        expectedBoardName = RandomStringUtils.random(50, true, true);
        board = given()
            .spec(reqSpec)
            .when().queryParam("name", expectedBoardName).basePath(endpointBoard)
            .post()
            .then()
            .extract().body().as(BoardEntity.class);

        expectedListName = RandomStringUtils.random(10, true, true);
        listEntity = given()
            .spec(reqSpec)
            .when().queryParam("name", expectedListName).queryParam("idBoard", board.id()).basePath(endpointLists)
            .post()
            .then()
            //.spec(respSpec)
            .extract().body().as(ListEntity.class);

        expectedCardName = RandomStringUtils.random(15, true, true);
        card = given()
            .spec(reqSpec)
            .when()
            .queryParam("idList", listEntity.id())
            .queryParam("name", expectedCardName)
            .basePath(endpointCards)
            .post()
            .then()
            .body("name", equalTo(expectedCardName))
            .spec(respSpec)
            .extract().body().as(CardEntity.class);
    }

    @AfterClass
    public void deleteBoard() {

        given()
            .spec(reqSpec)
            .when().basePath(endpointBoardWithID)
            .pathParam("id", board.id())
            .delete()
            .then()
            .spec(respSpec);
    }

    @BeforeMethod
    public void createCheckList(){
        expectedChecklistName = RandomStringUtils.random(15, true, true);
        checkListEntity = given()
            .spec(reqSpec)
            .when()
            .queryParam("idCard", card.id())
            .queryParam("name", expectedChecklistName)
            .post(endpoint)
            .then()
            .log().all()
            .spec(respSpec)
            .extract().body().as(CheckListEntity.class);
    }

    @Test
    public void addNewCheckList() {
        expectedChecklistName = RandomStringUtils.random(15, true, true);
        checkListEntity = given()
            .spec(reqSpec)
            .when()
            .queryParam("idCard", card.id())
            .queryParam("name", expectedChecklistName)
            .post(endpoint)
            .then()
            .body("name", equalTo(expectedChecklistName) )
            .log().all()
            .spec(respSpec)
         .extract().body().as(CheckListEntity.class);
    }

    @Test
    public void deleteCheckList() {
        given()
            .spec(reqSpec)
            .when()
            .pathParam("id", checkListEntity.id())
            .delete(endpointWithId)
            .then()
            .log().all()
            .spec(respSpec);

        //assert 404
        given()
            .spec(reqSpec)
            .when().basePath(endpointWithId)
            .pathParam("id", card.id())
            .get()
            .then()
            .spec(negRespSpec)
            .log().all();
    }
}
