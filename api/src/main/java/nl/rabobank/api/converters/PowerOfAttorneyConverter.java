package nl.rabobank.api.converters;

import nl.rabobank.account.PaymentAccount;
import nl.rabobank.account.SavingsAccount;
import nl.rabobank.api.dto.AccountDto;
import nl.rabobank.api.dto.AccountTypeDto;
import nl.rabobank.api.dto.PowerOfAttorneyDto;
import nl.rabobank.authorizations.Authorization;
import nl.rabobank.authorizations.PowerOfAttorney;
import nl.rabobank.mongo.documents.AccountTypeDoc;
import nl.rabobank.mongo.documents.AuthorizationDoc;
import nl.rabobank.mongo.documents.PowerOfAttorneyDoc;
import org.springframework.stereotype.Service;

@Service
public class PowerOfAttorneyConverter {

    public PowerOfAttorneyDoc convertDomainToDocument(PowerOfAttorney powerOfAttorney) {
        AccountTypeDoc accountType = powerOfAttorney.getAccount() instanceof PaymentAccount ?
                AccountTypeDoc.PAYMENT_ACCOUNT :
                AccountTypeDoc.SAVINGS_ACCOUNT;
        AuthorizationDoc authorization = AuthorizationDoc.valueOf(powerOfAttorney.getAuthorization().name());
        return PowerOfAttorneyDoc
                .builder()
                .grantorName(powerOfAttorney.getGrantorName())
                .granteeName(powerOfAttorney.getGranteeName())
                .accountType(accountType)
                .accountNumber(powerOfAttorney.getAccount().getAccountNumber())
                .authorization(authorization)
                .build();
    }

    public PowerOfAttorney convertDocumentToDomain(PowerOfAttorneyDoc powerOfAttorneyDoc) {
        return PowerOfAttorney
                .builder()
                .granteeName(powerOfAttorneyDoc.getGranteeName())
                .grantorName(powerOfAttorneyDoc.getGrantorName())
                .authorization(Authorization.valueOf(powerOfAttorneyDoc.getAuthorization().name()))
                .account(powerOfAttorneyDoc.getAccountType() == AccountTypeDoc.PAYMENT_ACCOUNT ?
                        new PaymentAccount(powerOfAttorneyDoc.getAccountNumber(), "", 0.0) :
                        new SavingsAccount(powerOfAttorneyDoc.getAccountNumber(), "", 0.0))
                .build();
    }

    public PowerOfAttorney convertDtoToDomain(PowerOfAttorneyDto powerOfAttorneyDto) {
        return PowerOfAttorney
                .builder()
                .grantorName(powerOfAttorneyDto.getGrantorName())
                .granteeName(powerOfAttorneyDto.getGranteeName())
                .authorization(powerOfAttorneyDto.getAuthorization())
                .account(powerOfAttorneyDto.getAccountType() == AccountTypeDto.PAYMENT_ACCOUNT ?
                        new PaymentAccount(powerOfAttorneyDto.getAccountNumber(), "", 0.0) :
                        new SavingsAccount(powerOfAttorneyDto.getAccountNumber(), "", 0.0))
                .build();
    }

    public AccountDto convertDomainToAccountDto(PowerOfAttorney powerOfAttorney) {
        return AccountDto
                .builder()
                .grantorName(powerOfAttorney.getGrantorName())
                .accountType(powerOfAttorney.getAccount() instanceof PaymentAccount ?
                        AccountTypeDto.PAYMENT_ACCOUNT :
                        AccountTypeDto.SAVINGS_ACCOUNT)
                .accountNumber(powerOfAttorney.getAccount().getAccountNumber())
                .authorization(powerOfAttorney.getAuthorization())
                .build();
    }
}
