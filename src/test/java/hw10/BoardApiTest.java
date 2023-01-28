package hw10;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

import org.apache.commons.lang3.RandomStringUtils;
import org.example.entities.BoardEntity;
import org.example.utils.Endpoints;
import org.testng.annotations.Test;

public class BoardApiTest extends AbstractApiTest {

    @Test(priority = 1)
    protected void addNewBoard() {
        expectedBoardName = RandomStringUtils.random(50,true,true);
        board = given()
            .spec(baseRequestSpecification)
            .when()
            .queryParam("name", expectedBoardName)
            .basePath(Endpoints.BOARDS)
            .post()
            .then()
            .body("name",equalTo(expectedBoardName) )
            .spec(okResponse)
            .extract().body().as(BoardEntity.class);
    }

    @Test(priority = 2)
    protected void editBoard() {
        String descForBoard = RandomStringUtils.random(100,true,true);
        var boardNew = BoardEntity.builder().desc(descForBoard).id(board.id()).build();

        board = given()
            .spec(baseRequestSpecification)
            .when().basePath(Endpoints.BOARDS_ID)
            .pathParam("id", board.id())
            .body(boardNew)
            .put()
            .then()
            .body("desc",equalTo(descForBoard))
            .spec(okResponse)
            .extract().body().as(BoardEntity.class);
    }

    @Test(priority = 3)
    protected void getInfoAboutBoard() {

        board = given()
            .spec(baseRequestSpecification)
            .when().basePath(Endpoints.BOARDS_ID)
            .pathParam("id", board.id())
            .get()
            .then()
            .spec(okResponse)
            .body("name",equalTo(expectedBoardName) )
            .extract().body().as(BoardEntity.class);
    }

    @Test(priority = 4)
    protected void deleteBoard() {

        given()
            .spec(baseRequestSpecification)
            .when().basePath(Endpoints.BOARDS_ID)
            .pathParam("id", board.id())
            .delete()
            .then()
            .spec(okResponse);

        //assert 404
        given()
            .spec(baseRequestSpecification)
            .when().basePath(Endpoints.BOARDS_ID)
            .pathParam("id", board.id())
            .get()
            .then()
            .spec(notFoundResponse);

    }
}
