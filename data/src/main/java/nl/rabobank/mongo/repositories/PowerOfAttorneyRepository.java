package nl.rabobank.mongo.repositories;

import nl.rabobank.mongo.documents.AccountTypeDoc;
import nl.rabobank.mongo.documents.AuthorizationDoc;
import nl.rabobank.mongo.documents.PowerOfAttorneyDoc;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PowerOfAttorneyRepository extends MongoRepository<PowerOfAttorneyDoc, Long> {

    List<PowerOfAttorneyDoc> findAllByGranteeName(@Param("granteeName") String granteeName);

    List<PowerOfAttorneyDoc> findAllByGrantorNameAndGranteeNameAndAccountTypeAndAccountNumberAndAuthorization(
            @Param("grantorName") String grantorName,
            @Param("granteeName") String granteeName,
            @Param("accountType") AccountTypeDoc accountType,
            @Param("accountNumber") String accountNumber,
            @Param("authorization") AuthorizationDoc authorization
    );

}
