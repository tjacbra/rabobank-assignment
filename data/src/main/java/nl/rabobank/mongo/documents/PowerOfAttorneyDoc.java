package nl.rabobank.mongo.documents;

import lombok.*;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "PowerOfAttorney")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@CompoundIndex(
        unique = true,
        name = "id",
        def = "{'granteeName' : 1, 'grantorName': 1, 'accountType': 1, 'accountNumber': 1, 'authorization': 1}")
public class PowerOfAttorneyDoc {
    @Indexed(name = "grantee_name")
    @NonNull
    private String granteeName;
    @NonNull
    private String grantorName;
    @NonNull
    private AccountTypeDoc accountType;
    @NonNull
    private String accountNumber;
    @NonNull
    private AuthorizationDoc authorization;

}
