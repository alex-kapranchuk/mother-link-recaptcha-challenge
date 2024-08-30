import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.options.AriaRole;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import models.BillingEntity;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.instancio.Instancio;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pageobjects.AccountPage;
import pageobjects.BillingEntitiesPage;
import pageobjects.LoginPage;
import pageobjects.UsersPage;

import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
import static utils.ReCaptchaSolveWaiting.waitForReCaptchaResponse;

import static utils.ReaderResources.getExtensionPath;

@Epic("Epic Name: Login")
@Feature("Feature Name: Login throw e2e")
public class RegisterEntityE2ETest {

    BillingEntity billingEntity = new BillingEntity();
    private static final Logger logger = LogManager.getLogger(RegisterEntityE2ETest.class);
    private static final String EXTENSION_PATH = getExtensionPath();

    // Generate model - Note: Generate only for Company name - in real case better generate for all fields
    final String COMPANY_NAME = Instancio.create(BillingEntity.getCompanyModel()) + ".ltd";

    @Test
    @Feature("Login on prod with recaptcha")
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("Test: Login on prod and create entity")
    @Description("Test description: Login on prod and create entity E2E")
    public void loginForCreateEntityE2ETest() {
        if (EXTENSION_PATH.isEmpty()) {
            // Handle the case when EXTENSION_PATH is not set properly
            return;  // Throw or exception/handle in utils methods
        }
        try (Playwright playwright = Playwright.create()) {
            logger.info("Launch the browser with the extension");
            BrowserContext context = playwright.chromium().launchPersistentContext(
                    Paths.get("user-data-dir"),
                    new BrowserType.LaunchPersistentContextOptions()
                            .setHeadless(false)  // Launch in windowed mode
                            .setArgs(Arrays.asList(
                                    "--disable-extensions-except=" + EXTENSION_PATH,  // Disable all extensions except the specified one
                                    "--load-extension=" + EXTENSION_PATH  // Load extension
                            ))
            );

            // Get the first page in the context and increase wait for elements - this don`t have impact for time of test execution
            Page page = context.pages().get(0);
            page.setDefaultTimeout(90000);

            // Use the page objects
            LoginPage loginPage = new LoginPage(page);

            //Use the chaining pattern
            loginPage
                    .goToLogin()
                    .fillEmail("likeplum@starmail.net")
                    .fillPassword("111111");

            //Wait for the specific API request with status 200 or timeout after 10 seconds
            waitForReCaptchaResponse(page, "recaptcha/api2/userverify", 200, 10000); // Code work without this method, decide keep it for message about successful solve captcha
            logger.info("Solving captcha");

            loginPage
                    .clickSignIn();

            //Check successful login
            page.waitForURL("https://mc-99999.motherlink.io/en/account");
            Assertions.assertEquals("https://mc-99999.motherlink.io/en/account", page.url());
            logger.info("Login is successful");

            /* Register Entity flow Note:
              2 flow for run test separate and for increase stability,
              economy time resource and memory that equals money **/

            // Use the page objects
            AccountPage accountPage = new AccountPage(page);
            UsersPage usersPage = new UsersPage(page);
            BillingEntitiesPage billingEntitiesPage = new BillingEntitiesPage(page);

            //Use the chaining pattern
            accountPage
                    .goToAccount()
                    .clickToLogo();

            //Use the chaining pattern
            usersPage
                    .navigateToBillingEntities();

            //Use the chaining pattern
            billingEntitiesPage
                    .clickCreateEntities()
                    .createNewEntity()
                    .fillCompanyName(COMPANY_NAME)
                    .fillRegistrationCode(billingEntity.getRegistrationCode())
                    .clickSelector()
                    .navigationInEntityNext()
                    .searchCountry(billingEntity.getCountry())
                    .fillAddressAndCity(billingEntity.getAddress(), billingEntity.getCity(),
                            billingEntity.getState(), billingEntity.getZip())
                    .doneCreating();

            logger.info("Check creating of entity");

            //Add assert for creating pop-up
            Assertions.assertEquals(" Billing entity created \n  ×\n ", page.getByText("Billing entity created ×").textContent());

            //Statements according to the test flow
            final String VAT = "--";
            final String PUBLISHERS = "0";

            // List of data that appears in the list of billing entities
            List<String> cellNames = List.of(COMPANY_NAME, billingEntity.getRegistrationCode(), billingEntity.getCountry(), VAT, PUBLISHERS);

            // Stream to perform assertions isVisible()
            cellNames.stream()
                    .map(name -> page.getByRole(AriaRole.CELL, new Page.GetByRoleOptions().setName(name)))
                    .forEach(cell -> assertThat(cell.first()).isVisible());

            logger.info("Clean up or further actions...delete created entities");

            billingEntitiesPage
                    .deleteCreatedInTestEntity(COMPANY_NAME, billingEntity.getRegistrationCode());

            //Add assert for deleting pop-up
            Assertions.assertEquals("""
                     Billing entity deleted\s
                      ×
                     \
                    """, page.getByText("Billing entity deleted ×").textContent());

            // Stream to perform assertions isHidden()
            assertThat(page.getByRole(AriaRole.ROW, new Page.GetByRoleOptions().setName(COMPANY_NAME + " " + billingEntity.getRegistrationCode())).getByRole(AriaRole.BUTTON)).isHidden();

            logger.info("Test is finished");
            /* Generally this test running during 6 sec **/
        }
    }
}
