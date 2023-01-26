package hw10;

import static hw10.BoardApiTest.endpointBoard;
import static hw10.BoardApiTest.endpointBoardWithID;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.startsWith;

import org.apache.commons.lang3.RandomStringUtils;
import org.example.entities.BoardEntity;
import org.example.entities.CardEntity;
import org.example.entities.ListEntity;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class CardsApiTest extends AbstractApiTest {

    String endpointCardWithId = "/cards/{id}";
    String endpointLists = "/lists";
    String endpointCards = "/cards";
    CardEntity card;
    BoardEntity board;
    ListEntity listEntity;
    String expectedBoardName;
    String expectedListName;
    String expectedCardName;

    @BeforeClass
    public void createBoardWithList() {
        expectedBoardName = RandomStringUtils.random(50, true, true);
        board = given()
            .spec(reqSpec)
            .when().queryParam("name", expectedBoardName).basePath(endpointBoard)
            .post()
            .then()
            .extract().body().as(BoardEntity.class);
        System.out.println(board.id());

        expectedListName = RandomStringUtils.random(10, true, true);
        listEntity = given()
            .spec(reqSpec)
            .when().queryParam("name", expectedListName).queryParam("idBoard", board.id()).basePath(endpointLists)
            .post()
            .then()
            .log().all()
            //.spec(respSpec)
            .extract().body().as(ListEntity.class);
    }

    @AfterClass
    public void deleteBoard() {

        given()
            .spec(reqSpec)
            .when().basePath(endpointBoardWithID)
            .pathParam("id", board.id())
            .delete()
            .then()
            .log().all()
            .spec(respSpec);
    }

    @Test
    public void addNewCard() {
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
            .log().all()
            .spec(respSpec)
            .extract().body().as(CardEntity.class);
    }

    @BeforeMethod
    public void createCard(){
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
            .log().all()
            .spec(respSpec)
            .extract().body().as(CardEntity.class);
    }
    @Test
    public void editCard() {

        expectedCardName = RandomStringUtils.random(15, true, true);
        String newCardName = "{\"name\":\"" + expectedCardName + "\"}";

        card = given()
            .spec(reqSpec)
            .when().basePath(endpointCardWithId)
            .pathParam("id", card.id())
            .body(newCardName)
            .put()
            .then()
            .body("name", equalTo(expectedCardName) )
            .log().all()
            .spec(respSpec)
            .extract().body().as(CardEntity.class);
    }

    @Test
    public void getInfoAboutCard() {
        addNewCard();

        card = given()
            .spec(reqSpec)
            .when().basePath(endpointCardWithId)
            .pathParam("id", card.id())
            .get()
            .then()
            .spec(respSpec)
            .body("name", startsWith(expectedCardName))
            .log().all()
            .extract().body().as(CardEntity.class);
    }

    @Test
    public void deleteCard() {
        addNewCard();

        given()
            .spec(reqSpec)
            .when().pathParam("id", card.id())
            .delete(endpointCardWithId)
            .then()
            .log().all()
            .spec(respSpec);
    }
}
