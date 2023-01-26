package hw10;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

import org.apache.commons.lang3.RandomStringUtils;
import org.example.entities.BoardEntity;
import org.testng.annotations.Test;

public class BoardApiTest extends AbstractApiTest {

    @Test(priority = 1)
    public void addNewBoard() {
        expectedBoardName = RandomStringUtils.random(50,true,true);
        board = given()
            .spec(reqSpec)
            .when()
            .queryParam("name", expectedBoardName)
            .basePath(endpointBoard)
            .post()
            .then()
            .body("name",equalTo(expectedBoardName) )
            .log().all()
            .spec(respSpec)
            .extract().body().as(BoardEntity.class);
        System.out.println(board.id());
    }

    @Test(priority = 2)
    public void editBoard() {
        String descForBoard = RandomStringUtils.random(100,true,true);
        String newInfoForBoard = "{\"desc\":\""+descForBoard+"\"}";

        board = given()
            .spec(reqSpec)
            .when().basePath(endpointBoardWithID)
            .pathParam("id", board.id())
            .body(newInfoForBoard)
            .put()
            .then()
            .body("desc",equalTo(descForBoard))
            .log().all()
            .spec(respSpec)
            .extract().body().as(BoardEntity.class);
    }

    @Test(priority = 3)
    public void getInfoAboutBoard() {

        board = given()
            .spec(reqSpec)
            .when().basePath(endpointBoardWithID)
            .pathParam("id", board.id())
            .get()
            .then()
            .spec(respSpec)
            .body("name",equalTo(expectedBoardName) )
            .log().all()
            .extract().body().as(BoardEntity.class);
    }

    @Test(priority = 4)
    public void deleteBoard() {

        given()
            .spec(reqSpec)
            .when().basePath(endpointBoardWithID)
            .pathParam("id", board.id())
            .delete()
            .then()
            .log().all()
            .spec(respSpec);

        //assert 404
        given()
            .spec(reqSpec)
            .when().basePath(endpointBoardWithID)
            .pathParam("id", board.id())
            .get()
            .then()
            .spec(negRespSpec)
            .log().all();

    }
}
