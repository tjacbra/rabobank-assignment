package nl.rabobank.api.services;

import nl.rabobank.api.converters.PowerOfAttorneyConverter;
import nl.rabobank.authorizations.PowerOfAttorney;
import nl.rabobank.mongo.documents.PowerOfAttorneyDoc;
import nl.rabobank.mongo.repositories.PowerOfAttorneyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
public class PowerOfAttorneyService {

    @Autowired
    private PowerOfAttorneyConverter powerOfAttorneyConverter;

    @Autowired
    private PowerOfAttorneyRepository powerOfAttorneyRepository;

    public boolean createPowerOfAttorney(PowerOfAttorney powerOfAttorney) {
        var created = false;
        var powerOfAttorneyDoc = powerOfAttorneyConverter.convertDomainToDocument(powerOfAttorney);
        if (powerOfAttorneyRepository.findAllByGrantorNameAndGranteeNameAndAccountTypeAndAccountNumberAndAuthorization(
                powerOfAttorneyDoc.getGrantorName(),
                powerOfAttorneyDoc.getGranteeName(),
                powerOfAttorneyDoc.getAccountType(),
                powerOfAttorneyDoc.getAccountNumber(),
                powerOfAttorneyDoc.getAuthorization())
                .isEmpty()) {
            powerOfAttorneyRepository.save(powerOfAttorneyDoc);
            created = true;
        }
        return created;
    }

    public List<PowerOfAttorney> findAllPowerOfAttorneyByGranteeName(String granteeName) {
        return powerOfAttorneyRepository.findAllByGranteeName(granteeName)
                .stream()
                .map(powerOfAttorneyConverter::convertDocumentToDomain)
                .collect(toList());
    }
}
