package pageobjects;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import io.qameta.allure.Step;

public class AccountPage {
    private final Page page;

    // Constructor
    public AccountPage(Page page) {
        this.page = page;
    }

    @Step("Go to Account url")
    public AccountPage goToAccount() {
        page.navigate("https://mc-99999.motherlink.io/en/account");
        return this;
    }

    @Step("Click on Logo")
    public AccountPage clickToLogo() {
        page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("Logo")).click();
        return this;
    }
}
