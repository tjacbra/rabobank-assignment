package nl.rabobank.mongo.repositories;

import nl.rabobank.mongo.MongoConfiguration;
import nl.rabobank.mongo.documents.AccountTypeDoc;
import nl.rabobank.mongo.documents.AuthorizationDoc;
import nl.rabobank.mongo.documents.PowerOfAttorneyDoc;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.ContextConfiguration;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ContextConfiguration(classes = MongoConfiguration.class)
@DataMongoTest()
public class PowerOfAttorneyRepositoryTest {

    @Autowired
    private PowerOfAttorneyRepository powerOfAttorneyRepository;

    @BeforeEach
    public void dataSetUp() {
        powerOfAttorneyRepository.deleteAll();
        powerOfAttorneyRepository.saveAll(
                List.of(PowerOfAttorneyDoc
                                .builder()
                                .accountType(AccountTypeDoc.PAYMENT_ACCOUNT)
                                .grantorName("grantor")
                                .accountNumber("accountNumber")
                                .authorization(AuthorizationDoc.WRITE)
                                .granteeName("grantee")
                                .build(),
                        PowerOfAttorneyDoc
                                .builder()
                                .accountType(AccountTypeDoc.PAYMENT_ACCOUNT)
                                .grantorName("grantor")
                                .accountNumber("accountNumber")
                                .authorization(AuthorizationDoc.WRITE)
                                .granteeName("grantee2")
                                .build()
                ));
    }

    @Test
    public void givenAGrantee_thenFindAllByGranteeName_shouldFindAllDocsForGrantee() {
        var powerOfAttorneyDocs = powerOfAttorneyRepository.findAllByGranteeName("grantee");
        assertNotNull(powerOfAttorneyDocs);
        assertEquals(1, powerOfAttorneyDocs.size());
    }

    @Test
    public void givenAGranteeEtc_thenFindAllByGrantorNameAndGranteeNameAndAccountTypeAndAccountNumberAndAuthorization_shouldFindAllDocsForGranteeEtc() {
        var powerOfAttorneyDocs = powerOfAttorneyRepository
                .findAllByGrantorNameAndGranteeNameAndAccountTypeAndAccountNumberAndAuthorization(
                        "grantor", "grantee", AccountTypeDoc.PAYMENT_ACCOUNT, "accountNumber", AuthorizationDoc.WRITE);
        assertNotNull(powerOfAttorneyDocs);
        assertEquals(1, powerOfAttorneyDocs.size());
    }


}
