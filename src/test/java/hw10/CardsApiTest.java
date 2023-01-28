package hw10;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.startsWith;

import org.apache.commons.lang3.RandomStringUtils;
import org.example.entities.BoardEntity;
import org.example.entities.CardEntity;
import org.example.entities.ListEntity;
import org.example.utils.Endpoints;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class CardsApiTest extends AbstractApiTest {

    CardEntity card;
    ListEntity listEntity;
    String expectedListName;
    String expectedCardName;

    @BeforeClass
    protected void createBoardWithList() {
        expectedBoardName = RandomStringUtils.random(50, true, true);
        expectedListName = RandomStringUtils.random(10, true, true);
        var boardCreate = BoardEntity.builder().name(expectedBoardName).build();
        board = createBoard(expectedBoardName);
        listEntity = createListOnTheBoard(board.id(), expectedListName);
    }

    @AfterClass
    protected void deleteBoard() {
        deleteBoard(board.id());
    }

    @Test
    protected void addNewCard() {
        expectedCardName = RandomStringUtils.random(15, true, true);
        card = given()
            .spec(baseRequestSpecification)
            .when()
            .queryParam("idList", listEntity.id())
            .queryParam("name", expectedCardName)
            .basePath(Endpoints.CARDS)
            .post()
            .then()
            .body("name", equalTo(expectedCardName))
            .spec(okResponse)
            .extract().body().as(CardEntity.class);
    }

    @BeforeMethod
    protected void createCard() {
        expectedCardName = RandomStringUtils.random(15, true, true);
        card = given()
            .spec(baseRequestSpecification)
            .when()
            .queryParam("idList", listEntity.id())
            .queryParam("name", expectedCardName)
            .basePath(Endpoints.CARDS)
            .post()
            .then()
            .body("name", equalTo(expectedCardName))
            .log().all()
            .spec(okResponse)
            .extract().body().as(CardEntity.class);
    }

    @Test
    protected void editCard() {

        expectedCardName = RandomStringUtils.random(15, true, true);
        var cardNew = CardEntity.builder().name(expectedCardName).id(card.id()).build();

        card = given()
            .spec(baseRequestSpecification)
            .when().basePath(Endpoints.CARDS_ID)
            .pathParam("id", card.id())
            .body(cardNew)
            .put()
            .then()
            .body("name", equalTo(expectedCardName))
            .log().all()
            .spec(okResponse)
            .extract().body().as(CardEntity.class);
    }

    @Test
    protected void getInfoAboutCard() {
        card = given()
            .spec(baseRequestSpecification)
            .when().basePath(Endpoints.CARDS_ID)
            .pathParam("id", card.id())
            .get()
            .then()
            .spec(okResponse)
            .body("name", startsWith(expectedCardName))
            .extract().body().as(CardEntity.class);
    }

    @Test
    protected void deleteCard() {
        given()
            .spec(baseRequestSpecification)
            .when().pathParam("id", card.id())
            .delete(Endpoints.CARDS_ID)
            .then()
            .spec(okResponse);

        //assert 404
        given()
            .spec(baseRequestSpecification)
            .when().basePath(Endpoints.CARDS_ID)
            .pathParam("id", card.id())
            .get()
            .then()
            .spec(notFoundResponse);
    }
}
