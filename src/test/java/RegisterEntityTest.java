import com.microsoft.playwright.*;
import com.microsoft.playwright.options.AriaRole;
import com.microsoft.playwright.options.Cookie;
import io.qameta.allure.Allure;
import io.qameta.allure.Description;
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
import pageobjects.UsersPage;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
import static utils.ReaderResources.readCookiesFromResource;


public class RegisterEntityTest extends Base{

    private static final Logger logger = LogManager.getLogger(RegisterEntityTest.class);

    BillingEntity billingEntity = new BillingEntity();

    // Generate model - Note: Generate only for Company name - in real case better generate for all fields
    BillingEntity entityModel = Instancio.create(BillingEntity.getCompanyModel());
    final String COMPANY_NAME = entityModel.getCompanyName() + ".ltd";

    @Test
    @Feature("Login on prod with cookies")
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("Test: Login with cookies on prod and create entity")
    @Description("Test description: Login with cookies on prod and create entity E2E")
    public void registerEntityTest(){
        //Add cookie for context for this specified test
        logger.info("Read cookies from resources");
        List<Cookie> cookies;
        try {
            cookies = readCookiesFromResource("/cookies.json");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        context.addCookies(cookies);

        logger.info("Create new context with cookies and set Up timeout");

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

             //Allure screenshot
             Allure.addAttachment("Screenshot", new ByteArrayInputStream(page.screenshot()));

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

            //Allure screenshot
            Allure.addAttachment("Screenshot", new ByteArrayInputStream(page.screenshot()));

        logger.info("Test is finished");
        /* Generally this test running during 6 sec **/
    }
}

