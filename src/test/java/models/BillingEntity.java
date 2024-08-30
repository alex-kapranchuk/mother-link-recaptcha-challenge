package models;

import org.instancio.Instancio;
import org.instancio.Model;

import static org.instancio.Select.field;

public class BillingEntity {

    private final String REGISTRATION_CODE = "EE000000";
    private final String COUNTRY = "Estonia";
    private final String ADDRESS = "Vesivarava 50";
    private final String CITY = "Tallinn";
    private final String STATE = "Harjumaa";
    private final String ZIP = "123321";

    private String companyName;

    public static Model<BillingEntity> getCompanyModel() {
        return companyModel;
    }

    static Model<BillingEntity> companyModel = Instancio.of(BillingEntity.class)
            .generate(field(BillingEntity::getCompanyName), gen -> gen.text().pattern("#C#c#c#c#c#c#c#c#c"))
            .toModel();

    public String getRegistrationCode() {
        return REGISTRATION_CODE;
    }

    public String getCountry() {
        return COUNTRY;
    }

    public String getAddress() {
        return ADDRESS;
    }

    public String getCity() {
        return CITY;
    }

    public String getState() {
        return STATE;
    }

    public String getZip() {
        return ZIP;
    }

    public String getCompanyName() {
        return companyName;
    }
}
