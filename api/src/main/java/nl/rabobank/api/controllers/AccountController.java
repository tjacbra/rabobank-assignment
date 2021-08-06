package nl.rabobank.api.controllers;

import nl.rabobank.account.PaymentAccount;
import nl.rabobank.account.SavingsAccount;
import nl.rabobank.api.converters.PowerOfAttorneyConverter;
import nl.rabobank.api.dto.AccountDto;
import nl.rabobank.api.dto.AccountTypeDto;
import nl.rabobank.api.dto.PowerOfAttorneyDto;
import nl.rabobank.api.services.PowerOfAttorneyService;
import nl.rabobank.authorizations.PowerOfAttorney;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@Validated
@RequestMapping(
        value = "/api",
        produces = MediaType.APPLICATION_JSON_VALUE)
public class AccountController {

    @Autowired
    private PowerOfAttorneyService powerOfAttorneyService;

    @Autowired
    private PowerOfAttorneyConverter powerOfAttorneyConverter;

    @GetMapping(path = "/accounts/grantee/{granteeName}")
    public List<AccountDto> getAccounts(@PathVariable("granteeName") String granteeName) {
        return powerOfAttorneyService.findAllPowerOfAttorneyByGranteeName(granteeName)
                .stream()
                .map(powerOfAttorneyConverter::convertDomainToAccountDto)
                .collect(Collectors.toList());
    }

    @PostMapping(path = "/power_of_attorneys", consumes = MediaType.APPLICATION_JSON_VALUE)
    public String createPowerOfAttorney(@Valid @RequestBody PowerOfAttorneyDto powerOfAttorneyDto) {
        boolean created = powerOfAttorneyService.createPowerOfAttorney(powerOfAttorneyConverter.convertDtoToDomain(powerOfAttorneyDto));
        return String.format("%s %s", powerOfAttorneyDto, created ? "created successfully" : "already granted");
    }


}
