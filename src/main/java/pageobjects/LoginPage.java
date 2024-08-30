package pageobjects;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import io.qameta.allure.Step;

public class LoginPage {

    private final Page page;

    // Constructor
    public LoginPage(Page page) {
        this.page = page;
    }

    @Step("go to login page")
    public LoginPage goToLogin(){
        page.navigate("https://mc-99999.motherlink.io/login");
        return this;
    }

    // Page actions using the chaining pattern
    @Step("Fill email")
    public LoginPage fillEmail(String email) {
        page.getByLabel("Email").fill(email);
        return this;
    }

    @Step("Fill password")
    public LoginPage fillPassword(String password) {
        page.getByLabel("Password").fill(password);
        return this;
    }

    @Step("Click Sign in button")
    public LoginPage clickSignIn() {
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Sign in"))
                .click();
        return this;
    }
}
