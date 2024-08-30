package pageobjects;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import io.qameta.allure.Step;


public class BillingEntitiesPage {
    private final Page page;

    // Constructor
    public BillingEntitiesPage(Page page) {
        this.page = page;
    }

    @Step("Click on tab Billing entities")
    public BillingEntitiesPage clickCreateEntities() {
        page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName(" Billing entities")).click();
        return this;
    }

    @Step("Navigate to  New entity")
    public BillingEntitiesPage createNewEntity() {
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName(" New entity")).click();
        return this;
    }

    @Step("Fill company name")
    public BillingEntitiesPage fillCompanyName(String company) {
        page.getByLabel("Company name").fill(company);
        return this;
    }

    @Step("Fill registration code")
    public BillingEntitiesPage fillRegistrationCode(String registration) {
        page.getByLabel("Registration code").fill(registration);
        return this;
    }

    @Step("Switch selector No VAT")
    public BillingEntitiesPage clickSelector() {
        page.locator("div:nth-child(5) > .inline-flex > .relative > .w-9").click();
        return this;
    }

    @Step("Click on Next button")
    public BillingEntitiesPage navigationInEntityNext() {
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Next").setExact(true)).click();
        return this;
    }

    @Step("Search country on list")
    public BillingEntitiesPage searchCountry(String country) {
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Select ")).click();
        page.getByTitle("Add Billing Modal").getByPlaceholder("Search...").fill(country);
        page.getByRole(AriaRole.MENUITEM, new Page.GetByRoleOptions().setName(country)).click();
        return this;
    }

    @Step("fill address,city,state,zip")
    public BillingEntitiesPage fillAddressAndCity(String address, String city, String state, String zip) {
        page.getByLabel("Address").fill(address);
        page.getByLabel("City").fill(city);
        page.getByLabel("State/Province").fill(state);
        page.getByLabel("Zip/Postal code").fill(zip);
        return this;
    }

    @Step("Click on done button")
    public BillingEntitiesPage doneCreating() {
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Done")).click();
        return this;
    }

    @Step("Delete entity by company and registration code")
    public void deleteCreatedInTestEntity(String company, String registration) {
        page.getByRole(AriaRole.ROW, new Page.GetByRoleOptions().setName(company + " " + registration)).getByRole(AriaRole.BUTTON).click();
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName(" Delete")).click();
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Yes, delete")).click();
    }
}
