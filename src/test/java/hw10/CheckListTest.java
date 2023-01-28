package hw10;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

import org.apache.commons.lang3.RandomStringUtils;
import org.example.entities.CardEntity;
import org.example.entities.CheckListEntity;
import org.example.entities.ListEntity;
import org.example.utils.Endpoints;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class CheckListTest extends AbstractApiTest {
    CardEntity card;
    ListEntity listEntity;
    CheckListEntity checkListEntity;
    String expectedChecklistName;

    @BeforeClass
    protected void createBoardWithCard() {
        var boardName = RandomStringUtils.random(50, true, true);
        board = createBoard(boardName);

        var listName = RandomStringUtils.random(10, true, true);
        listEntity = createListOnTheBoard(board.id(), listName);

        var cardName = RandomStringUtils.random(15, true, true);
        card = createCardInTheList(listEntity.id(), cardName);
    }

    @AfterClass
    protected void deleteBoard() {
        deleteBoard(board.id());
    }

    @BeforeMethod
    protected void createCheckList() {
        expectedChecklistName = RandomStringUtils.random(15, true, true);
        checkListEntity = given()
            .spec(baseRequestSpecification)
            .when()
            .queryParam("idCard", card.id())
            .queryParam("name", expectedChecklistName)
            .post(Endpoints.CHECKLISTS)
            .then()
            .spec(okResponse)
            .extract().body().as(CheckListEntity.class);
    }

    @Test
    protected void addNewCheckList() {
        expectedChecklistName = RandomStringUtils.random(15, true, true);
        checkListEntity = given()
            .spec(baseRequestSpecification)
            .when()
            .queryParam("idCard", card.id())
            .queryParam("name", expectedChecklistName)
            .post(Endpoints.CHECKLISTS)
            .then()
            .body("name", equalTo(expectedChecklistName))
            .log().all()
            .spec(okResponse)
            .extract().body().as(CheckListEntity.class);
    }

    @Test
    protected void deleteCheckList() {
        given()
            .spec(baseRequestSpecification)
            .when()
            .pathParam("id", checkListEntity.id())
            .delete(Endpoints.CHECKLISTS_ID)
            .then()
            .spec(okResponse);

        //assert 404
        given()
            .spec(baseRequestSpecification)
            .when().basePath(Endpoints.CHECKLISTS_ID)
            .pathParam("id", card.id())
            .get()
            .then()
            .spec(notFoundResponse);
    }
}
