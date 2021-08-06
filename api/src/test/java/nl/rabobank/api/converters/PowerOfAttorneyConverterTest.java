package nl.rabobank.api.converters;

import nl.rabobank.account.PaymentAccount;
import nl.rabobank.api.dto.AccountTypeDto;
import nl.rabobank.api.dto.PowerOfAttorneyDto;
import nl.rabobank.authorizations.Authorization;
import nl.rabobank.authorizations.PowerOfAttorney;
import nl.rabobank.mongo.documents.AccountTypeDoc;
import nl.rabobank.mongo.documents.AuthorizationDoc;
import nl.rabobank.mongo.documents.PowerOfAttorneyDoc;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class PowerOfAttorneyConverterTest {

    @InjectMocks
    private PowerOfAttorneyConverter powerOfAttorneyConverter;

    @Test
    public void givenAPowerOfAttorney_thenConvertDomainToDocument_shouldReturnADocument() {
        var doc = powerOfAttorneyConverter.convertDomainToDocument(PowerOfAttorney
                .builder()
                .grantorName("grantor")
                .granteeName("grantee")
                .authorization(Authorization.WRITE)
                .account(new PaymentAccount("accountNumber", "", 0.0))
                .build());
        assertEquals("accountNumber", doc.getAccountNumber());
        assertEquals("grantee", doc.getGranteeName());
        assertEquals("grantor", doc.getGrantorName());
        assertEquals(AuthorizationDoc.WRITE, doc.getAuthorization());
        assertEquals(AccountTypeDoc.PAYMENT_ACCOUNT, doc.getAccountType());
    }

    @Test
    public void givenAPowerOfAttorneyDoc_thenConvertDocumentToDomain_shouldReturnADomainObject() {
        var powerOfAttorney = powerOfAttorneyConverter.convertDocumentToDomain(PowerOfAttorneyDoc
                .builder()
                .grantorName("grantor")
                .granteeName("grantee")
                .authorization(AuthorizationDoc.WRITE)
                .accountNumber("accountNumber")
                .accountType(AccountTypeDoc.PAYMENT_ACCOUNT)
                .build()
        );
        assertEquals("grantee", powerOfAttorney.getGranteeName());
        assertEquals("grantor", powerOfAttorney.getGrantorName());
        assertEquals("accountNumber", powerOfAttorney.getAccount().getAccountNumber());
        assertEquals(Authorization.WRITE, powerOfAttorney.getAuthorization());
    }

    @Test
    public void givenAPowerOfAttorneyDto_thenConvertDtoToDomain_shouldReturnADomainObject() {
        var powerOfAttorney = powerOfAttorneyConverter.convertDtoToDomain(PowerOfAttorneyDto
                .builder()
                .grantorName("grantor")
                .granteeName("grantee")
                .authorization(Authorization.WRITE)
                .accountNumber("accountNumber")
                .accountType(AccountTypeDto.PAYMENT_ACCOUNT)
                .build()
        );
        assertEquals("grantee", powerOfAttorney.getGranteeName());
        assertEquals("grantor", powerOfAttorney.getGrantorName());
        assertEquals("accountNumber", powerOfAttorney.getAccount().getAccountNumber());
        assertEquals(Authorization.WRITE, powerOfAttorney.getAuthorization());
    }

    @Test
    public void givenAPowerOfAttorney_thenConvertDomainToAccountDto_shouldReturnAnAccountDto() {
        var accountDto = powerOfAttorneyConverter.convertDomainToAccountDto(PowerOfAttorney
                .builder()
                .grantorName("grantor")
                .granteeName("grantee")
                .authorization(Authorization.WRITE)
                .account(new PaymentAccount("accountNumber", "", 0.0))
                .build()
        );
        assertEquals("grantor", accountDto.getGrantorName());
        assertEquals("accountNumber", accountDto.getAccountNumber());
        assertEquals(AccountTypeDto.PAYMENT_ACCOUNT, accountDto.getAccountType());
        assertEquals(Authorization.WRITE, accountDto.getAuthorization());
    }

}
