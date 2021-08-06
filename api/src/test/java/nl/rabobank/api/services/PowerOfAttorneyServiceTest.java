package nl.rabobank.api.services;

import nl.rabobank.account.PaymentAccount;
import nl.rabobank.api.converters.PowerOfAttorneyConverter;
import nl.rabobank.authorizations.Authorization;
import nl.rabobank.authorizations.PowerOfAttorney;
import nl.rabobank.mongo.documents.AccountTypeDoc;
import nl.rabobank.mongo.documents.AuthorizationDoc;
import nl.rabobank.mongo.documents.PowerOfAttorneyDoc;
import nl.rabobank.mongo.repositories.PowerOfAttorneyRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PowerOfAttorneyServiceTest {

    @Mock
    private PowerOfAttorneyConverter powerOfAttorneyConverter;

    @Mock
    private PowerOfAttorneyRepository powerOfAttorneyRepository;

    @InjectMocks
    private PowerOfAttorneyService powerOfAttorneyService;

    @Test
    public void givenANewPowerOfAttorney_thenCreatePowerOfAttorney_shouldSavePowerOfAttorneyDocument() {
        var powerOfAttorneyDocArgumentCaptor = ArgumentCaptor.forClass(PowerOfAttorneyDoc.class);

        when(powerOfAttorneyRepository.findAllByGrantorNameAndGranteeNameAndAccountTypeAndAccountNumberAndAuthorization(
                anyString(), anyString(), any(AccountTypeDoc.class), anyString(), any(AuthorizationDoc.class)))
                .thenReturn(new ArrayList<>());
        when(powerOfAttorneyRepository.save(powerOfAttorneyDocArgumentCaptor.capture())).thenReturn(null);
        when(powerOfAttorneyConverter.convertDomainToDocument(any(PowerOfAttorney.class))).thenCallRealMethod();

        powerOfAttorneyService.createPowerOfAttorney(
                PowerOfAttorney
                        .builder()
                        .grantorName("grantor")
                        .granteeName("grantee")
                        .authorization(Authorization.WRITE)
                        .account(new PaymentAccount("accountNumber", "", 0.0))
                        .build()
        );

        assertNotNull(powerOfAttorneyDocArgumentCaptor.getValue());
    }

    @Test
    public void givenAllReadyExistingOfPowerOfAttorney_thenCreatePowerOfAttorney_shouldNotSavePowerOfAttorneyDocument() {
        var powerOfAttorneyDoc = PowerOfAttorneyDoc
                .builder()
                .grantorName("grantor")
                .granteeName("grantee")
                .authorization(AuthorizationDoc.WRITE)
                .accountNumber("accountNumber")
                .accountType(AccountTypeDoc.PAYMENT_ACCOUNT)
                .build();

        when(powerOfAttorneyRepository.findAllByGrantorNameAndGranteeNameAndAccountTypeAndAccountNumberAndAuthorization(
                anyString(), anyString(), any(AccountTypeDoc.class), anyString(), any(AuthorizationDoc.class)))
                .thenReturn(List.of(powerOfAttorneyDoc));
        when(powerOfAttorneyConverter.convertDomainToDocument(any(PowerOfAttorney.class))).thenCallRealMethod();
        when(powerOfAttorneyConverter.convertDocumentToDomain(any(PowerOfAttorneyDoc.class))).thenCallRealMethod();

        powerOfAttorneyService.createPowerOfAttorney(powerOfAttorneyConverter.convertDocumentToDomain(powerOfAttorneyDoc));

        verify(powerOfAttorneyRepository, times(0)).save(any(PowerOfAttorneyDoc.class));
    }

    @Test
    public void givenAGranteeName_thenFindAllPowerOfAttorneyByGranteeName_shouldReturnAllPowerOfAttorneyForTheGrantee() {
        when(powerOfAttorneyRepository.findAllByGranteeName(anyString())).thenReturn(List.of(PowerOfAttorneyDoc
                .builder()
                .accountType(AccountTypeDoc.PAYMENT_ACCOUNT)
                .accountNumber("accountNumber")
                .granteeName("grantee")
                .grantorName("grantor")
                .authorization(AuthorizationDoc.WRITE)
                .build()));
        when(powerOfAttorneyConverter.convertDocumentToDomain(any(PowerOfAttorneyDoc.class))).thenCallRealMethod();

        var poas = powerOfAttorneyService.findAllPowerOfAttorneyByGranteeName("");
        assertFalse(poas.isEmpty());
        assertEquals(1, poas.size());
    }

}
