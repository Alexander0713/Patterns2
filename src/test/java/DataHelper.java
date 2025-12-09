import com.github.javafaker.Faker;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import lombok.experimental.UtilityClass;

import java.util.Locale;

import static io.restassured.RestAssured.given;

@UtilityClass
public class DataHelper {

    private static final RequestSpecification requestSpec = new RequestSpecBuilder()
            .setBaseUri("http://localhost")
            .setPort(9999)
            .setAccept(ContentType.JSON)
            .setContentType(ContentType.JSON)
            .log(LogDetail.ALL)
            .build();

    private static final Faker faker = new Faker(new Locale("en"));

    public static AuthInfo getActiveUser() {
        return createUser("active");
    }

    public static AuthInfo getBlockedUser() {
        return createUser("blocked");
    }

    public static AuthInfo getNonExistentUser() {
        return new AuthInfo(faker.name().username(), faker.internet().password());
    }

    public static AuthInfo getUserWithInvalidLogin(AuthInfo validUser) {
        return new AuthInfo(faker.name().username(), validUser.getPassword());
    }

    public static AuthInfo getUserWithInvalidPassword(AuthInfo validUser) {
        return new AuthInfo(validUser.getLogin(), faker.internet().password());
    }

    private static AuthInfo createUser(String status) {
        String login = faker.name().username();
        String password = faker.internet().password();
        RegistrationDto user = new RegistrationDto(login, password, status);
        sendCreateUserRequest(user);
        return new AuthInfo(login, password);
    }

    private static void sendCreateUserRequest(RegistrationDto user) {
        given()
                .spec(requestSpec)
                .body(user)
                .when()
                .post("/api/system/users")
                .then()
                .statusCode(200);
    }
}