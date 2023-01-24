package hw10;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.startsWith;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import org.example.entities.BoardEntity;

public class BoardApiTest extends AbstractApiTest {

    public String endpoint = "https://api.trello.com/1/boards";
    BoardEntity board;
    String endpointWithID = "https://api.trello.com/1/boards/{id}";

    @org.testng.annotations.Test(priority = 1)
    public void addNewBoard() {
        String boardName = "New API Board";
        reqSpec = new RequestSpecBuilder()
            .addQueryParam("key", apiKey)
            .addQueryParam("token", apiToken)
            .addQueryParam("name", boardName)
            .setContentType(ContentType.JSON)
            .build();

        board = given()
            .spec(reqSpec)
            .when()
            .post(endpoint)
            .then()
            .body("name", startsWith("New API Board"))
            .log().all()
            .spec(respSpec)
            .extract().body().as(BoardEntity.class);
        System.out.println(board.id());
    }

    @org.testng.annotations.Test(priority = 2)
    public void editBoard() {
        String newInfoForBoard = "{\"desc\":\"New description in the of board\"}";
        reqSpec = new RequestSpecBuilder()
            .addQueryParam("key", apiKey)
            .addQueryParam("token", apiToken)
            .addPathParams("id", board.id())
            .setBody(newInfoForBoard)
            .setContentType(ContentType.JSON)
            .build();

        board = given()
            .spec(reqSpec)
            .when()
            .put(endpointWithID)
            .then()
            .body("desc", startsWith("New description in the of board"))
            .log().all()
            .spec(respSpec)
            .extract().body().as(BoardEntity.class);
    }

    @org.testng.annotations.Test(priority = 3)
    public void getInfoAboutBoard() {
        reqSpec = new RequestSpecBuilder()
            .addQueryParam("key", apiKey)
            .addQueryParam("token", apiToken)
            .addPathParams("id", board.id())
            .setContentType(ContentType.JSON)
            .build();

        board = given()
            .spec(reqSpec)
            .when()
            .get(endpointWithID)
            .then()
            .spec(respSpec)
            .body("name", startsWith("New API Board"))
            .log().all()
            .extract().body().as(BoardEntity.class);
    }

    @org.testng.annotations.Test(priority = 4)
    public void deleteBoard() {
        reqSpec = new RequestSpecBuilder()
            .addQueryParam("key", apiKey)
            .addQueryParam("token", apiToken)
            .addPathParams("id", board.id())
            .setContentType(ContentType.JSON)
            .build();

        given()
            .spec(reqSpec)
            .when()
            .delete(endpointWithID)
            .then()
            .log().all()
            .spec(respSpec);
    }
}
