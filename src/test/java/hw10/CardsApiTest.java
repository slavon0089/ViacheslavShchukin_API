package hw10;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.startsWith;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import org.example.entities.CardEntity;

public class CardsApiTest extends AbstractApiTest {

    String idList = "63cafbd76a8b8701f53a09a3";
    String endpoint = "https://api.trello.com/1/cards";
    String endpointWithId = "https://api.trello.com/1/cards/{id}";
    CardEntity card;

    @org.testng.annotations.Test
    public void addNewCard() {
        String CardName = "New API Card";

        card = given()
            .spec(reqSpec)
            .when()
            .queryParam("idList", idList)
            .queryParam("name", CardName)
            .post(endpoint)
            .then()
            .body("name", startsWith("New API Card"))
            .log().all()
            .spec(respSpec)
            .extract().body().as(CardEntity.class);
    }

    @org.testng.annotations.Test
    public void editCard() {
        addNewCard();
        String newCardName = "{\"name\":\"New name for card\"}";

        card = given()
            .spec(reqSpec)
            .when()
            .pathParam("id", card.id())
            .body(newCardName)
            .put(endpointWithId)
            .then()
            .body("name", startsWith("New name for card"))
            .log().all()
            .spec(respSpec)
            .extract().body().as(CardEntity.class);
    }

    @org.testng.annotations.Test()
    public void getInfoAboutCard() {
        addNewCard();

        card = given()
            .spec(reqSpec)
            .when()
            .pathParam("id", card.id())
            .get(endpointWithId)
            .then()
            .spec(respSpec)
            .body("name", startsWith("New API Card"))
            .log().all()
            .extract().body().as(CardEntity.class);
    }

    @org.testng.annotations.Test()
    public void deleteCard() {
        addNewCard();

        given()
            .spec(reqSpec)
            .when().pathParam("id", card.id())
            .delete(endpointWithId)
            .then()
            .log().all()
            .spec(respSpec);
    }
}
