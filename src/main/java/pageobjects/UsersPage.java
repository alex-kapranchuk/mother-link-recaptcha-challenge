package pageobjects;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import io.qameta.allure.Step;

public class UsersPage {
    private final Page page;

    // Constructor
    public UsersPage(Page page) {
        this.page = page;
    }

    // Page actions using the chaining pattern
    @Step("Go to entities with reload and click")
    public BillingEntitiesPage navigateToBillingEntities() {
        page.navigate("https://mc-99999.motherlink.io/en/users");
        page.reload();
        page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName(" Billing entities")).click();
        return new BillingEntitiesPage(page);
    }
}
