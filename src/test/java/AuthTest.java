import com.codeborne.selenide.Configuration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.chrome.ChromeOptions;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;

public class AuthTest {

    @BeforeEach
    void setUp() {

        open("http://localhost:9999");
    }

    @Test
    void shouldLoginWithActiveUser() {
        AuthInfo activeUser = DataHelper.getActiveUser();

        $("[data-test-id=login] input").setValue(activeUser.getLogin());
        $("[data-test-id=password] input").setValue(activeUser.getPassword());
        $("[data-test-id=action-login]").click();

        $("h2").shouldHave(text("Личный кабинет")).shouldBe(visible);
    }

    @Test
    void shouldNotLoginWithBlockedUser() {
        AuthInfo blockedUser = DataHelper.getBlockedUser();

        $("[data-test-id=login] input").setValue(blockedUser.getLogin());
        $("[data-test-id=password] input").setValue(blockedUser.getPassword());
        $("[data-test-id=action-login]").click();

        $("[data-test-id=error-notification]")
                .shouldHave(text("Ошибка"))
                .shouldHave(text("Пользователь заблокирован"));
    }

    @Test
    void shouldNotLoginWithNonExistentUser() {
        AuthInfo nonExistentUser = DataHelper.getNonExistentUser();

        $("[data-test-id=login] input").setValue(nonExistentUser.getLogin());
        $("[data-test-id=password] input").setValue(nonExistentUser.getPassword());
        $("[data-test-id=action-login]").click();

        $("[data-test-id=error-notification]")
                .shouldHave(text("Ошибка"))
                .shouldHave(text("Неверно указан логин или пароль"));
    }

    @Test
    void shouldNotLoginWithInvalidLogin() {
        AuthInfo activeUser = DataHelper.getActiveUser();
        AuthInfo userWithInvalidLogin = DataHelper.getUserWithInvalidLogin(activeUser);

        $("[data-test-id=login] input").setValue(userWithInvalidLogin.getLogin());
        $("[data-test-id=password] input").setValue(userWithInvalidLogin.getPassword());
        $("[data-test-id=action-login]").click();

        $("[data-test-id=error-notification]")
                .shouldHave(text("Ошибка"))
                .shouldHave(text("Неверно указан логин или пароль"));
    }

    @Test
    void shouldNotLoginWithInvalidPassword() {
        AuthInfo activeUser = DataHelper.getActiveUser();
        AuthInfo userWithInvalidPassword = DataHelper.getUserWithInvalidPassword(activeUser);

        $("[data-test-id=login] input").setValue(userWithInvalidPassword.getLogin());
        $("[data-test-id=password] input").setValue(userWithInvalidPassword.getPassword());
        $("[data-test-id=action-login]").click();

        $("[data-test-id=error-notification]")
                .shouldHave(text("Ошибка"))
                .shouldHave(text("Неверно указан логин или пароль"));
    }

    @Test
    void shouldNotLoginWithEmptyLogin() {
        AuthInfo activeUser = DataHelper.getActiveUser();

        $("[data-test-id=password] input").setValue(activeUser.getPassword());
        $("[data-test-id=action-login]").click();

        $("[data-test-id=login] .input__sub")
                .shouldHave(text("Поле обязательно для заполнения"));
    }

    @Test
    void shouldNotLoginWithEmptyPassword() {
        AuthInfo activeUser = DataHelper.getActiveUser();

        $("[data-test-id=login] input").setValue(activeUser.getLogin());
        $("[data-test-id=action-login]").click();

        $("[data-test-id=password] .input__sub")
                .shouldHave(text("Поле обязательно для заполнения"));
    }
}