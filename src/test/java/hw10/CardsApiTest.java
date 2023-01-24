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

    @org.testng.annotations.Test(priority = 1)
    public void addNewCard() {
        String CardName = "New API Card";
        reqSpec = new RequestSpecBuilder()
            .addQueryParam("key", apiKey)
            .addQueryParam("token", apiToken)
            .addQueryParam("idList", idList)
            .addQueryParam("name", CardName)
            .setContentType(ContentType.JSON)
            .build();

        card = given()
            .spec(reqSpec)
            .when()
            .post(endpoint)
            .then()
            .body("name", startsWith("New API Card"))
            .log().all()
            .spec(respSpec)
            .extract().body().as(CardEntity.class);
    }

    @org.testng.annotations.Test(priority = 2)
    public void editCard() {
        String newCardName = "{\"name\":\"New name for card\"}";
        reqSpec = new RequestSpecBuilder()
            .addQueryParam("key", apiKey)
            .addQueryParam("token", apiToken)
            .addPathParams("id", card.id())
            .setBody(newCardName)
            .setContentType(ContentType.JSON)
            .build();

        card = given()
            .spec(reqSpec)
            .when()
            .put(endpointWithId)
            .then()
            .body("name", startsWith("New name for card"))
            .log().all()
            .spec(respSpec)
            .extract().body().as(CardEntity.class);
    }

    @org.testng.annotations.Test(priority = 3)
    public void getInfoAboutCard() {
        reqSpec = new RequestSpecBuilder()
            .addQueryParam("key", apiKey)
            .addQueryParam("token", apiToken)
            .addPathParams("id", card.id())
            .setContentType(ContentType.JSON)
            .build();

        card = given()
            .spec(reqSpec)
            .when()
            .get(endpointWithId)
            .then()
            .spec(respSpec)
            .body("name", startsWith("New name for card"))
            .log().all()
            .extract().body().as(CardEntity.class);
    }

    @org.testng.annotations.Test(priority = 4)
    public void deleteCard() {
        reqSpec = new RequestSpecBuilder()
            .addQueryParam("key", apiKey)
            .addQueryParam("token", apiToken)
            .addPathParams("id", card.id())
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
