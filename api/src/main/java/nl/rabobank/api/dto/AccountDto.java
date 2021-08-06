package nl.rabobank.api.dto;

import lombok.*;
import nl.rabobank.authorizations.Authorization;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class AccountDto {
    @NonNull
    private String grantorName;
    @NonNull
    private AccountTypeDto accountType;
    @NonNull
    private String accountNumber;
    @NonNull
    private Authorization authorization;
}
